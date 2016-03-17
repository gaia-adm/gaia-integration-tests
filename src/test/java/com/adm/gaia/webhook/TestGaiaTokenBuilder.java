package com.adm.gaia.webhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestGaiaTokenBuilder extends GaiaTestCase {

    @Autowired
    private GaiaTokenBuilder _gaiaTokenBuilder;

    @Test
    public void testBuild() {
        
        Assert.assertEquals(_gaiaTokenBuilder.build(), "");
    }
}