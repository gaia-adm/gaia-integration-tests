package com.adm.gaia.util;

import com.adm.gaia.webhook.GaiaConfiguration;
import com.adm.gaia.webhook.GaiaUrlContainer;
import com.adm.gaia.webhook.rest.RestClient;
import com.adm.gaia.webhook.rest.RestConstants;
import com.adm.gaia.webhook.rest.RestRequest;
import com.adm.gaia.webhook.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GaiaCleaner {

    private static final Logger _logger = LoggerFactory.getLogger(GaiaCleaner.class);
    @Autowired
    private GaiaConfiguration _config;
    @Autowired
    private GaiaUrlContainer _urlContainer;
    @Autowired
    private GaiaEtcd _etcd;
    @Autowired
    private GaiaTenantUtil _tenant;

    public void clean() {

        deleteClient();
        deleteTenant();
        String token = _etcd.getToken();
        if (token != null && !token.isEmpty()) {
            deleteWebhook();
            revokeToken(token);
        }
        deleteESIndex();
    }

    private void deleteESIndex() {

    }

    /**
     * DELETE: https://webhook.mydomain.gaiahub.io/wh/config/webhook-token
     * Content-Type: application/json
     * Accept: application/json
     * Authorization: Bearer <accesstoken>
     */
    private void deleteWebhook() {

    }

    private void deleteClient() {

        String url = null;
        try {
            url =
                    String.format(
                            "%s%s/%s",
                            _urlContainer.getGaiaUrl(),
                            RestConstants.CREATE_CLIENT_SUFFIX,
                            _config.getClientName());
            RestResponse response =
                    RestClient.delete(new RestRequest(url, RestConstants.APPLICATION_JSON));
            _logger.debug(response.getResponseMessage());
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to delete client, URL: %s", url), ex);
        }
    }

    private void deleteTenant() {

        String url = null;
        try {
            url =
                    String.format(
                            "%s%s/%d",
                            _urlContainer.getGaiaUrl(),
                            RestConstants.CREATE_TENANT_SUFFIX,
                            _tenant.getId());
            RestResponse response =
                    RestClient.delete(new RestRequest(url, RestConstants.APPLICATION_JSON));
            _logger.debug(response.getResponseMessage());
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to delete tenant, URL: %s", url), ex);
        }
    }

    public void revokeToken(String token) {

        String url = null;
        try {
            url =
                    _urlContainer.getGaiaUrl()
                    + String.format(RestConstants.REVOKE_TOKEN_SUFFIX_FORMAT, token);
            RestResponse
                    response =
                    RestClient.delete(new RestRequest(url, RestConstants.APPLICATION_JSON));
            _logger.debug(response.getResponseMessage());
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to delete token, URL: %s", url), ex);
        }
        _logger.debug("Deleted Token: " + token);
    }
}
