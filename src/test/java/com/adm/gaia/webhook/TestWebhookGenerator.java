package com.adm.gaia.webhook;

import com.adm.gaia.Constants;
import com.adm.gaia.elasticsearch.ElasticSearchHttpClient;
import com.adm.gaia.elasticsearch.ElasticSearchUtil;
import com.adm.gaia.util.GaiaCleaner;
import com.adm.gaia.util.GaiaTenantUtil;
import com.adm.gaia.util.GaiaTokenBuilder;
import com.adm.gaia.util.GaiaWebhookUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestWebhookGenerator extends GaiaTestCase {

    @Autowired
    private GaiaCleaner _cleaner;
    @Autowired
    private GaiaTokenBuilder _tokenBuilder;
    @Autowired
    private GaiaWebhookUtil _webhook;
    @Autowired
    private ElasticSearchHttpClient _esClient;
    @Autowired
    private GaiaTenantUtil _tenant;

    @BeforeMethod
    public void cleanup() {

        _cleaner.clean();
    }

    @Test
    public void testGenerate() throws Exception {

        long tenantId = publishData();
        validateTenantCreationOnElasticSearch(tenantId);
    }

    private void validateTenantCreationOnElasticSearch(long tenantId) {

        String index = ElasticSearchUtil.buildIndex(tenantId);
        String search = _esClient.search(index, "i_am_here_dexter_your_sister_dd");
        JSONArray hits = new JSONObject(search).getJSONObject("hits").getJSONArray("hits");
        Assert.assertEquals(hits.length(), 1);
        Assert.assertEquals(new JSONObject(hits.get(0).toString()).get("_index"), index);
    }

    private long publishData() throws IOException, URISyntaxException {

        long tenantId = _tenant.create();
        String token = _tokenBuilder.build(tenantId);
        String hookUrl = _webhook.generate(token, Constants.DATA_SOURCE, Constants.EVENT_TYPE);
        Assert.assertNotNull(hookUrl, "Null hookUrl");
        Assert.assertFalse(hookUrl.isEmpty(), "Empty hookUrl");
        String httpHookUrl = hookUrl.replace("https", "http");
        _webhook.publish(token, httpHookUrl, getPayload());

        return tenantId;
    }

    private String getPayload() throws IOException, URISyntaxException {

        return new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(
                "event_payload").toURI())));
    }
}