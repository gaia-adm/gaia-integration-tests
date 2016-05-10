package com.adm.gaia.rest;

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

    public static RestResponse post(RestRequest restRequest) {

        Response response;
        String body;
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
            response = execute(log, request);
            body = response.body().string();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return new RestResponse(response.toString(), body);
    }

    public static RestResponse delete(RestRequest restRequest) {

        Response response;
        String body;
        try {
            String log = String.format("HTTP DELETE, URL: %s", restRequest.getUri());
            _logger.debug(log);
            Request request = new Request.Builder().
                    url(restRequest.getUri()).
                    headers(Headers.of(restRequest.getHeaders())).
                    delete().build();
            response = execute(log, request);
            body = response.body().string();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return new RestResponse(response.toString(), body);
    }

    public static RestResponse get(RestRequest restRequest) {

        Response response;
        String body;
        try {
            String log = String.format("HTTP GET, URL: %s", restRequest.getUri());
            _logger.debug(log);
            Request request = new Request.Builder().
                    url(restRequest.getUri()).
                    headers(Headers.of(restRequest.getHeaders())).
                    get().build();
            response = execute(log, request);
            body = response.body().string();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return new RestResponse(response.toString(), body);
    }

    private static Response execute(String log, Request request) throws IOException {

        Response ret = _client.newCall(request).execute();
        if (!ret.isSuccessful()) {
            String error = String.format("Unexpected code: %d, %s, %s", ret.code(),
                    ret.body().string(), log);
            _logger.error(error);
            throw new RuntimeException(error);
        }

        return ret;
    }
}
