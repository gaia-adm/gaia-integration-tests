package com.adm.gaia;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GaiaConfiguration {

    @Value("${gaia.etcd.url}")
    private String _gaiaEtcdUrl;
    @Value("${gaia.es.url}")
    private String _gaiaESUrl;
    @Value("${gaia.net.protocol}")
    private String _gaiaNetProtocol; // http or https
    @Value("${gaia.host}")
    private String _gaiaHost;
    @Value("${gaia.port}")
    private String _gaiaPort;
    @Value("${tenant.admin.user.name}")
    private String _tenantAdminUserName;
    @Value("${client.name}")
    private String _clientName;
    @Value("${client.secret}")
    private String _clientSecret;
    @Value("${sleep.time.before.es.check}")
    private long _sleepTimeBeforeEsCheck;

    public String getGaiaEtcdUrl() {

        return _gaiaEtcdUrl;
    }

    public String getGaiaESUrl() {

        return _gaiaESUrl;
    }

    public String getGaiaNetProtocol() {

        return _gaiaNetProtocol;
    }

    public String getGaiaHost() {

        return _gaiaHost;
    }

    public String getGaiaPort() {

        return _gaiaPort;
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

    public long getSleepTimeBeforeEsCheck() {

        return _sleepTimeBeforeEsCheck;
    }
}
