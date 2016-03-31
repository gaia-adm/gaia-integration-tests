package com.adm.gaia.webhook;

import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class GaiaConfiguration {

    @Value("${gaia.scheme}")
    private String _gaiaScheme;
    @Value("${gaia.host}")
    private String _gaiaHost;
    @Value("${gaia.port}")
    private int _gaiaPort;
    @Value("${gaia.es.port}")
    private int _gaiaESPort;
    @Value("${tenant.admin.user.name.prefix}")
    private String _tenantAdminUserNamePrefix;
    @Value("${client.name}")
    private String _clientName;
    @Value("${client.secret}")
    private String _clientSecret;
    private String _gaiaUrl;

    public String getGaiaUrl() {

        if (StringUtils.isEmpty(_gaiaUrl)) {
            buildGaiaUrl();
        }

        return _gaiaUrl;
    }

    public String getGaiaHost() {

        return _gaiaHost;
    }

    public int getGaiaESPort() {

        return _gaiaESPort;
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

    private void buildGaiaUrl() {

        _gaiaUrl = new HttpUrl.Builder().
                scheme(_gaiaScheme).
                host(_gaiaHost).
                port(_gaiaPort).
                build().toString();
    }
}
