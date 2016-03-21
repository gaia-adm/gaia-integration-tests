package com.adm.gaia.webhook.rest;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RestClient {

    private static final Logger _logger = LoggerFactory.getLogger(RestClient.class);
    private static OkHttpClient _client;

    static {

        _client =
                new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(
                        10,
                        TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
    }

    public static Response post(RestRequest restRequest) {

        Response ret;
        try {
            String log =
                    String.format(
                            "HTTP POST, URL: %s, Body: %s, Media Type: %s",
                            restRequest.getUri(),
                            restRequest.getEntity(),
                            restRequest.getRequestMediaType());
            _logger.debug(log);
            Request request =
                    new Request.Builder().
                            url(restRequest.getUri()).
                            headers(Headers.of(restRequest.getHeaders())).
                            post(RequestBody.create(restRequest.getRequestMediaType(),
                                    restRequest.getEntity().toString())).
                            build();
            ret = execute(log, request);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return ret;
    }

    public static Response delete(RestRequest restRequest) {

        Response ret;
        try {
            String log = String.format("HTTP DELETE, URL: %s", restRequest.getUri());
            _logger.debug(log);
            Request request = new Request.Builder().
                    url(restRequest.getUri()).
                    headers(Headers.of(restRequest.getHeaders())).
                    delete().build();
            ret = execute(log, request);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return ret;
    }

    public static Response get(RestRequest restRequest) {

        Response ret;
        try {
            String log = String.format("HTTP GET, URL: %s", restRequest.getUri());
            _logger.debug(log);
            Request request = new Request.Builder().
                    url(restRequest.getUri()).
                    headers(Headers.of(restRequest.getHeaders())).
                    get().build();
            ret = execute(log, request);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return ret;
    }

    private static Response execute(String log, Request request) throws IOException {

        Response ret;
        ret = _client.newCall(request).execute();
        if (!ret.isSuccessful()) {
            String error = String.format("Unexpected code: %d, %s, %s", ret.code(),
                    ret.body().string(), log);
            _logger.error(error);
            throw new RuntimeException(error);
        }
        return ret;
    }
}
