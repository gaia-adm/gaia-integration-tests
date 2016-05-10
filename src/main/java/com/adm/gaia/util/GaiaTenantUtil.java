package com.adm.gaia.util;

import com.adm.gaia.rest.RestClient;
import com.adm.gaia.rest.RestConstants;
import com.adm.gaia.rest.RestRequest;
import com.adm.gaia.rest.RestResponse;
import com.adm.gaia.webhook.GaiaConfiguration;
import com.adm.gaia.webhook.GaiaUrlContainer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GaiaTenantUtil {

    private static final Logger _logger = LoggerFactory.getLogger(GaiaTenantUtil.class);
    @Autowired
    private GaiaConfiguration _config;
    @Autowired
    private GaiaUrlContainer _urlContainer;

    public long create() {

        String url = null, body = null;
        try {
            url = _urlContainer.getGaiaUrl() + RestConstants.CREATE_TENANT_SUFFIX;
            body = getJsonBodyCreateTenant();
            RestResponse
                    response =
                    RestClient.post(new RestRequest(url,
                            body,
                            RestConstants.APPLICATION_JSON,
                            RestConstants.APPLICATION_JSON));
            _logger.debug(response.getResponseMessage());
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to create tenant, URL: %s, body: %s",
                    url,
                    body), ex);
        }
        long ret = getId();
        _logger.debug(String.format("Tenant ID: %d created", ret));

        return ret;
    }

    public long getId() {

        long ret = -1;
        String url = null;
        try {
            url =
                    _urlContainer.getGaiaUrl()
                    + String.format(RestConstants.GET_TENANT_SUFFIX_FORMAT, getTenantAdmin());
            String body =
                    RestClient.get(new RestRequest(
                            url,
                            RestConstants.APPLICATION_JSON)).getResponseBody();
            if(!"null".equals(body)) {
                ret = new JSONObject(body).getLong("tenantId");
            }
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to get tenant id, URL: %s", url), ex);
        }

        return ret;
    }

    private String getJsonBodyCreateTenant() {

        return new JSONObject().put("adminUserName", getTenantAdmin()).toString();
    }

    public String getTenantAdmin() {

        return _config.getTenantAdminUserName();
    }
}
