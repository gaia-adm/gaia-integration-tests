package com.adm.gaia.webhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TestWebhookGenerator extends GaiaTestCase {
    
    @Autowired
    private GaiaTokenBuilder _gaiaTokenBuilder;
    @Autowired
    private WebhookGenerator _webhookGenerator;
    
    @Test
    public void testGenerate() throws Exception {
        
        String token = _gaiaTokenBuilder.build();
        String hookUrl = _webhookGenerator.generate(token, "github", "push");
        Assert.assertNotNull(hookUrl, "Null hookUrl");
        Assert.assertFalse(hookUrl.isEmpty(), "Empty hookUrl");
        String payload = new String(Files.readAllBytes(
                Paths.get(getClass().getClassLoader().getResource("event_payload").toURI())));
        _webhookGenerator.publish(token, hookUrl, payload);
    }
}