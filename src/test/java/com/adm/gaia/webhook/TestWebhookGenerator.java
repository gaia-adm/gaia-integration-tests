package com.adm.gaia.webhook;

import com.adm.gaia.elasticsearch.ElasticSearchManagement;
import com.adm.gaia.elasticsearch.SearchQuery;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TestWebhookGenerator extends GaiaTestCase {

    private static final Logger _logger = LoggerFactory.getLogger(TestWebhookGenerator.class);

    @Autowired
    private WebhookGenerator _webhookGenerator;
    @Autowired
    private ElasticSearchManagement _elasticSearchManagement;
    private String _indexPattern = "gaia*github.push";
    private String _index;
    
    @Test
    public void testGenerate() throws Exception {

        _token = _gaiaTokenBuilder.build();
        String hookUrl = _webhookGenerator.generate(_token, "github", "push");
        Assert.assertNotNull(hookUrl, "Null hookUrl");
        Assert.assertFalse(hookUrl.isEmpty(), "Empty hookUrl");
        String httpHookUrl = hookUrl.replace("https", "http");
        String payload = new String(Files.readAllBytes(
                Paths.get(getClass().getClassLoader().getResource("event_payload").toURI())));
        _webhookGenerator.publish(_token, httpHookUrl, payload);

        SearchResponse response = new SearchQuery(_elasticSearchManagement, _indexPattern).
                search("head_commit.message:Update README.md");
        Assert.assertTrue(response.getHits().totalHits() > 0);
        SearchHit hit = response.getHits().getAt(0);
        String head_commit = hit.getSource().get("head_commit").toString();
        Assert.assertTrue(head_commit.contains("0d1a26e67d8f5eaf1f6ba5c57fc3c7d91ac0fd1c"));
        _index = hit.index();
    }

    @AfterMethod
    private void deleteIndices() {

        _logger.debug("deleting index: " + _index);
        DeleteIndexRequest request = new DeleteIndexRequest(_index);
        boolean acknowledged = _elasticSearchManagement.getClient().admin().indices().delete(request).actionGet().isAcknowledged();
        _logger.debug("acknowledged: " + acknowledged);
    }
}