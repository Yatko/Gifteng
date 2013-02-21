package com.venefica.auth;

import com.venefica.dao.UserDao;
import com.venefica.model.User;
import com.venefica.service.fault.AuthenticationException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.ws.addressing.EndpointReferenceType;

/**
 * Custom token authorization intercepter. Used for authentication in JAX-WS web
 * services.
 *
 * @author Sviatoslav Grebenchukov
 */
public class TokenAuthorizationInterceptor extends SoapHeaderInterceptor {

    public final static String REGISTERUSER_OPERATION = "RegisterUser";
    public final static String AUTHENTICATE_OPERATION = "Authenticate";
    public final static String AUTH_TOKEN = "AuthToken";
    
    protected final Log log = LogFactory.getLog(TokenAuthorizationInterceptor.class);
    
    @Inject
    private ThreadSecurityContextHolder securityContextHolder;
    
    @Inject
    private UserDao userDao;
    
    @Inject
    private TokenEncryptor tokenEncryptor;

    /**
     * Authenticates incoming messages or sends back error responses.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void handleMessage(Message msg) throws Fault {
        String operation = msg.getExchange().getBindingOperationInfo().getName().getLocalPart();

        // Skip "register" operation
        if (operation.equals(REGISTERUSER_OPERATION) || operation.equals(AUTHENTICATE_OPERATION)) {
            return;
        }

        try {
            Map<String, ?> headers = (Map<String, ?>) msg.get(Message.PROTOCOL_HEADERS);

            if (headers == null || !headers.containsKey(AUTH_TOKEN)) {
                throw new AuthenticationException(
                        "Unauthorized request arrived (no token provided)!");
            }

            List<String> authTokenHeader = (List<String>) headers.get(AUTH_TOKEN);
            Token token = decryptToken(authTokenHeader);

            if (token.isExpired()) {
                throw new AuthenticationException("Auth token is expired!");
            }

            Long userId = token.getUserId();
            User user = userDao.get(userId);

            if (user == null) {
                throw new AuthenticationException("Invalid auth token (user not found)!");
            }

            SecurityContext securityContext = new SecurityContext(user);
            securityContextHolder.setContext(securityContext);
        } catch (Exception e) {
            // TODO: include fault details!!!
            throw new Fault(e);
        }
    }

    private Token decryptToken(List<String> authTokenHeader) throws TokenDecryptionException,
            AuthenticationException {
        if (authTokenHeader.isEmpty()) {
            throw new AuthenticationException("Empty auth token is provided!");
        }

        return tokenEncryptor.decrypt(authTokenHeader.get(0));
    }

    protected void sendErrorResponse(Message msg, int responseCode) {
        Message outMsg = getOutMessage(msg);
        outMsg.put(Message.RESPONSE_CODE, responseCode);

        msg.getInterceptorChain().abort();

        try {
            // Send out message back
            getConduit(msg).prepare(outMsg);
            close(outMsg);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
    }

    protected Message getOutMessage(Message msg) {
        Exchange exchange = msg.getExchange();
        Message outMsg = exchange.getOutFaultMessage();

        if (outMsg == null) {
            Endpoint endpoint = exchange.get(Endpoint.class);
            outMsg = endpoint.getBinding().createMessage();
            exchange.setOutMessage(outMsg);
        }

        outMsg.putAll(msg);
        return outMsg;
    }

    private Conduit getConduit(Message msg) throws IOException {
        Exchange exchange = msg.getExchange();
        EndpointReferenceType target = exchange.get(EndpointReferenceType.class);
        Conduit conduit = exchange.getDestination().getBackChannel(msg, null, target);
        exchange.setConduit(conduit);
        return conduit;
    }

    private void close(Message outMsg) throws IOException {
        OutputStream os = outMsg.getContent(OutputStream.class);
        os.flush();
        os.close();
    }
}
