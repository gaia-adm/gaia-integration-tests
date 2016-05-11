package com.adm.gaia.util;

import com.adm.gaia.Constants;
import com.adm.gaia.elasticsearch.ElasticSearchUtil;
import com.adm.gaia.rest.RestClient;
import com.adm.gaia.rest.RestRequest;
import com.adm.gaia.rest.RestResponse;
import com.adm.gaia.webhook.GaiaConfiguration;
import com.adm.gaia.webhook.GaiaUrlContainer;
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

        long tenantId = _tenant.getId();
        deleteESIndex(tenantId);
        deleteClient();
        deleteTenant(tenantId);
        String token = _etcd.getToken();
        if (token != null && !token.isEmpty()) {
            deleteWebhook();
            revokeToken(token);
        }
    }

    private void deleteESIndex(long tenantId) {

        String index = ElasticSearchUtil.buildIndex(tenantId);
        String url = _urlContainer.getGaiaESUrl() + index;
        RestClient.delete(new RestRequest(url));
        _logger.debug(String.format("ElasticSearch index %s deleted (%s)", index, url));
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
            String clientName = _config.getClientName();
            url = String.format("%s%s/%s",
                    _urlContainer.getGaiaUrl(),
                    Constants.CREATE_CLIENT_SUFFIX,
                    clientName);
            RestResponse
                    response =
                    RestClient.delete(new RestRequest(url));
            _logger.debug(String.format("Client name: %s deleted. %s",
                    clientName,
                    response.getResponseMessage()));
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to delete client, URL: %s", url), ex);
        }
    }

    private void deleteTenant(long tenantId) {

        String url = null;
        try {
            if (tenantId > 0) {
                url = String.format("%s%s/%d",
                        _urlContainer.getGaiaUrl(),
                        Constants.CREATE_TENANT_SUFFIX,
                        tenantId);
                RestResponse
                        response =
                        RestClient.delete(new RestRequest(url));
                _logger.debug(String.format(
                        "Tenant ID: %d deleted. %s",
                        tenantId,
                        response.getResponseMessage()));
            }
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to delete tenant, URL: %s", url), ex);
        }
    }

    public void revokeToken(String token) {

        String url = null;
        try {
            url =
                    _urlContainer.getGaiaUrl() + String.format(
                            Constants.REVOKE_TOKEN_SUFFIX_FORMAT,
                            token);
            RestResponse response = RestClient.delete(new RestRequest(url));
            _logger.debug(response.getResponseMessage());
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to delete token, URL: %s", url), ex);
        }
        _logger.debug("Deleted Token: " + token);
    }
}
