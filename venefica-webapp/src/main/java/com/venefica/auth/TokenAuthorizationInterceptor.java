package com.venefica.auth;

import com.venefica.config.Constants;
import com.venefica.service.fault.AuthenticationException;
//import java.io.IOException;
//import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
//import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.Fault;
//import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
//import org.apache.cxf.transport.Conduit;
//import org.apache.cxf.ws.addressing.EndpointReferenceType;

/**
 * Custom token authorization intercepter. Used for authentication in JAX-WS web
 * services.
 * 
 * Read also:
 * http://cxf.apache.org/docs/ws-security.html#WS-Security-UsernameTokenAuthentication
 *
 * @author Sviatoslav Grebenchukov
 */
public class TokenAuthorizationInterceptor extends SoapHeaderInterceptor {

    private final static Log log = LogFactory.getLog(TokenAuthorizationInterceptor.class);
    
    @Inject
    private TokenUtil tokenUtil;

    /**
     * Authenticates incoming messages or sends back error responses.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void handleMessage(Message msg) throws Fault {
        String operation = msg.getExchange().getBindingOperationInfo().getName().getLocalPart();

        // Skip "register" and "authenticate" operations
        if ( Constants.OPERATIONS_FOR_SKIP_TOKEN_AUTHORIZATION.contains(operation) ) {
            return;
        }
        
        try {
            Map<String, ?> headers = (Map<String, ?>) msg.get(Message.PROTOCOL_HEADERS);
            if (headers == null || !headers.containsKey(Constants.AUTH_TOKEN)) {
                throw new AuthenticationException("Unauthorized request arrived (no token provided)!");
            }

            List<String> authTokenHeader = (List<String>) headers.get(Constants.AUTH_TOKEN);
            if (authTokenHeader.isEmpty()) {
                throw new AuthenticationException("Empty auth token is provided!");
            }
            
            tokenUtil.decryptAndStoreToken(authTokenHeader.get(0));
        } catch (Exception e) {
            // TODO: include fault details!!!
            throw new Fault(e);
        }
    }

//    private void sendErrorResponse(Message msg, int responseCode) {
//        Message outMsg = getOutMessage(msg);
//        outMsg.put(Message.RESPONSE_CODE, responseCode);
//
//        msg.getInterceptorChain().abort();
//
//        try {
//            // Send out message back
//            getConduit(msg).prepare(outMsg);
//            close(outMsg);
//        } catch (IOException e) {
//            log.warn(e.getMessage(), e);
//        }
//    }

//    private Message getOutMessage(Message msg) {
//        Exchange exchange = msg.getExchange();
//        Message outMsg = exchange.getOutFaultMessage();
//
//        if (outMsg == null) {
//            Endpoint endpoint = exchange.get(Endpoint.class);
//            outMsg = endpoint.getBinding().createMessage();
//            exchange.setOutMessage(outMsg);
//        }
//
//        outMsg.putAll(msg);
//        return outMsg;
//    }

//    private Conduit getConduit(Message msg) throws IOException {
//        Exchange exchange = msg.getExchange();
//        EndpointReferenceType target = exchange.get(EndpointReferenceType.class);
//        Conduit conduit = exchange.getDestination().getBackChannel(msg, null, target);
//        exchange.setConduit(conduit);
//        return conduit;
//    }

//    private void close(Message outMsg) throws IOException {
//        OutputStream os = outMsg.getContent(OutputStream.class);
//        os.flush();
//        os.close();
//    }
}
