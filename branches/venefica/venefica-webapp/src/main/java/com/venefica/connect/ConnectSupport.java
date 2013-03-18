package com.venefica.connect;

import com.venefica.config.Constants;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuth1Version;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * Contains utility methods used by
 *
 * @{link {@link SignInController} and
 * @{link {@link ConnectController}.
 *
 * @author Sviatoslav Grebenchukov
 */
public class ConnectSupport {

    private static final Log logger = LogFactory.getLog(ConnectSupport.class);

    /**
     * Builds the provider URL to redirect the user to for connection
     * authorization.
     *
     * @param connectionFactory the service provider's connection factory e.g.
     * FacebookConnectionFactory
     * @param request the current web request
     * @return the URL to redirect the user to for authorization
     * @throws IllegalArgumentException if the connection factory is not OAuth1
     * based.
     */
    public String buildOAuthUrl(ConnectionFactory<?> connectionFactory, NativeWebRequest request) {
        return buildOAuthUrl(connectionFactory, request, null);
    }

    /**
     * Builds the provider URL to redirect the user to for connection
     * authorization.
     *
     * @param connectionFactory the service provider's connection factory e.g.
     * FacebookConnectionFactory
     * @param request the current web request
     * @param additionalParameters parameters to add to the authorization URL.
     * @return the URL to redirect the user to for authorization
     * @throws IllegalArgumentException if the connection factory is not OAuth1
     * based.
     */
    public String buildOAuthUrl(ConnectionFactory<?> connectionFactory, NativeWebRequest request,
            MultiValueMap<String, String> additionalParameters) {
        if (connectionFactory instanceof OAuth1ConnectionFactory) {
            return buildOAuth1Url((OAuth1ConnectionFactory<?>) connectionFactory, request,
                    additionalParameters);
        } else if (connectionFactory instanceof OAuth2ConnectionFactory) {
            return buildOAuth2Url((OAuth2ConnectionFactory<?>) connectionFactory, request,
                    additionalParameters);
        } else {
            throw new IllegalArgumentException("ConnectionFactory not supported");
        }
    }

    /**
     * Complete the connection to the OAuth1 provider.
     *
     * @param connectionFactory the service provider's connection factory e.g.
     * FacebookConnectionFactory
     * @param request the current web request
     * @return a new connection to the service provider
     */
    public Connection<?> completeConnection(OAuth1ConnectionFactory<?> connectionFactory,
            NativeWebRequest request) {
        String verifier = request.getParameter("oauth_verifier");
        AuthorizedRequestToken requestToken = new AuthorizedRequestToken(
                extractCachedRequestToken(request), verifier);
        OAuthToken accessToken = connectionFactory.getOAuthOperations().exchangeForAccessToken(
                requestToken, null);
        return connectionFactory.createConnection(accessToken);
    }

    /**
     * Complete the connection to the OAuth2 provider.
     *
     * @param connectionFactory the service provider's connection factory e.g.
     * FacebookConnectionFactory
     * @param request the current web request
     * @return a new connection to the service provider
     */
    public Connection<?> completeConnection(OAuth2ConnectionFactory<?> connectionFactory,
            NativeWebRequest request) {
        String code = request.getParameter("code");
        try {
            AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(
                    code, callbackUrl(request), null);
            return connectionFactory.createConnection(accessGrant);
        } catch (HttpClientErrorException e) {
            logger.warn("HttpClientErrorException while completing connection: " + e.getMessage());
            logger.warn("      Response body: " + e.getResponseBodyAsString());
            throw e;
        }
    }

    // internal helpers
    private String buildOAuth1Url(OAuth1ConnectionFactory<?> connectionFactory,
            NativeWebRequest request, MultiValueMap<String, String> additionalParameters) {
        OAuth1Operations oauthOperations = connectionFactory.getOAuthOperations();
        OAuth1Parameters parameters = new OAuth1Parameters(additionalParameters);
        if (oauthOperations.getVersion() == OAuth1Version.CORE_10) {
            parameters.setCallbackUrl(callbackUrl(request));
        }
        OAuthToken requestToken = fetchRequestToken(request, oauthOperations);
        request.setAttribute(Constants.OAUTH_TOKEN_ATTRIBUTE, requestToken, RequestAttributes.SCOPE_SESSION);
        return buildOAuth1Url(oauthOperations, requestToken.getValue(), parameters);
    }

    private OAuthToken fetchRequestToken(NativeWebRequest request, OAuth1Operations oauthOperations) {
        if (oauthOperations.getVersion() == OAuth1Version.CORE_10_REVISION_A) {
            return oauthOperations.fetchRequestToken(callbackUrl(request), null);
        }
        return oauthOperations.fetchRequestToken(null, null);
    }

    private String buildOAuth1Url(OAuth1Operations oauthOperations, String requestToken,
            OAuth1Parameters parameters) {
        return oauthOperations.buildAuthorizeUrl(requestToken, parameters);
    }

    private String buildOAuth2Url(OAuth2ConnectionFactory<?> connectionFactory,
            NativeWebRequest request, MultiValueMap<String, String> additionalParameters) {
        OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
        OAuth2Parameters parameters = getOAuth2Parameters(request, additionalParameters);

        return oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, parameters);
    }

    private OAuth2Parameters getOAuth2Parameters(NativeWebRequest request,
            MultiValueMap<String, String> additionalParameters) {
        OAuth2Parameters parameters = new OAuth2Parameters(additionalParameters);
        parameters.setRedirectUri(callbackUrl(request));
        String scope = request.getParameter("scope");

        if (scope != null) {
            parameters.setScope(scope);
        }

        String display = request.getParameter("display");

        if (display != null) {
            parameters.set("display", display);
        }

        return parameters;
    }

    private String callbackUrl(NativeWebRequest request) {
        HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
        String requestUrl = nativeRequest.getRequestURL().toString();

        return requestUrl.endsWith(Constants.CALLBACK_PATH) ? requestUrl : nativeRequest.getRequestURL()
                .toString() + "/" + Constants.CALLBACK_PATH;
    }

    private OAuthToken extractCachedRequestToken(WebRequest request) {
        OAuthToken requestToken = (OAuthToken) request.getAttribute(Constants.OAUTH_TOKEN_ATTRIBUTE,
                RequestAttributes.SCOPE_SESSION);
        request.removeAttribute(Constants.OAUTH_TOKEN_ATTRIBUTE, RequestAttributes.SCOPE_SESSION);
        return requestToken;
    }
    
}
