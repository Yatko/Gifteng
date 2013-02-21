package com.venefica.auth;

import javax.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * Removes security context from the holder at the end of the request.
 *
 * @author Sviatoslav Grebenchukov
 */
public class SecurityContextCleaner extends AbstractPhaseInterceptor<Message> {

    private final Log log = LogFactory.getLog(SecurityContextCleaner.class);
    
    @Inject
    private ThreadSecurityContextHolder securityContextHolder;

    public SecurityContextCleaner() {
        super(Phase.POST_INVOKE);
    }

    @Override
    public void handleMessage(Message msg) throws Fault {
        securityContextHolder.clearContext();
        log.debug("Security context cleared");
    }

    @Override
    public void handleFault(Message message) {
        securityContextHolder.clearContext();
        log.debug("Security context cleared after a fault");
    }
}
