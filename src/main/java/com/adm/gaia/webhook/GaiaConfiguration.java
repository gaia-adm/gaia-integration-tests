package com.adm.gaia.webhook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GaiaConfiguration {
    
    @Value("${gaia.url}")
    private String _gaiaUrl;
    @Value("${tenant.admin.user.name.prefix}")
    private String _tenantAdminUserNamePrefix;
    @Value("${client.name}")
    private String _clientName;
    @Value("${client.secret}")
    private String _clientSecret;
    
    public String getGaiaUrl() {
        
        return _gaiaUrl;
    }
    
    public String getTenantAdminUserNamePrefix() {
        
        return _tenantAdminUserNamePrefix;
    }
    
    public String getClientName() {
        
        return _clientName;
    }
    
    public String getClientSecret() {
        
        return _clientSecret;
    }
}
