package com.adm.gaia.webhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestGaiaTokenBuilder extends GaiaTestCase {
    
    @Autowired
    private GaiaTokenBuilder _gaiaTokenBuilder;
    
    @Test
    public void testBuildGaiaAccessToken() {
        
        String token = _gaiaTokenBuilder.build();
        Assert.assertNotNull(token, "Null token");
        Assert.assertFalse(token.isEmpty(), "Empty token");
    }
}