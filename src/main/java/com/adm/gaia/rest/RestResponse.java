package com.adm.gaia.rest;

/**
 * Created by kornfeld on 14/04/2016.
 */
public class RestResponse {

    private String _responseMessage;
    private String _responseBody;

    public RestResponse(String responseMessage, String responseBody) {

        _responseMessage = responseMessage;
        _responseBody = responseBody;
    }

    public String getResponseMessage() {

        return _responseMessage;
    }

    public String getResponseBody() {

        return _responseBody;
    }
}
