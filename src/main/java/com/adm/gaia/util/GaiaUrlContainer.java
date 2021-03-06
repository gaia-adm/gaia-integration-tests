package com.adm.gaia.util;

import com.adm.gaia.GaiaConfiguration;
import com.adm.gaia.common.GaiaITestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class GaiaUrlContainer {

    @Autowired
    private GaiaConfiguration _config;
    private String _gaiaUrl;
    private String _gaiaESUrl;
    private String _etcdUrl;

    public String getGaiaUrl() {

        if (StringUtils.isEmpty(_gaiaUrl)) {
            _gaiaUrl = String.format("%s://%s:%s", _config.getGaiaNetProtocol(), _config.getGaiaHost(), _config.getGaiaPort());
            _gaiaUrl = addSlash(_gaiaUrl);
        }

        return _gaiaUrl;
    }

    public String getGaiaESUrl() {

        if (StringUtils.isEmpty(_gaiaESUrl)) {
            _gaiaESUrl = _config.getGaiaESUrl();
            _gaiaESUrl = addSlash(_gaiaESUrl);
        }

        return _gaiaESUrl;
    }

    public String getGaiaEtcdUrl() {

        if (StringUtils.isEmpty(_etcdUrl)) {
            _etcdUrl = _config.getGaiaEtcdUrl();
        }
        return _etcdUrl;
    }

    private String addSlash(String url) {

        String ret = url;
        if(!url.endsWith("/")) {
            ret += "/";
        }

        return ret;
    }
}
