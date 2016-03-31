package com.adm.gaia.elasticsearch;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kornfeld on 31/03/2016.
 */
public class SearchQuery {

    protected static final Logger _logger = LoggerFactory.getLogger(SearchQuery.class);
    protected ElasticSearchManagement _elasticSearchManagement;
    protected String _index;

    public SearchQuery(ElasticSearchManagement elasticSearchManagement, String index) {

        _elasticSearchManagement = elasticSearchManagement;
        _index = index;
    }

    public SearchResponse search(String query) {

        SearchRequestBuilder searchRequestBuilder =
                getSearchRequestBuilder();
        QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(query);
        queryStringQueryBuilder.useDisMax(true);

        searchRequestBuilder.setQuery(queryStringQueryBuilder);

        _logger.debug("ElasticSearch request: " + searchRequestBuilder);
        SearchResponse response = searchRequestBuilder.execute().actionGet();
        log(response);

        return response;
    }

    private SearchRequestBuilder getSearchRequestBuilder() {

        SearchRequestBuilder searchRequestBuilder = _elasticSearchManagement.getClient().
                prepareSearch(_index.toLowerCase());

        return searchRequestBuilder;
    }

    private void log(SearchResponse response) {

        if (_logger.isDebugEnabled()) {
            SearchHit[] results = response.getHits().getHits();
            _logger.debug("\r\n------------------------------");
            _logger.debug("Results: " + results.length);
            for (SearchHit hit : results) {
                _logger.debug("\t" + hit.getId() + ": " + hit.getScore());
            }
        }
    }
}
