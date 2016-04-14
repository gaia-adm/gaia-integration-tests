package com.adm.gaia.webhook;

import com.adm.gaia.webhook.rest.RestClient;
import com.adm.gaia.webhook.rest.RestConstants;
import com.adm.gaia.webhook.rest.RestRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by kornfeld on 20/03/2016.
 */
@Component
public class WebhookGenerator {

    private static final Logger _logger = LoggerFactory.getLogger(GaiaTokenBuilder.class);
    @Autowired
    private GaiaConfiguration _config;

    public String generate(String accessToken, String dataSource, String eventType) {

        String url = null, body = null, ret = "";
        try {
            url = _config.getGaiaUrl() + RestConstants.GENERATE_WEBHOOK;
            body = new JSONObject().
                    put("datasource", dataSource).
                    put("event", eventType).toString();
            RestRequest restRequest = new RestRequest(url,
                    body,
                    RestConstants.APPLICATION_JSON,
                    RestConstants.APPLICATION_JSON).header("Authorization", "Bearer " + accessToken);
            String response = RestClient.post(restRequest);
            JSONObject jsonObject = new JSONObject(response);
            _logger.debug(jsonObject.toString());
            ret = jsonObject.getString("hookUrl");
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to generate webhook, URL: %s",
                    url), ex);
        }

        return ret;
    }

    public void publish(String accessToken, String hookUrl, String event) {

        try {
            RestRequest restRequest = new RestRequest(hookUrl,
                    event,
                    RestConstants.APPLICATION_JSON,
                    RestConstants.APPLICATION_JSON).header("Authorization", "Bearer " + accessToken);
            String response = RestClient.post(restRequest);
            _logger.debug(response);
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to publish event to webhook, URL: %s",
                    hookUrl), ex);
        }
    }
}
