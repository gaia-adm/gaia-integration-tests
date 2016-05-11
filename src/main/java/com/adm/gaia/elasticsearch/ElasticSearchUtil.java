package com.adm.gaia.elasticsearch;

import com.adm.gaia.Constants;

public class ElasticSearchUtil {

    public static String buildIndex(long tenantId) {

        return String.format(
                "gaia.%d.%s.%s",
                tenantId,
                Constants.DATA_SOURCE,
                Constants.EVENT_TYPE);
    }
}
