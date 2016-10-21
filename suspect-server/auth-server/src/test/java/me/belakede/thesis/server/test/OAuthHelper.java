package me.belakede.thesis.server.test;

import me.belakede.thesis.server.auth.domain.User;
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

@Component
public class OAuthHelper {

    @Autowired
    AuthorizationServerTokenServices tokenservice;

    @Value("${oauth2.client.id}")
    private String clientId;

    @Value("${oauth2.client.secret}")
    private String secret;

    public RequestPostProcessor addBearerToken(User user) {
        return mockRequest -> {
            // Create OAuth2 token
            OAuth2Request oauth2Request = new OAuth2Request(null, clientId, null, true, null, null, null, null, null);
            Authentication userauth = new TestingAuthenticationToken(user.getUsername(), null, user.getRoles().stream().map(Enum::name).toArray(String[]::new));
            OAuth2Authentication oauth2auth = new OAuth2Authentication(oauth2Request, userauth);
            OAuth2AccessToken token = tokenservice.createAccessToken(oauth2auth);

            // Set Authorization header to use Bearer
            mockRequest.addHeader("Authorization", "Bearer " + token.getValue());
            return mockRequest;
        };
    }

}
