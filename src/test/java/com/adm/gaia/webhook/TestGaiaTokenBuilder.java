package com.adm.gaia.webhook;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestGaiaTokenBuilder extends GaiaTestCase {
    
    @Test
    public void testBuild() {
        
        Assert.assertEquals(new GaiaTokenBuilder().build(), "");
    }
}