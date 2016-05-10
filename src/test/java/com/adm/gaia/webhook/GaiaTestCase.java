package com.adm.gaia.webhook;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(locations = { "classpath:test-gaia-application-context.xml" })
public class GaiaTestCase extends AbstractTestNGSpringContextTests {}
