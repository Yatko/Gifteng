package com.venefica.connect;

import com.venefica.config.Constants;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Allows to connect the local user account with accounts of well-known social
 * networks. This controller is based on
 * {@link org.springframework.social.connect.web.ConnectController}.
 *
 * @author Sviatoslav Grebenchukov
 */
@Controller
@RequestMapping("/connect")
public class ConnectController {
    
    private static final String ERROR_CODE = "error";
    private static final String ERROR_REASON_PROVIDER = "provider";
    private static final String ERROR_REASON_REJECT = "reject";
    private static final String MULTIPLE_CONNECTIONS_CODE = "multiple";
    
    //Sets URL of application's connect error page.
    private final static String CONNECT_ERROR_URL = "/connect/error";
    private final static String CONNECT_OK_URL = "/connect/ok";
    
    private static final Log logger = LogFactory.getLog(ConnectController.class);
    
    @Inject
    private ConnectionFactoryLocator connectionFactoryLocator;
    
    @Inject
    private ConnectionRepository connectionRepository;
    
    @Inject
    private ConnectSupport connectSupport;
    
    /**
     * Process a connect request by commencing the process of establishing a
     * connection to the provider on behalf of the member.
     * 
     * @param providerId
     * @param request
     * @param response
     * @return 
     */
    @RequestMapping(value = "/{providerId}", method = RequestMethod.GET)
    public RedirectView connect(
            @PathVariable("providerId") String providerId,
            NativeWebRequest request,
            HttpServletResponse response) {
        try {
            ConnectionFactory connectionFactory = connectionFactoryLocator.getConnectionFactory(providerId);
            String encryptedToken = request.getHeader(Constants.AUTH_TOKEN);
            if ( encryptedToken == null || encryptedToken.isEmpty() ) {
                //Try to get from the params
                encryptedToken = request.getParameter(Constants.AUTH_TOKEN);
            }
            
            if (encryptedToken != null) {
                logger.debug("Encrypted token: " + encryptedToken);
                logger.debug("Encrypted token length: " + encryptedToken.length());
            }

            response.addCookie(new Cookie(Constants.AUTH_TOKEN, encryptedToken));
            return new RedirectView(connectSupport.buildOAuthUrl(connectionFactory, request));
        } catch (Exception e) {
            logger.error("", e);
            return redirectToConnectError(ERROR_REASON_PROVIDER);
        }
    }

    /**
     * Process the authorization callback from an OAuth 1 service provider.
     */
    @RequestMapping(value = "/{providerId}/" + ConnectSupport.CALLBACK_PATH, method = RequestMethod.GET, params = "oauth_token")
    public RedirectView oauth1Callback(
            @PathVariable("providerId") String providerId,
            NativeWebRequest request) {
        try {
            OAuth1ConnectionFactory connectionFactory = (OAuth1ConnectionFactory) connectionFactoryLocator.getConnectionFactory(providerId);
            Connection connection = connectSupport.completeConnection(connectionFactory, request);
            addConnection(connection, connectionFactory, request);
            return redirectToConnectOk();
        } catch (MultipleConnectionsException e) {
            logger.warn("", e);
            return redirectToConnectError(MULTIPLE_CONNECTIONS_CODE);
        } catch (Exception e) {
            logger.warn("Exception while handling OAuth1 callback (" + e.getMessage() + "). Redirecting to " + CONNECT_ERROR_URL, e);
            return redirectToConnectError(ERROR_REASON_PROVIDER);
        }
    }

    /**
     * Process the authorization callback from an OAuth 2 service provider.
     */
    @RequestMapping(value = "/{providerId}/" + ConnectSupport.CALLBACK_PATH, method = RequestMethod.GET, params = "code")
    public RedirectView oauth2Callback(
            @PathVariable("providerId") String providerId,
            NativeWebRequest request) {
        try {
            OAuth2ConnectionFactory connectionFactory = (OAuth2ConnectionFactory) connectionFactoryLocator.getConnectionFactory(providerId);
            Connection connection = connectSupport.completeConnection(connectionFactory, request);
            addConnection(connection, connectionFactory, request);
            return redirectToConnectOk();
        } catch (MultipleConnectionsException e) {
            logger.warn("", e);
            return redirectToConnectError(MULTIPLE_CONNECTIONS_CODE);
        } catch (Exception e) {
            logger.warn("Exception while handling OAut2 callback (" + e.getMessage() + "). Redirecting to " + CONNECT_ERROR_URL, e);
            return redirectToConnectError(ERROR_REASON_PROVIDER);
        }
    }

    @RequestMapping(value = "/{providerId}/" + ConnectSupport.CALLBACK_PATH, method = RequestMethod.GET)
    public RedirectView cancelConnectCallback(
            @PathVariable("providerId") String providerId,
            NativeWebRequest request) {
        return redirectToConnectError(ERROR_REASON_REJECT);
    }
    
    /**
     * Handles connect errors
     */
    @RequestMapping("/error")
    public void error() {
    }

    /**
     * Handles successful connects
     */
    @RequestMapping("/ok")
    public void ok() {
    }

    // internal helpers
    
    private RedirectView redirectToConnectOk() {
        return new RedirectView(CONNECT_OK_URL, true);
    }

    private RedirectView redirectToConnectError(String errorCode) {
        return new RedirectView(URIBuilder.fromUri(CONNECT_ERROR_URL).queryParam(ERROR_CODE, errorCode).build().toString(), true);
    }

    private void addConnection(Connection connection, ConnectionFactory connectionFactory, WebRequest request) throws MultipleConnectionsException {
        try {
            String providerId = connectionFactory.getProviderId();
            List<Connection<?>> existingConnections = connectionRepository.findConnections(providerId);

            if (existingConnections.isEmpty()) {
                connectionRepository.addConnection(connection);
            } else {
                throw new MultipleConnectionsException();
            }
        } catch (DuplicateConnectionException e) {
            logger.warn("Duplicated connection!", e);
        }
    }
}
