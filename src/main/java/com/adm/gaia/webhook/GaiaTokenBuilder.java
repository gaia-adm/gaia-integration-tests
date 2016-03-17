package com.adm.gaia.webhook;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.adm.gaia.webhook.rest.RestClient;
import com.adm.gaia.webhook.rest.RestConstants;

public class GaiaTokenBuilder {
    
    @Autowired
    GaiaConfiguration _config;
    
    public String build() {
        
        createTenant();
        
        return "";
    }
    
    private void createTenant() {
        
        RestClient.post(
                _config.getGaiaUrl() + RestConstants.CREATE_TENANT_SUFFIX,
                getJsonBodyCreateTenant(),
                RestConstants.APPLICATION_JSON);
    }
    
    private String getJsonBodyCreateTenant() {
        
        return new JSONObject().put("adminUserName", _config.getTenantAdminUserName()).toString();
    }
}
