/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * The default behavior for testing with Spring is to use the
 * DependencyInjectionTestExecutionListener, however this class has a very ugly
 * problem: if an exception occurs during the bean initialization, it will tell
 * you that the exception occurred, but will tell no: what was the exception and
 * where. To overcome the abovementioned problem, this implementation prints the
 * stacktrace.
 *
 * @author gyuszi
 */
public class DumpErrorTestExecutionListener extends DependencyInjectionTestExecutionListener {

    @Override
    @SuppressWarnings("CallToThreadDumpStack")
    public void prepareTestInstance(TestContext testContext) throws Exception {
        System.out.println("######");
        System.out.println("PREPARING TEST CASE: " + testContext.getTestClass().getSimpleName());
        System.out.println("######");
        
        try {
            logException(testContext);
            super.prepareTestInstance(testContext);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Throwable th) {
            th.printStackTrace();
            throw new Exception("Wrapped throwable exception", th);
        }
    }

    @Override
    @SuppressWarnings("CallToThreadDumpStack")
    public void beforeTestMethod(TestContext testContext) throws Exception {
        System.out.println("------");
        System.out.println("Preparing test method: " + testContext.getTestClass().getSimpleName() + "/" + testContext.getTestMethod().getName());
        for (String attr : testContext.attributeNames()) {
            System.out.println("Attributes: " + attr);
        }
        System.out.println("------");

        try {
            logException(testContext);
            super.beforeTestMethod(testContext);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Throwable th) {
            th.printStackTrace();
            throw new Exception("Wrapped throwable exception", th);
        }
    }

    @Override
    @SuppressWarnings("CallToThreadDumpStack")
    public void afterTestMethod(TestContext testContext) throws Exception {
        try {
            logException(testContext);
            super.afterTestMethod(testContext);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Throwable th) {
            th.printStackTrace();
            throw new Exception("Wrapped throwable exception", th);
        }
    }
    
    @SuppressWarnings("CallToThreadDumpStack")
    private void logException(TestContext testContext) {
        if (testContext != null && testContext.getTestException() != null) {
            testContext.getTestException().printStackTrace();
        }
    }
}
