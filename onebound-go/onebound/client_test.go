package onebound

import (
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestNewClient(t *testing.T) {
	client := NewClient("taobao", "test_key", "test_secret")

	if client.key != "test_key" {
		t.Errorf("Expected key 'test_key', got '%s'", client.key)
	}

	if client.secret != "test_secret" {
		t.Errorf("Expected secret 'test_secret', got '%s'", client.secret)
	}

	if client.apiURL != "https://api-gw.onebound.cn" {
		t.Errorf("Expected apiURL 'https://api-gw.onebound.cn', got '%s'", client.apiURL)
	}
}

func TestBuildURL(t *testing.T) {
	client := NewClient("taobao", "test_key", "test_secret")

	params := map[string]string{
		"num_iid": "123456",
		"q":       "test",
	}

	url := client.buildURL("item_get", params)

	if url == "" {
		t.Error("buildURL returned empty string")
	}

	if !contains(url, "key=test_key") {
		t.Error("URL should contain key parameter")
	}

	if !contains(url, "api_name=item_get") {
		t.Error("URL should contain api_name parameter")
	}
}

func TestItemGetRequest(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		response := map[string]interface{}{
			"item": map[string]interface{}{
				"num_iid": "123456",
				"title":   "Test Item",
				"price":   "99.00",
			},
			"error_code": "0000",
		}
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}))
	defer server.Close()

	client := NewClient("taobao", "test_key", "test_secret")
	client.apiURL = server.URL

	result, err := client.ItemGet("123456", nil)

	if err != nil {
		t.Errorf("ItemGet returned error: %v", err)
	}

	if result["error_code"] != "0000" {
		t.Errorf("Expected error_code '0000', got '%v'", result["error_code"])
	}
}

func contains(s, substr string) bool {
	return len(s) >= len(substr) && (s == substr || len(s) > 0 && containsHelper(s, substr))
}

func containsHelper(s, substr string) bool {
	for i := 0; i <= len(s)-len(substr); i++ {
		if s[i:i+len(substr)] == substr {
			return true
		}
	}
	return false
}
