package com.adm.gaia;

public final class Constants {

    public static final String DATA_SOURCE = "gaia-itest";
    public static final String EVENT_TYPE = "push";

    public static final String CREATE_TENANT_SUFFIX = "sts/tenant";
    public static final String GET_TENANT_SUFFIX_FORMAT = CREATE_TENANT_SUFFIX + "?user=%s";
    public static final String CREATE_CLIENT_SUFFIX = "sts/oauth/client";
    public static final String
            CREATE_TOKEN_SUFFIX_FORMAT =
            "sts/oauth/token?grant_type=client_credentials&client_id=%s&client_secret=%s";
    public static final String REVOKE_TOKEN_SUFFIX_FORMAT = "sts/oauth/token/revoke?token=%s";
    public static final String GENERATE_WEBHOOK = "wh/config";
    public static final String APPLICATION_JSON = "application/json";
}
