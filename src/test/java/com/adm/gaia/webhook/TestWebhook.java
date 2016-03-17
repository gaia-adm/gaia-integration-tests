package com.adm.gaia.webhook;

import org.testng.annotations.Test;

public class TestWebhook {
    
    @Test()
    public void testSampleProviderRegister() {
        
        SampleProvider provider = new SampleProvider();
        provider.register();
    }
}
