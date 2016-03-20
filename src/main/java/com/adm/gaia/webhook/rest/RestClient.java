package com.adm.gaia.webhook.rest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestClient {

    private static final Logger _logger = LoggerFactory.getLogger(RestClient.class);
    private static OkHttpClient _client;

    static {

        _client =
                new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(
                        10,
                        TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
    }

    public static Response post(String url, String body, MediaType mediaType) {

        Response ret;
        try {
            String log =
                    String.format(
                            "HTTP POST, URL: %s, Body: %s, Media Type: %s",
                            url,
                            body,
                            mediaType.toString());
            _logger.debug(log);
            Request request =
                    new Request.Builder().url(url).post(RequestBody.create(mediaType, body)).build();
            ret = execute(log, request);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return ret;
    }

    public static Response post(String url) {

        return post(url, "", RestConstants.APPLICATION_JSON);
    }

    public static Response get(String url) {

        Response ret;
        try {
            String log = String.format("HTTP GET, URL: %s", url);
            _logger.debug(log);
            Request request = new Request.Builder().url(url).get().build();
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
