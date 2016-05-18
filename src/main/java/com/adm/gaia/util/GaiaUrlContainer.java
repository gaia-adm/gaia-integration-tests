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

    public String getGaiaUrl() {

        if (StringUtils.isEmpty(_gaiaUrl)) {
            _gaiaUrl = System.getenv("gaiaUrl");
            if(_gaiaUrl == null) {
                throw new GaiaITestException("Gaia URL environment variable not found (gaiaUrl)");
            }
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

        return _config.getGaiaEtcdUrl();
    }

    private String addSlash(String url) {

        String ret = url;
        if(!url.endsWith("/")) {
            ret += "/";
        }

        return ret;
    }
}
