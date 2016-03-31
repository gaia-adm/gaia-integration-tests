package com.adm.gaia.webhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.StringUtils;
import org.testng.annotations.AfterMethod;

@ContextConfiguration(locations = { "classpath:test-gaia-application-context.xml" })
public class GaiaTestCase extends AbstractTestNGSpringContextTests {

    @Autowired
    protected GaiaTokenBuilder _gaiaTokenBuilder;
    protected String _token = "";

    @AfterMethod
    protected void cleanup() {

        if (!StringUtils.isEmpty(_token)) {
            _gaiaTokenBuilder.revokeToken(_token);
            _gaiaTokenBuilder.clean();
            _token = "";
        }
    }
}
