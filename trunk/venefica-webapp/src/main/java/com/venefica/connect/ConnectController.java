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
    private static final String PROVIDER_ERROR_CODE = "provider";
    private static final String MULTIPLE_CONNECTIONS_CODE = "multiple";
    
    private static final Log logger = LogFactory.getLog(ConnectController.class);
    
    @Inject
    private ConnectionFactoryLocator connectionFactoryLocator;
    
    @Inject
    private ConnectionRepository connectionRepository;
    
    @Inject
    private ConnectSupport connectSupport;
    
    private String connectErrorUrl = "/connect/error";
    
    private String connectOkUrl = "/connect/ok";

    /**
     * Sets URL of application's connect error page.
     *
     * @param connectErrorUrl the connect error URL
     */
    public void setConnectErrorUrl(String connectErrorUrl) {
        this.connectErrorUrl = connectErrorUrl;
    }

    public void setConnectOkUrl(String connectOkUrl) {
        this.connectOkUrl = connectOkUrl;
    }

    /**
     * Process a connect request by commencing the process of establishing a
     * connection to the provider on behalf of the member.
     */
    @RequestMapping(value = "/{providerId}", method = RequestMethod.GET)
    public RedirectView connect(@PathVariable String providerId, NativeWebRequest request,
            HttpServletResponse response) {
        ConnectionFactory<?> connectionFactory = connectionFactoryLocator
                .getConnectionFactory(providerId);
        try {
            String encryptedToken = request.getHeader(Constants.AUTH_TOKEN);

            if (encryptedToken != null) {
                logger.debug("Encrypted token: " + encryptedToken);
                logger.debug("Encrypted token length: " + encryptedToken.length());
            }

            response.addCookie(new Cookie(Constants.AUTH_TOKEN, encryptedToken));
            return new RedirectView(connectSupport.buildOAuthUrl(connectionFactory, request));
        } catch (Exception e) {
            return redirectToConnectError(PROVIDER_ERROR_CODE);
        }
    }

    /**
     * Process the authorization callback from an OAuth 1 service provider.
     */
    @RequestMapping(value = "/{providerId}/" + Constants.CALLBACK_PATH, method = RequestMethod.GET, params = "oauth_token")
    public RedirectView oauth1Callback(@PathVariable String providerId, NativeWebRequest request) {
        try {
            OAuth1ConnectionFactory<?> connectionFactory = (OAuth1ConnectionFactory<?>) connectionFactoryLocator
                    .getConnectionFactory(providerId);
            Connection<?> connection = connectSupport
                    .completeConnection(connectionFactory, request);
            addConnection(connection, connectionFactory, request);
            return redirectToConnectOk();
        } catch (MultipleConnectionsException e) {
            return redirectToConnectError(MULTIPLE_CONNECTIONS_CODE);
        } catch (Exception e) {
            logger.warn("Exception while handling OAuth1 callback (" + e.getMessage()
                    + "). Redirecting to " + connectErrorUrl + ".");
            return redirectToConnectError(PROVIDER_ERROR_CODE);
        }
    }

    /**
     * Process the authorization callback from an OAuth 2 service provider.
     */
    @RequestMapping(value = "/{providerId}/" + Constants.CALLBACK_PATH, method = RequestMethod.GET, params = "code")
    public RedirectView oauth2Callback(@PathVariable String providerId, NativeWebRequest request) {
        try {
            OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) connectionFactoryLocator
                    .getConnectionFactory(providerId);
            Connection<?> connection = connectSupport
                    .completeConnection(connectionFactory, request);
            addConnection(connection, connectionFactory, request);
            return redirectToConnectOk();
        } catch (MultipleConnectionsException e) {
            return redirectToConnectError(MULTIPLE_CONNECTIONS_CODE);
        } catch (Exception e) {
            logger.warn("Exception while handling OAut2 callback (" + e.getMessage()
                    + "). Redirecting to " + connectErrorUrl, e);
            return redirectToConnectError(PROVIDER_ERROR_CODE);
        }
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
        return new RedirectView(connectOkUrl, true);
    }

    private RedirectView redirectToConnectError(String errorCode) {
        return new RedirectView(URIBuilder.fromUri(connectErrorUrl)
                .queryParam(ERROR_CODE, "provider").build().toString(), true);
    }

    private void addConnection(Connection<?> connection, ConnectionFactory<?> connectionFactory,
            WebRequest request) throws MultipleConnectionsException {
        try {
            String providerId = connectionFactory.getProviderId();
            List<Connection<?>> existingConnections = connectionRepository
                    .findConnections(providerId);

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
