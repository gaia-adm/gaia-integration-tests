package com.adm.gaia.util;

import com.adm.gaia.Constants;
import com.adm.gaia.rest.RestClient;
import com.adm.gaia.rest.RestRequest;
import com.adm.gaia.rest.RestResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GaiaWebhookUtil {

    private static final Logger _logger = LoggerFactory.getLogger(GaiaTokenBuilder.class);
    @Autowired
    private GaiaUrlContainer _urlContainer;

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
            throw new RuntimeException(String.format("Failed to generate webhook, URL: %s", url),
                    ex);
        }

        return ret;
    }

    public void publish(String accessToken, String hookUrl, String event) {

        try {
            RestResponse response = RestClient.post(getRequest(accessToken, hookUrl, event));
            _logger.debug(response.getResponseMessage());
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to publish event to webhook, URL: %s",
                    hookUrl), ex);
        }
    }

    public String getTenantWebhooks(String accessToken) {

        String url = null, ret = null;
        try {
            url = getWebhookUrl();
            ret = RestClient.get(getRequest(accessToken, url, null)).getResponseBody();
        } catch (Exception ex) {
            _logger.error(String.format("Failed to get tenants' webhooks, URL: %s", url), ex);
        }

        return ret;
    }

    public void deleteTenantWebhooks(String accessToken) {

        String webhooks = getTenantWebhooks(accessToken);
        if (webhooks != null) {

        }
    }

    private String getWebhookUrl() {

        return _urlContainer.getGaiaUrl() + Constants.WEBHOOK_SUFFIX;
    }

    private RestRequest getRequest(String accessToken, String url, String body) {

        return new RestRequest(url,
                body,
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON).header(
                "Authorization",
                "Bearer " + accessToken);
    }
}
