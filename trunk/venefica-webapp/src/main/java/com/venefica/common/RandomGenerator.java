/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Utility class to help various random values.
 * @author gyuszi
 */
public class RandomGenerator {
    
    /**
     * Generates a random text using only numeric characters (0-9).
     * 
     * @param length the length of the code
     * @return the generated code
     */
    public static String generateNumeric(int length) {
        return RandomStringUtils.randomNumeric(length);
    }
}
