package com.adm.gaia;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GaiaConfiguration {

    @Value("${COREOS_PRIVATE_IPV4}")
    private String _gaiaEtcdHost;
    @Value("${gaia.es.url}")
    private String _gaiaESUrl;
    @Value("${gaiaUrl}")
    private String _gaiaUrl;
    @Value("${tenant.admin.user.name}")
    private String _tenantAdminUserName;
    @Value("${client.name}")
    private String _clientName;
    @Value("${client.secret}")
    private String _clientSecret;

    public String getGaiaEtcdHost() {

        return _gaiaEtcdHost;
    }

    public String getGaiaESUrl() {

        return _gaiaESUrl;
    }

    public String getGaiaUrl() {

        return _gaiaUrl;
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
