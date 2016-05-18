package com.adm.gaia;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GaiaConfiguration {

    @Value("${gaia.etcd.url}")
    private String _gaiaEtcdUrl;
    @Value("${gaia.es.url}")
    private String _gaiaESUrl;
    @Value("${tenant.admin.user.name}")
    private String _tenantAdminUserName;
    @Value("${client.name}")
    private String _clientName;
    @Value("${client.secret}")
    private String _clientSecret;

    public String getGaiaEtcdUrl() {

        return _gaiaEtcdUrl;
    }

    public String getGaiaESUrl() {

        return _gaiaESUrl;
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
