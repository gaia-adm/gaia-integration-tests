package com.adm.gaia.webhook;

import org.springframework.beans.factory.annotation.Autowired;

public class TestWebhookGenerator {
    
    @Autowired
    private GaiaTokenBuilder _gaiaTokenBuilder;
    @Autowired
    private WebhookGenerator _webhookGenerator;
    
    // @Test
    public void testGenerate() {
        
        String token = _gaiaTokenBuilder.build();
        _webhookGenerator.generate(token, null, null);
    }
}