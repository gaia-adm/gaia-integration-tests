package com.adm.gaia.webhook.rest;

import okhttp3.MediaType;

public final class RestConstants {
    
    public static final String CREATE_TENANT_SUFFIX = "sts/tenant";
    public static final String GET_TENANT_SUFFIX_FORMAT = CREATE_TENANT_SUFFIX + "?user=%s";
    public static final String CREATE_CLIENT_SUFFIX = "sts/oauth/client";
    public static final String CREATE_TOKEN_SUFFIX_FORMAT =
            "sts/oauth/token?grant_type=client_credentials&client_id=%s&client_secret=%s";
    public static final String GENERATE_WEBHOOK = "wh/config";
    public static final String APPLICATION_JSON = "application/json";
}
