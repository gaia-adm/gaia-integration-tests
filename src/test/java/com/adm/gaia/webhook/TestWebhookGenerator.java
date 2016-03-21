package com.adm.gaia.webhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TestWebhookGenerator extends GaiaTestCase {
    
    @Autowired
    private WebhookGenerator _webhookGenerator;
    
    //@Test
    public void testGenerate() throws Exception {

        _token = _gaiaTokenBuilder.build();
        String hookUrl = _webhookGenerator.generate(_token, "github", "push");
        Assert.assertNotNull(hookUrl, "Null hookUrl");
        Assert.assertFalse(hookUrl.isEmpty(), "Empty hookUrl");
        String payload = new String(Files.readAllBytes(
                Paths.get(getClass().getClassLoader().getResource("event_payload").toURI())));
        _webhookGenerator.publish(_token, hookUrl, payload);
    }
}