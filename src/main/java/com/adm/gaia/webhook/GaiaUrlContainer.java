package com.adm.gaia.webhook;

import okhttp3.HttpUrl;

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
            _gaiaUrl = buildGaiaUrl();
        }
        
        return _gaiaUrl;
    }
    
    public String getGaiaESUrl() {
        
        if (StringUtils.isEmpty(_gaiaUrl)) {
            _gaiaESUrl = buildGaiaESUrl();
        }
        
        return _gaiaESUrl;
    }

    public String getGaiaEtcdUrl() {

        return _config.getGaiaEtcdUrl();
    }
    
    private String buildGaiaUrl() {
        
        return new HttpUrl.Builder().scheme(_config.getGaiaScheme()).host(
                _config.getGaiaHost()).port(_config.getGaiaPort()).build().toString();
    }
    
    private String buildGaiaESUrl() {
        
        return new HttpUrl.Builder().scheme(_config.getGaiaESScheme()).host(
                _config.getGaiaESHost()).port(_config.getGaiaESPort()).build().toString();
    }
}
