package me.belakede.test.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

/**
 * https://amsterdam.luminis.eu/2015/11/12/integration-testing-a-spring-restful-web-service-secured-with-oauth2/
 */
@Component
public class OAuth2Helper {

    @Autowired
    AuthorizationServerTokenServices tokenservice;

    @Value("${oauth2.client.id}")
    private String clientId;

    @Value("${oauth2.client.secret}")
    private String secret;

    public RequestPostProcessor addBearerToken(String username, String... authorities) {
        return mockRequest -> {
            mockRequest.addHeader("Authorization", "Bearer " + createOAuth2AccessToken(username, authorities).getValue());
            return mockRequest;
        };
    }

    public OAuth2AccessToken createOAuth2AccessToken(String username, String... authorities) {
        OAuth2Request oauth2Request = new OAuth2Request(null, clientId, null, true, null, null, null, null, null);
        Authentication userauth = new TestingAuthenticationToken(username, null, authorities);
        OAuth2Authentication oauth2auth = new OAuth2Authentication(oauth2Request, userauth);
        return tokenservice.createAccessToken(oauth2auth);
    }

}
