package com.adm.gaia.webhook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GaiaConfiguration {
    
    @Value("${gaia.sts.url}")
    private String _gaiaSTSUrl;
    @Value("${gaia.whs.url}")
    private String _gaiaWHSUrl;
    @Value("${tenant.admin.user.name.prefix}")
    private String _tenantAdminUserNamePrefix;
    @Value("${client.name}")
    private String _clientName;
    @Value("${client.secret}")
    private String _clientSecret;
    
    public String getGaiaSTSUrl() {
        
        return _gaiaSTSUrl;
    }

    public String getGaiaWHSUrl() {

        return _gaiaWHSUrl;
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
