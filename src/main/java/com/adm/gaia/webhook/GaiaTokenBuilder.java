package com.adm.gaia.webhook;

import java.util.UUID;

import okhttp3.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.adm.gaia.webhook.rest.RestClient;
import com.adm.gaia.webhook.rest.RestConstants;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GaiaTokenBuilder {

    private static final Logger _logger = LoggerFactory.getLogger(GaiaTokenBuilder.class);
    @Autowired
    private GaiaConfiguration _config;
    private String _admin;
    private String _clientId;

    public String build() {

        createClient(createTenant());

        return createToken();
    }

    private String createToken() {

        String url = null, ret = null;
        try {
            url = _config.getGaiaUrl() +
                    String.format(RestConstants.CREATE_TOKEN_SUFFIX_FORMAT,
                            getClientId(),
                            _config.getClientSecret());
            Response response = RestClient.post(url);
            ret = response.toString();
            _logger.debug(response.toString());
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to create client, URL: %s",
                    url), ex);
        }

        return ret;
    }

    private void createClient(String tenantId) {

        String url = null, body = null;
        try {
            url = _config.getGaiaUrl() + RestConstants.CREATE_CLIENT_SUFFIX;
            body = getJsonBodyCreateClient(tenantId);
            Response response = RestClient.post(url, body, RestConstants.APPLICATION_JSON);
            _logger.debug(response.toString());
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to create client, URL: %s, body: %s",
                    url,
                    body), ex);
        }
    }

    private String createTenant() {

        String url = null, body = null;
        try {
            url = _config.getGaiaUrl() + RestConstants.CREATE_TENANT_SUFFIX;
            body = getJsonBodyCreateTenant();
            Response response = RestClient.post(url, body, RestConstants.APPLICATION_JSON);
            _logger.debug(response.toString());
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to create tenant, URL: %s, body: %s",
                    url,
                    body), ex);
        }

        return getCreatedTenant();
    }

    private String getCreatedTenant() {

        String url = null;
        try {
            url =
                    _config.getGaiaUrl()
                            + String.format(
                            RestConstants.GET_TENANT_SUFFIX_FORMAT,
                            getTenantAdminUserName());

            return RestClient.get(url).body().string();
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to get tenant id, URL: %s", url), ex);
        }
    }

    private String getJsonBodyCreateTenant() {

        return new JSONObject().put("adminUserName", getTenantAdminUserName()).toString();
    }

    private synchronized String getTenantAdminUserName() {

        if (_admin == null) {
            _admin =
                    String.format(
                            "%s.%s",
                            _config.getTenantAdminUserNamePrefix(),
                            UUID.randomUUID());
        }

        return _admin;
    }

    private synchronized String getClientId() {

        if (_clientId == null) {
            _clientId =
                    String.format(
                            "%s.%s",
                            _config.getClientName(),
                            UUID.randomUUID());
        }

        return _clientId;
    }

    private String getJsonBodyCreateClient(String tenantId) {

        return new JSONObject().
                put("client_id", getClientId()).
                put("client_secret", _config.getClientSecret()).
                put("scope", "read,write,trust").
                put("authorized_grant_types", "client_credentials").
                put("authorities", "ROLE_APP").
                put("tenantId", tenantId).toString();
    }
}
