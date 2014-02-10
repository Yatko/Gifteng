package com.venefica.connect;

import com.venefica.auth.TokenUtil;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    
    private static final String OK_CODE = "ok";
    private static final String ERROR_CODE = "error";
    
    //Sets URL of application's connect error page.
    private final static String CONNECT_ERROR_URL = "/connect/" + ERROR_CODE;
    private final static String CONNECT_OK_URL = "/connect/" + OK_CODE;
    
    private static final Log logger = LogFactory.getLog(ConnectController.class);
    
    @Inject
    private ConnectionFactoryLocator connectionFactoryLocator;
    @Inject
    private ConnectionRepository connectionRepository;
    @Inject
    private ConnectSupport connectSupport;
    @Inject
    private TokenUtil tokenUtil;
    
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
//            logger.error(getClass().getSimpleName() + " - connect()");
//            connectSupport.dumpRequest(request);
            
            String encryptedToken = tokenUtil.getEncryptedToken(request.getNativeRequest(HttpServletRequest.class));
            
//            // this line probably has no any effect
//            response.addCookie(new Cookie(Constants.AUTH_TOKEN, encryptedToken));
            
            int referenceNumber = connectSupport.addReference(encryptedToken);
            ConnectionFactory connectionFactory = connectionFactoryLocator.getConnectionFactory(providerId);
            
            return new RedirectView(connectSupport.buildOAuthUrl(connectionFactory, request, referenceNumber));
        } catch (Exception e) {
            logger.error("", e);
            return redirectToConnectError(ConnectSupport.ERROR_REASON_PROVIDER);
        }
    }

    /**
     * Process the authorization callback from an OAuth 1 service provider.
     */
    @RequestMapping(value = "/{providerId}/" + ConnectSupport.CALLBACK_PATH, method = RequestMethod.GET, params = ConnectSupport.PARAM_TOKEN)
    public RedirectView oauth1Callback(
            @PathVariable("providerId") String providerId,
            NativeWebRequest request) {
//        logger.error(getClass().getSimpleName() + " - oauth1Callback()");
//        connectSupport.dumpRequest(request);
        
        Integer referenceNumber = getReferenceNumber(request);
        try {
            String encryptedToken = connectSupport.getReference(referenceNumber);
            tokenUtil.decryptAndStoreToken(encryptedToken);
            
            OAuth1ConnectionFactory connectionFactory = (OAuth1ConnectionFactory) connectionFactoryLocator.getConnectionFactory(providerId);
            Connection connection = connectSupport.completeConnection(connectionFactory, request);
            addConnection(connection, connectionFactory, request);
            return redirectToConnectOk();
        } catch (MultipleConnectionsException e) {
            logger.warn("", e);
            return redirectToConnectError(ConnectSupport.ERROR_REASON_MULTIPLE);
        } catch (Exception e) {
            logger.warn("Exception while handling OAuth1 callback (" + e.getMessage() + "). Redirecting to " + CONNECT_ERROR_URL, e);
            return redirectToConnectError(ConnectSupport.ERROR_REASON_PROVIDER);
        } finally {
            connectSupport.removeReference(referenceNumber);
        }
    }

    /**
     * Process the authorization callback from an OAuth 2 service provider.
     */
    @RequestMapping(value = "/{providerId}/" + ConnectSupport.CALLBACK_PATH, method = RequestMethod.GET, params = ConnectSupport.PARAM_CODE)
    public RedirectView oauth2Callback(
            @PathVariable("providerId") String providerId,
            NativeWebRequest request) {
//        logger.error(getClass().getSimpleName() + " - oauth2Callback()");
//        connectSupport.dumpRequest(request);
        
        Integer referenceNumber = getReferenceNumber(request);
        try {
            String encryptedToken = connectSupport.getReference(referenceNumber);
            tokenUtil.decryptAndStoreToken(encryptedToken);
            
            OAuth2ConnectionFactory connectionFactory = (OAuth2ConnectionFactory) connectionFactoryLocator.getConnectionFactory(providerId);
            Connection connection = connectSupport.completeConnection(connectionFactory, request, referenceNumber);
            addConnection(connection, connectionFactory, request);
            return redirectToConnectOk();
        } catch (MultipleConnectionsException e) {
            logger.warn("", e);
            return redirectToConnectError(ConnectSupport.ERROR_REASON_MULTIPLE);
        } catch (Exception e) {
            logger.warn("Exception while handling OAut2 callback (" + e.getMessage() + "). Redirecting to " + CONNECT_ERROR_URL, e);
            return redirectToConnectError(ConnectSupport.ERROR_REASON_PROVIDER);
        } finally {
            connectSupport.removeReference(referenceNumber);
        }
    }

    @RequestMapping(value = "/{providerId}/" + ConnectSupport.CALLBACK_PATH, method = RequestMethod.GET)
    public RedirectView cancelConnectCallback(
            @PathVariable("providerId") String providerId,
            NativeWebRequest request) {
//        logger.error(getClass().getSimpleName() + " - cancelConnectCallback()");
//        connectSupport.dumpRequest(request);
        return redirectToConnectError(ConnectSupport.ERROR_REASON_REJECT);
    }
    
    /**
     * Handles connect errors
     */
    @RequestMapping(value = "/" + ERROR_CODE, method = RequestMethod.GET, params = "reason")
    public void error(@RequestParam("reason") String reason) {
        logger.warn("Error with reason: " + reason);
    }

    /**
     * Handles successful connects
     */
    @RequestMapping("/" + OK_CODE)
    public void ok() {
    }

    // internal helpers
    
    private RedirectView redirectToConnectOk() {
        return new RedirectView(CONNECT_OK_URL, true);
    }

    private RedirectView redirectToConnectError(String reason) {
        return new RedirectView(URIBuilder.fromUri(CONNECT_ERROR_URL).queryParam("reason", reason).build().toString(), true);
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
    
    private Integer getReferenceNumber(NativeWebRequest request) {
        HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
        String reference = nativeRequest.getParameter(ConnectSupport.PARAM_REFERENCE);
        Integer referenceNumber = Integer.valueOf(reference);
        return referenceNumber;
    }
}
