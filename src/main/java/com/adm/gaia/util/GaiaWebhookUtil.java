package com.adm.gaia.util;

import com.adm.gaia.Constants;
import com.adm.gaia.GaiaConfiguration;
import com.adm.gaia.common.GaiaITestException;
import com.adm.gaia.rest.RestClient;
import com.adm.gaia.rest.RestRequest;
import com.adm.gaia.rest.RestResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;

@Component
public class GaiaWebhookUtil {

    private static final Logger _logger = LoggerFactory.getLogger(GaiaTokenBuilder.class);
    @Autowired
    private GaiaUrlContainer _urlContainer;
    @Autowired
    private GaiaConfiguration _config;

    public String generate(String accessToken, String dataSource, String eventType) {

        String url = null, body = null, ret = "";
        try {
            url = getWebhookUrl();
            body =
                    new JSONObject().put("datasource", dataSource).put("event",
                            eventType).toString();
            RestResponse response = RestClient.post(getRequest(accessToken, url, body));
            JSONObject jsonObject = new JSONObject(response.getResponseBody());
            _logger.debug(jsonObject.toString());
            ret = jsonObject.getString("hookUrl");
        } catch (Exception ex) {
            throw new GaiaITestException(String.format("Failed to generate webhook, URL: %s", url),
                    ex);
        }

        return ret;
    }

    public void publish(String accessToken, String hookUrl, String event) {

        try {
            RestResponse response = RestClient.post(getRequest(accessToken, hookUrl, event));
            _logger.debug(response.getResponseMessage());
        } catch (Exception ex) {
            throw new GaiaITestException(String.format("Failed to publish event to webhook, URL: %s",
                    hookUrl), ex);
        }
    }

    public void deleteTenantWebhook(String accessToken) {

        String webhooks = getTenantWebhooks(accessToken);
        if (webhooks != null && !webhooks.isEmpty()) {
            String token = getWebhookToken(webhooks);
            String url = String.format("%s/%s", getWebhookUrl(), token);
            RestClient.delete(getRequest(accessToken, url, null)).getResponseBody();
        }
    }

    private String getTenantWebhooks(String accessToken) {

        String url = null, ret = null;
        try {
            url = getWebhookUrl();
            ret = RestClient.get(getRequest(accessToken, url, null)).getResponseBody();
        } catch (Exception ex) {
            _logger.warn(String.format("Failed to get tenants' webhooks, URL: %s", url), ex);
        }

        return ret;
    }

    private String getWebhookToken(String webhooks) {
        JSONArray json = new JSONArray(webhooks);
        int count = json.length();
        Assert.assertEquals(count, 1, "Expected 1 webhook but found " + count);
        return json.getJSONObject(0).get("token").toString();
    }

    private String getWebhookUrl() {

        return _urlContainer.getGaiaUrl() + Constants.WEBHOOK_SUFFIX;
    }

    private RestRequest getRequest(String accessToken, String url, String body) {

        return new RestRequest(url,
                body,
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON).header("Authorization", "Bearer " + accessToken).header("X-ORIG-SERVER", _config.getGaiaHost());
    }
}
