import unittest
from unittest.mock import patch, MagicMock

from onebound import OneBoundClient


class TestOneBoundClient(unittest.TestCase):

    def setUp(self):
        self.client = OneBoundClient("taobao", "test_key", "test_secret")

    def test_client_initialization(self):
        self.assertEqual(self.client.key, "test_key")
        self.assertEqual(self.client.secret, "test_secret")
        self.assertEqual(self.client.api_url, OneBoundClient.API_URLS[0])

    def test_client_custom_api_url(self):
        custom_url = "https://custom-api.example.com"
        client = OneBoundClient("taobao", "test_key", "test_secret", custom_url)
        self.assertEqual(client.api_url, custom_url)

    def test_client_env_vars(self):
        with patch.dict('os.environ', {'ONEBOUND_KEY': 'env_key', 'ONEBOUND_SECRET': 'env_secret'}):
            client = OneBoundClient("taobao")
            self.assertEqual(client.key, "env_key")
            self.assertEqual(client.secret, "env_secret")

    @patch('onebound.client.requests.get')
    def test_item_get(self, mock_get):
        mock_response = MagicMock()
        mock_response.json.return_value = {
            "item": {
                "num_iid": "123456",
                "title": "Test Item",
                "price": "99.00"
            },
            "error_code": "0000"
        }
        mock_response.raise_for_status.return_value = None
        mock_get.return_value = mock_response

        result = self.client.taobao_item_get(num_iid="123456")

        self.assertEqual(result["item"]["num_iid"], "123456")
        self.assertEqual(result["item"]["title"], "Test Item")

    @patch('onebound.client.requests.get')
    def test_item_search(self, mock_get):
        mock_response = MagicMock()
        mock_response.json.return_value = {
            "items": [
                {"num_iid": "1", "title": "Item 1"},
                {"num_iid": "2", "title": "Item 2"}
            ],
            "total_results": "2",
            "error_code": "0000"
        }
        mock_response.raise_for_status.return_value = None
        mock_get.return_value = mock_response

        result = self.client.taobao_item_search(q="test query")

        self.assertEqual(len(result["items"]), 2)

    @patch('onebound.client.requests.get')
    def test_seller_info(self, mock_get):
        mock_response = MagicMock()
        mock_response.json.return_value = {
            "seller": {
                "shop_id": "12345",
                "shop_name": "Test Shop",
                "nick": "test_nick"
            },
            "error_code": "0000"
        }
        mock_response.raise_for_status.return_value = None
        mock_get.return_value = mock_response

        result = self.client.taobao_item_get(num_iid="12345")

        self.assertIn("seller", result)

    @patch('onebound.client.requests.get')
    def test_unsupported_platform(self, mock_get):
        with self.assertRaises(ValueError):
            OneBoundClient("nonexistent_platform", "test_key", "test_secret")


if __name__ == '__main__':
    unittest.main()