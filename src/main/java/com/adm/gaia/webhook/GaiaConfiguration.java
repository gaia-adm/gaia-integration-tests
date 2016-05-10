package com.adm.gaia.webhook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GaiaConfiguration {

    @Value("${gaia.etcd.url}")
    private String _gaiaEtcdUrl;
    @Value("${gaia.scheme}")
    private String _gaiaScheme;
    @Value("${gaia.es.scheme}")
    private String _gaiaESScheme;
    @Value("${gaia.host}")
    private String _gaiaHost;
    @Value("${gaia.es.host}")
    private String _gaiaESHost;
    
    @Value("${gaia.port}")
    private int _gaiaPort;
    @Value("${gaia.es.port}")
    private int _gaiaESPort;
    @Value("${tenant.admin.user.name}")
    private String _tenantAdminUserName;
    @Value("${client.name}")
    private String _clientName;
    @Value("${client.secret}")
    private String _clientSecret;

    public String get_gaiaEtcdUrl() {

        return _gaiaEtcdUrl;
    }

    public String getGaiaScheme() {
        
        return _gaiaScheme;
    }
    
    public String getGaiaHost() {
        
        return _gaiaHost;
    }
    
    public int getGaiaPort() {
        
        return _gaiaPort;
    }
    
    public String getGaiaESScheme() {
        
        return _gaiaESScheme;
    }
    
    public String getGaiaESHost() {
        
        return _gaiaESHost;
    }
    
    public int getGaiaESPort() {
        
        return _gaiaESPort;
    }
    
    public String getTenantAdminUserName() {
        
        return _tenantAdminUserName;
    }
    
    public String getClientName() {
        
        return _clientName;
    }
    
    public String getClientSecret() {
        
        return _clientSecret;
    }
}
