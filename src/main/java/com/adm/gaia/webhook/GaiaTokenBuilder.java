package com.adm.gaia.webhook;

import com.adm.gaia.util.GaiaEtcd;
import com.adm.gaia.util.GaiaTenantUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.adm.gaia.webhook.rest.RestClient;
import com.adm.gaia.webhook.rest.RestConstants;
import com.adm.gaia.webhook.rest.RestRequest;
import com.adm.gaia.webhook.rest.RestResponse;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GaiaTokenBuilder {
    
    private static final Logger _logger = LoggerFactory.getLogger(GaiaTokenBuilder.class);
    @Autowired
    private GaiaConfiguration _config;
    @Autowired
    private GaiaUrlContainer _urlContainer;
    @Autowired
    private GaiaTenantUtil _tenant;
    @Autowired
    private GaiaEtcd _etcd;

    public String build() {
        
        createClient(_tenant.create());
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
                          RestConstants.CREATE_TOKEN_SUFFIX_FORMAT,
                          getClientName(),
                          _config.getClientSecret());
            RestResponse response =
                    RestClient.post(new RestRequest(url, RestConstants.APPLICATION_JSON));
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
            url = _urlContainer.getGaiaUrl() + RestConstants.CREATE_CLIENT_SUFFIX;
            body = getJsonBodyCreateClient(tenantId);
            RestResponse response =
                    RestClient.post(
                            new RestRequest(
                                    url,
                                    body,
                                    RestConstants.APPLICATION_JSON,
                                    RestConstants.APPLICATION_JSON));
            _logger.debug(response.getResponseMessage());
        } catch (Exception ex) {
            throw new RuntimeException(
                    String.format("Failed to create client, URL: %s, body: %s", url, body),
                    ex);
        }
    }

    private synchronized String getClientName() {

        return _config.getClientName();
    }
    
    private String getJsonBodyCreateClient(long tenantId) {
        
        return new JSONObject().put("client_id", getClientName()).put(
                "client_secret",
                _config.getClientSecret()).put("scope", "read,write,trust").put(
                        "authorized_grant_types",
                        "client_credentials").put("authorities", "ROLE_APP").put(
                                "tenantId",
                                tenantId).toString();
    }
}
