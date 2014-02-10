package com.venefica.connect;

import java.util.TreeMap;
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
 * {@link SignInController} and
 * {@link ConnectController}.
 *
 * @author Sviatoslav Grebenchukov
 */
public class ConnectSupport {

    private static final Log logger = LogFactory.getLog(ConnectSupport.class);
    
    public static final String CALLBACK_PATH = "callback";
    
    final static String OAUTH_TOKEN_ATTRIBUTE = "oauthToken";
    final static String PARAM_VERIFIER = "oauth_verifier";
    final static String PARAM_TOKEN = "oauth_token";
    final static String PARAM_CODE = "code";
    final static String PARAM_SCOPE = "scope";
    final static String PARAM_DISPLAY = "display";
    final static String PARAM_REFERENCE = "ref";
    
    static final String ERROR_REASON_PROVIDER = "provider";
    static final String ERROR_REASON_REJECT = "reject";
    static final String ERROR_REASON_MULTIPLE = "multiple";
    
    private final static TreeMap<Integer, String> reference = new TreeMap<Integer, String>();
    
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
    public String buildOAuthUrl(ConnectionFactory connectionFactory, NativeWebRequest request, Integer referenceNumber) {
        if (connectionFactory instanceof OAuth1ConnectionFactory) {
            return buildOAuth1Url((OAuth1ConnectionFactory) connectionFactory, request, null, referenceNumber);
        } else if (connectionFactory instanceof OAuth2ConnectionFactory) {
            return buildOAuth2Url((OAuth2ConnectionFactory) connectionFactory, request, null, referenceNumber);
        } else {
            throw new IllegalArgumentException("ConnectionFactory not supported");
        }
    }

