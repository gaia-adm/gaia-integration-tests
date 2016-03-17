package com.adm.gaia.webhook;

import okhttp3.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.adm.gaia.webhook.rest.RestClient;
import com.adm.gaia.webhook.rest.RestConstants;
import org.springframework.stereotype.Component;

@Component
public class GaiaTokenBuilder {
    
    @Autowired
    private GaiaConfiguration _config;
    private static final Logger _logger = LoggerFactory.getLogger(GaiaTokenBuilder.class);
    
    public String build() {
        
        createTenant();
        
        return "";
    }
    
    private void createTenant() {

        Response response = RestClient.post(
                _config.getGaiaUrl() + RestConstants.CREATE_TENANT_SUFFIX,
                getJsonBodyCreateTenant(),
                RestConstants.APPLICATION_JSON);
        _logger.debug(response.toString());
    }
    
    private String getJsonBodyCreateTenant() {
        
        return new JSONObject().put("adminUserName", _config.getTenantAdminUserName()).toString();
    }
}
