package com.adm.gaia.webhook;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestGaiaTokenBuilder extends GaiaTestCase {
    
    @Test
    public void testBuildGaiaAccessToken() {

        _token  = _gaiaTokenBuilder.build();
        Assert.assertNotNull(_token, "Null token");
        Assert.assertFalse(_token.isEmpty(), "Empty token");
    }
}