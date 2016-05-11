package com.adm.gaia.webhook;

import com.adm.gaia.rest.RestClient;
import com.adm.gaia.rest.RestConstants;
import com.adm.gaia.rest.RestRequest;
import com.adm.gaia.rest.RestResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GaiaWebhookGenerator {

    private static final Logger _logger = LoggerFactory.getLogger(GaiaTokenBuilder.class);
    @Autowired
    private GaiaUrlContainer _urlContainer;

    public String generate(String accessToken, String dataSource, String eventType) {

        String url = null, body = null, ret = "";
        try {
            url = _urlContainer.getGaiaUrl() + RestConstants.GENERATE_WEBHOOK;
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
            throw new RuntimeException(String.format(
                    "Failed to publish event to webhook, URL: %s",
                    hookUrl), ex);
        }
    }

    private RestRequest getRequest(String accessToken, String url, String body) {

        return new RestRequest(url,
                body,
                RestConstants.APPLICATION_JSON,
                RestConstants.APPLICATION_JSON).header("Authorization", "Bearer " + accessToken);
    }
}
