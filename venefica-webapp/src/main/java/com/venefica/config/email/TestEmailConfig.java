/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.config.email;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author gyuszi
 */
@Configuration
@Profile(value = "test")
public class TestEmailConfig extends AbstractEmailConfig {
}
