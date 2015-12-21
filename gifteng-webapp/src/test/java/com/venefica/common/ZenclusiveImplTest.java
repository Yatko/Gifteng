/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import java.util.List;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author gyuszi
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DumpErrorTestExecutionListener.class})
@ActiveProfiles("test")
@ContextConfiguration(locations = {"/common-context.xml"})
public class ZenclusiveImplTest {
    
    @Inject
    private ZenclusiveImpl zenclusiveImpl;
    
    @Test
    public void testGetIncentives() {
        List<ZenclusiveImpl.Incentive> incentives = zenclusiveImpl.getIncentives();
        for ( ZenclusiveImpl.Incentive incentive : incentives ) {
            System.out.println("Incentive:");
            System.out.println("    " + incentive.getName());
            System.out.println("    " + incentive.getValue());
        }
    }
    
    @Test
    public void testSendReward() {
        boolean success = zenclusiveImpl.sendReward("venefica.labs@yatko.com", new ZenclusiveImpl.Incentive("David's Cookies", 10L));
        System.out.println("send reward success: " + success);
    }
}
