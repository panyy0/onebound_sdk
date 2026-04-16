"""
Integration tests for OneBound SDK.

Tests real API calls to verify connectivity and response format.
Requires ONEBOUND_KEY and ONEBOUND_SECRET environment variables or a .env file.

Note: Each platform requires separate API access. Tests for platforms
without access will be skipped with a warning.
"""
import os
import unittest
from pathlib import Path

# Load .env file from project root
env_path = Path(__file__).resolve().parent.parent.parent / ".env"
if env_path.exists():
    with open(env_path) as f:
        for line in f:
            line = line.strip()
            if line and not line.startswith("#") and "=" in line:
                key, value = line.split("=", 1)
                os.environ.setdefault(key.strip(), value.strip())

from onebound import OneBoundClient

KEY = os.environ.get("ONEBOUND_KEY", "")
SECRET = os.environ.get("ONEBOUND_SECRET", "")

# All categories with one representative platform each
CATEGORIES = [
    ("国内电商", "taobao"),
    ("海外电商", "amazon"),
    ("社交媒体", "smallredbook"),
    ("酒店旅游", "xiecheng"),
    ("垂直电商", "vip"),
    ("五金工业", "vipmro"),
    ("新闻资讯", "jmweb"),
    ("二手闲置", "zhuanzhuan"),
    ("企业信息", "tyc"),
    ("在线书店", "dangdang"),
    ("招投标", "jxsggzy"),
    ("商户数据", "yhby"),
]

# Known item IDs for platforms with access
KNOWN_ITEMS = {
    "taobao": "674436494131",
}


@unittest.skipUnless(KEY and SECRET, "ONEBOUND_KEY and ONEBOUND_SECRET not set")
class TestIntegration(unittest.TestCase):
    """Integration tests hitting real OneBound API endpoints."""

    def _call_api(self, platform: str, api_name: str, params: dict) -> dict:
        """Call an API and return the result. Returns None if access denied or unavailable."""
        client = OneBoundClient(platform, KEY, SECRET)
        try:
            result = client._request(api_name, params)
        except Exception as e:
            # API returned non-JSON or network error - platform may not support this API
            self.skipTest(f"{platform}: API request failed ({str(e)[:80]})")
        error_code = result.get("error_code")
        if error_code and str(error_code) == "4005":
            return None  # No access to this platform
        return result

    def _assert_success(self, platform: str, result: dict, api_desc: str):
        """Assert the API call was successful."""
        self.assertIsInstance(result, dict)
        error_code = result.get("error_code")
        if error_code is not None:
            self.assertEqual(
                str(error_code), "0000",
                f"{platform} {api_desc} failed: {result}"
            )

    def test_taobao_item_get(self):
        """Test taobao item_get with a known item ID."""
        client = OneBoundClient("taobao", KEY, SECRET)
        result = client.taobao_item_get(num_iid="674436494131")
        self._assert_success("taobao", result, "item_get")

    def test_taobao_item_search(self):
        """Test taobao item_search."""
        client = OneBoundClient("taobao", KEY, SECRET)
        result = client.taobao_item_search(q="手机")
        self._assert_success("taobao", result, "item_search")


# Dynamically generate test methods for each category
for _name, _platform in CATEGORIES:
    # Skip taobao - already has explicit tests above
    if _platform == "taobao":
        continue

    def _make_search_test(platform, name):
        def test_method(self):
            result = self._call_api(platform, "item_search", {"q": "test"})
            if result is None:
                self.skipTest(f"{platform} (error 4005: no access, contact provider to activate)")
            self._assert_success(platform, result, "item_search")
        test_method.__doc__ = f"Test {name} ({platform}): item_search"
        return test_method

    def _make_get_test(platform, name):
        def test_method(self):
            num_iid = KNOWN_ITEMS.get(platform)
            if not num_iid:
                self.skipTest(f"{platform}: no known item ID for item_get test")
            result = self._call_api(platform, "item_get", {"num_iid": num_iid})
            if result is None:
                self.skipTest(f"{platform} (error 4005: no access, contact provider to activate)")
            self._assert_success(platform, result, "item_get")
        test_method.__doc__ = f"Test {name} ({platform}): item_get"
        return test_method

    setattr(TestIntegration, f"test_{_platform}_item_search", _make_search_test(_platform, _name))
    if _platform in KNOWN_ITEMS:
        setattr(TestIntegration, f"test_{_platform}_item_get", _make_get_test(_platform, _name))


if __name__ == "__main__":
    unittest.main()