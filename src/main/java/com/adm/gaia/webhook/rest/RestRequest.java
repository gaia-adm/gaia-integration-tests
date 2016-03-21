package com.adm.gaia.webhook.rest;

import okhttp3.MediaType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kornfeld on 21/03/2016.
 */
public class RestRequest {

    private String _uri;
    private Object _entity;
    private MediaType _requestMediaType;
    private MediaType _responseMediaType;
    private Map<String, String> _headers = new HashMap<String, String>();

    public RestRequest(String uri, String responseMediaType) {

        this(uri, "", responseMediaType, responseMediaType);
    }

    public RestRequest(String uri, Object entity, String mediaType, String responseMediaType) {

        _uri = uri;
        _entity = entity;
        _requestMediaType = MediaType.parse(mediaType);
        _responseMediaType = MediaType.parse(responseMediaType);
    }

    public String getUri() {

        return _uri;
    }

    public Object getEntity() {

        return _entity;
    }

    public MediaType getRequestMediaType() {

        return _requestMediaType;
    }

    public MediaType getResponseMediaType() {

        return _responseMediaType;
    }

    public Map<String, String> getHeaders() {

        return _headers;
    }

    public RestRequest header(String name, String value) {

        _headers.put(name, value);

        return this;
    }

    @Override
    public String toString() {

        return String.format(
                "URI: %s, RequestMediaType: %s, ResponseMediaType: %s, Headers: %s",
                _uri,
                _requestMediaType,
                _responseMediaType,
                _headers.toString());
    }
}
