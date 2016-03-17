package com.adm.gaia.webhook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GaiaConfiguration {
    
    @Value("${gaia.url}")
    private String _gaiaUrl;
    @Value("${tenant.admin.user.name}")
    private String _tenantAdminUserName;
    
    public String getGaiaUrl() {
        
        return _gaiaUrl;
    }
    
    public String getTenantAdminUserName() {
        
        return _tenantAdminUserName;
    }
}
