/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.config.email;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author gyuszi
 */
@Configuration
@Profile(value = "production")
@ImportResource("classpath:email.xml")
public class ProductionEmailConfig extends AbstractEmailConfig {
}
