/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import javax.inject.Inject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A quick and dirty solution to test mail send.
 * 
 * @author gyuszi
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DumpErrorTestExecutionListener.class})
@ActiveProfiles("test")
@ContextConfiguration(locations = "/common-context.xml")
public class EmailSenderTest {
    
    @Inject
    private EmailSender emailSender;
    
    @Test
    @Ignore
    public void sendHtmlEmailTest() throws MailException {
        emailSender.sendHtmlEmail("Mail subject", "HTML message", "PLAIN message", "venefica.labs@yatko.com", null);
    }
}
