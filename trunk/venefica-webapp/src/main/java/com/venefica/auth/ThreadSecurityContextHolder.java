package com.venefica.auth;

/**
 * Stores a security context in the thread local storage.
 *
 * @author Sviatoslav Grebenchukov
 */
public class ThreadSecurityContextHolder {

    ThreadLocal<SecurityContext> context;

    public ThreadSecurityContextHolder() {
        context = new ThreadLocal<SecurityContext>();
    }

    public void setContext(SecurityContext securityContext) {
        context.set(securityContext);
    }

    public SecurityContext getContext() {
        return context.get();
    }

    public void clearContext() {
        context.remove();
    }
}