    /**
     * Complete the connection to the OAuth1 provider.
     *
     * @param connectionFactory the service provider's connection factory e.g.
     * TwitterConnectionFactory
     * @param request the current web request
     * @return a new connection to the service provider
     */
    public Connection completeConnection(OAuth1ConnectionFactory connectionFactory, NativeWebRequest request) {
        String verifier = request.getParameter(PARAM_VERIFIER);
        AuthorizedRequestToken requestToken = new AuthorizedRequestToken(extractCachedRequestToken(request), verifier);
        OAuthToken accessToken = connectionFactory.getOAuthOperations().exchangeForAccessToken(requestToken, null);
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
    public Connection completeConnection(OAuth2ConnectionFactory connectionFactory, NativeWebRequest request, Integer referenceNumber) {
        try {
            String code = request.getParameter(PARAM_CODE);
            String callbackUrl = callbackUrl(request, referenceNumber);
            OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
            AccessGrant accessGrant = oauthOperations.exchangeForAccess(code, callbackUrl, null);
            return connectionFactory.createConnection(accessGrant);
        } catch (HttpClientErrorException e) {
            logger.warn("HttpClientErrorException while completing connection: " + e.getMessage());
            logger.warn("      Response body: " + e.getResponseBodyAsString());
            throw e;
        }
    }
    
    public Integer addReference(String encryptedToken) {
        synchronized ( reference ) {
            int referenceNumber = reference.isEmpty() ? 1 : reference.lastKey() + 1;
            reference.put(referenceNumber, encryptedToken);
            return referenceNumber;
        }
    }
    
    public void removeReference(Integer referenceNumber) {
        synchronized ( reference ) {
            reference.remove(referenceNumber);
        }
    }
    
    public String getReference(Integer referenceNumber) {
        synchronized ( reference ) {
            return reference.get(referenceNumber);
        }
    }
    
//    public void dumpRequest(NativeWebRequest request) {
//        HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
//        String requestUrl = nativeRequest.getRequestURL().toString();
//        requestUrl += "?" + nativeRequest.getQueryString();
//        logger.debug("RequestUrl: " + requestUrl);
//        
//        if ( nativeRequest.getCookies() != null ) {
//            String requestCookie = "";
//            for ( javax.servlet.http.Cookie cookie : nativeRequest.getCookies() ) {
//                requestCookie += (requestCookie.isEmpty() ? "" : ", ");
//                requestCookie += cookie.getName() + "=" + cookie.getValue();
//            }
//            logger.debug("RequestCookie: " + requestCookie);
//        }
//    }

    // internal helpers
    
    private String callbackUrl(NativeWebRequest request, Integer referenceNumber) {
        HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
        String requestUrl = nativeRequest.getRequestURL().toString();
        if ( !requestUrl.endsWith(CALLBACK_PATH) ) {
            requestUrl = requestUrl + "/" + CALLBACK_PATH;
        }
        
        if ( referenceNumber != null ) {
            requestUrl += "?" + PARAM_REFERENCE + "=" + referenceNumber;
        }

        return requestUrl;
    }

    private OAuthToken extractCachedRequestToken(WebRequest request) {
        String attribute = OAUTH_TOKEN_ATTRIBUTE;
        int scope = RequestAttributes.SCOPE_SESSION;
        OAuthToken requestToken = (OAuthToken) request.getAttribute(attribute, scope);
        request.removeAttribute(attribute, scope);
        return requestToken;
    }
    
    /***********/
    /* OAuth 1 */
    /***********/
    
    private String buildOAuth1Url(OAuth1ConnectionFactory connectionFactory, NativeWebRequest request, MultiValueMap<String, String> additionalParameters, Integer referenceNumber) {
        OAuth1Operations oauthOperations = connectionFactory.getOAuthOperations();
        OAuth1Parameters parameters = getOAuth1Parameters(connectionFactory, request, additionalParameters, referenceNumber);
        OAuthToken requestToken = fetchRequestToken(request, oauthOperations, referenceNumber);
        request.setAttribute(OAUTH_TOKEN_ATTRIBUTE, requestToken, RequestAttributes.SCOPE_SESSION);
        return oauthOperations.buildAuthorizeUrl(requestToken.getValue(), parameters);
    }

    private OAuthToken fetchRequestToken(NativeWebRequest request, OAuth1Operations oauthOperations, Integer referenceNumber) {
        String callbackUrl = null;
        if (oauthOperations.getVersion() == OAuth1Version.CORE_10_REVISION_A) {
            callbackUrl = callbackUrl(request, referenceNumber);
        }
        return oauthOperations.fetchRequestToken(callbackUrl, null);
    }

    private OAuth1Parameters getOAuth1Parameters(OAuth1ConnectionFactory connectionFactory, NativeWebRequest request, MultiValueMap<String, String> additionalParameters, Integer referenceNumber) {
        OAuth1Operations oauthOperations = connectionFactory.getOAuthOperations();
        OAuth1Parameters parameters = new OAuth1Parameters(additionalParameters);
        if (oauthOperations.getVersion() == OAuth1Version.CORE_10) {
            String callbackUrl = callbackUrl(request, referenceNumber);
            parameters.setCallbackUrl(callbackUrl);
        }
        return parameters;
    }
    
    /***********/
    /* OAuth 2 */
    /***********/
    
    private String buildOAuth2Url(OAuth2ConnectionFactory connectionFactory, NativeWebRequest request, MultiValueMap<String, String> additionalParameters, Integer referenceNumber) {
        OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
        OAuth2Parameters parameters = getOAuth2Parameters(request, additionalParameters, referenceNumber);
        return oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, parameters);
    }

    private OAuth2Parameters getOAuth2Parameters(NativeWebRequest request, MultiValueMap<String, String> additionalParameters, Integer referenceNumber) {
        String callbackUrl = callbackUrl(request, referenceNumber);
        OAuth2Parameters parameters = new OAuth2Parameters(additionalParameters);
        parameters.setRedirectUri(callbackUrl);

        String scope = request.getParameter(PARAM_SCOPE);
        if (scope != null) {
            parameters.setScope(scope);
        }

        String display = request.getParameter(PARAM_DISPLAY);
        if (display != null) {
            parameters.set(PARAM_DISPLAY, display);
        }

        return parameters;
    }
}
