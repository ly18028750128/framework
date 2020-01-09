package org.cloud.utils.http;

import org.junit.Test;

import static org.junit.Assert.*;

public class OKHttpClientBuilderTest {

    @Test
    public void testBuildOKHttpClient() throws Exception{
        OKHttpClientBuilder.single().buildOKHttpClient("PKCS12","certs/szhtcloud.p12","1554920771");
    }
}