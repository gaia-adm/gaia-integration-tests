package com.adm.gaia.webhook;

import com.adm.gaia.webhook.rest.RestClient;
import com.adm.gaia.webhook.rest.RestConstants;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by kornfeld on 20/03/2016.
 */
@Component
public class WebhookGenerator {

    @Autowired
    private GaiaConfiguration _config;

    public void generate(String accessToken, String dataSource, String eventType) {

        String url = null, body = null;
        try {
            url = _config.getGaiaWHSUrl() + RestConstants.GENERATE_WEBHOOK;
            body = new JSONObject().
                    put("datasource", dataSource).
                    put("event", eventType).toString();
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to generate webhook, URL: %s",
                    url), ex);
        }
    }
}
