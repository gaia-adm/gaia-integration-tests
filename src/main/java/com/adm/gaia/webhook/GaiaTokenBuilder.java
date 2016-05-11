package com.adm.gaia.webhook;

import com.adm.gaia.rest.RestClient;
import com.adm.gaia.Constants;
import com.adm.gaia.rest.RestRequest;
import com.adm.gaia.rest.RestResponse;
import com.adm.gaia.util.GaiaEtcd;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GaiaTokenBuilder {

    private static final Logger _logger = LoggerFactory.getLogger(GaiaTokenBuilder.class);
    @Autowired
    private GaiaConfiguration _config;
    @Autowired
    private GaiaUrlContainer _urlContainer;
    @Autowired
    private GaiaEtcd _etcd;

    public String build(long tenantId) {

        createClient(tenantId);
        String token = createToken();
        _etcd.putToken(token);

        return token;
    }

    private String createToken() {

        String url = null, ret = null;
        try {
            url =
                    _urlContainer.getGaiaUrl()
                    + String.format(
                            Constants.CREATE_TOKEN_SUFFIX_FORMAT,
                            getClientName(),
                            _config.getClientSecret());
            RestResponse
                    response =
                    RestClient.post(new RestRequest(url));
            JSONObject jsonObject = new JSONObject(response.getResponseBody());
            _logger.debug(jsonObject.toString());
            ret = jsonObject.getString("access_token");
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to create token, URL: %s", url), ex);
        }

        return ret;
    }

    private void createClient(long tenantId) {

        String url = null, body = null;
        try {
            url = _urlContainer.getGaiaUrl() + Constants.CREATE_CLIENT_SUFFIX;
            body = getJsonBodyCreateClient(tenantId);
            RestResponse
                    response =
                    RestClient.post(new RestRequest(url,
                            body,
                            Constants.APPLICATION_JSON,
                            Constants.APPLICATION_JSON));
            _logger.debug(response.getResponseMessage());
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to create client, URL: %s, body: %s",
                    url,
                    body), ex);
        }
    }

    private synchronized String getClientName() {

        return _config.getClientName();
    }

    private String getJsonBodyCreateClient(long tenantId) {

        return new JSONObject().put("client_id", getClientName()).put("client_secret",
                _config.getClientSecret()).put("scope", "read,write,trust").put(
                "authorized_grant_types",
                "client_credentials").put("authorities", "ROLE_APP").put("tenantId",
                tenantId).toString();
    }
}
