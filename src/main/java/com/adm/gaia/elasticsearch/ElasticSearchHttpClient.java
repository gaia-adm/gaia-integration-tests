package com.adm.gaia.elasticsearch;

import com.adm.gaia.rest.RestClient;
import com.adm.gaia.rest.RestRequest;
import com.adm.gaia.rest.RestResponse;
import com.adm.gaia.util.RetriableOperationExecutor;
import com.adm.gaia.webhook.GaiaUrlContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

@Component
public class ElasticSearchHttpClient {

    private static final Logger _logger = LoggerFactory.getLogger(ElasticSearchHttpClient.class);

    @Autowired
    private GaiaUrlContainer _urlContainer;

    public String search(String index, String query) {

        String ret;
        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode ES search query: " + query, e);
        }
        String url = String.format("%s%s/_search?q=%s", _urlContainer.getGaiaESUrl(), index, query);
        try {
            RestResponse response = RetriableOperationExecutor.execute(
                    Void -> RestClient.get(new RestRequest(url)),
                    exception -> exception.getMessage().contains("index_not_found_exception"),
                    5000,
                    3);
            ret = response.getResponseBody();
            _logger.debug(String.format("Search URL: %s, Results: %s", url, ret));
        } catch (Exception e) {
            throw new RuntimeException("Failed to search for created webhook index in ES, url: "
                                       + url, e);
        }

        return ret;
    }
}
