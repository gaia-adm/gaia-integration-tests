package com.adm.gaia.webhook.rest;

import okhttp3.MediaType;

public final class RestConstants {
    
    public static final String CREATE_TENANT_SUFFIX = "sts/tenant";
    public static final String GET_TENANT_SUFFIX_FORMAT = CREATE_TENANT_SUFFIX + "?user=%s";
    public static final String CREATE_CLIENT_SUFFIX = "sts/oauth/client";
    
    public static final MediaType APPLICATION_JSON = MediaType.parse("application/json");
}
