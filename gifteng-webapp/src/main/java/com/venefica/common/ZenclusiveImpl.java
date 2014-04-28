/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import com.venefica.config.Constants;
import com.venefica.config.ZenclusiveConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author gyuszi
 */
@Named
public class ZenclusiveImpl {
    
    private static final Log logger = LogFactory.getLog(ZenclusiveImpl.class);
    
    private final static String ZENCLUSIVE_ADDRESS = "https://www.zenclusive.com/zencl/web/v1/";
    private final static String ZENCLUSIVE_INCENTIVES_ADDRESS = ZENCLUSIVE_ADDRESS + "incentives";
    private final static String ZENCLUSIVE_REWARDS_ADDRESS = ZENCLUSIVE_ADDRESS + "rewards";
    
    @Inject
    private ZenclusiveConfig zenclusiveConfig;
    
    private HttpClient httpClient;
    private String authorization;
    private List<Incentive> incentives;
    
    @PostConstruct
    public void init() {
        httpClient = new DefaultHttpClient();
        authorization = "Basic " + new String(Base64.encodeBase64((zenclusiveConfig.getAccountId() + ":" + zenclusiveConfig.getPassword()).getBytes()));
        incentives = getIncentives();
    }
    
    protected List<Incentive> getIncentives() {
        if ( incentives != null ) {
            return incentives;
        }
        
        HttpEntity entity = null;
        HttpGet request = new HttpGet(ZENCLUSIVE_INCENTIVES_ADDRESS);
        request.setHeader("Authorization", authorization);
        //logRequest(request);
        
        try {
            HttpResponse response = httpClient.execute(request);
            entity = response.getEntity();
            
            if (entity == null) {
                logger.warn("The returned response is null");
                return null;
            }
            
            //logResponse(entity);
            
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new InputStreamReader(entity.getContent()));
            JSONObject jsonObject = (JSONObject) obj;
            
            boolean success = (Boolean) jsonObject.get("success");
            if ( !success ) {
                logger.warn("The returned result is not marked as success");
                return null;
            }
            
            List<Incentive> result = new ArrayList<Incentive>(0);
            JSONArray incentives = (JSONArray) jsonObject.get("incentives");
            for ( Object incentive : incentives ) {
                JSONObject jsonObject_ = (JSONObject) incentive;
                String name = (String) jsonObject_.get("incentivename");
                long value = (Long) jsonObject_.get("incentivevalue");
                
                result.add(new Incentive(name, value));
            }
            return result;
        } catch (IOException ex) {
            logger.error("Exception thrown when trying to request incentives from zenclusive", ex);
        } catch (ParseException ex) {
            logger.error("Exception thrown when trying to parse response of incentives from zenclusive", ex);
        } finally {
            if (entity != null) {
                try {
                    EntityUtils.consume(entity);
                } catch (IOException ex) {
                    logger.error("Exception when consuming entity", ex);
                }
            }
        }
        return null;
    }
    
    public Incentive getIncentiveByParams(String name, String value) {
        if ( getIncentives() == null ) {
            logger.warn("Cannot find incentive by params (name: " + name + ", value: " + value + ") as the available list is null");
            return null;
        }
        
        for ( Incentive incentive : incentives ) {
            try {
                if ( incentive.getName().equals(name) && incentive.getValue() == Long.parseLong(value) ) {
                    return incentive;
                }
            } catch (Exception ex) {
                logger.error("Exception thrown when comparing incentives (name: " + name + ", value: " + value + ")", ex);
            }
        }
        
        logger.warn("Cannot find incentive by params (name: " + name + ", value: " + value + ") as no one is matching");
        return null;
    }
    
    public boolean sendReward(String email, Incentive incentive) {
        if ( email == null || email.trim().isEmpty() ) {
            logger.warn("Cannot send any reward to an unspecified email");
            return false;
        } else if ( incentive == null ) {
            logger.warn("Cannot send any reward to an unspecified incentive");
            return false;
        }
        
        HttpEntity entity;
        try {
            List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
            parameters.add(new BasicNameValuePair("email", email));
            parameters.add(new BasicNameValuePair("incentivename", incentive.getName()));
            parameters.add(new BasicNameValuePair("incentivevalue", String.valueOf(incentive.getValue())));
            entity = new UrlEncodedFormEntity(parameters, Constants.DEFAULT_CHARSET);
        } catch ( Exception ex ) {
            logger.error("", ex);
            return false;
        }
        
        HttpPost request = new HttpPost(ZENCLUSIVE_REWARDS_ADDRESS);
        request.setHeader("Authorization", authorization);
        request.setEntity(entity);
        //logRequest(request);
        
        try {
            HttpResponse response = httpClient.execute(request);
            entity = response.getEntity();
            
            if (entity == null) {
                logger.warn("The returned response is null");
                return false;
            }
            
            //logResponse(entity);
            
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new InputStreamReader(entity.getContent()));
            JSONObject jsonObject = (JSONObject) obj;
            
            boolean success = (Boolean) jsonObject.get("success");
            logger.warn("The returned result after send reward: " + success);
            return success;
        } catch (IOException ex) {
            logger.error("Exception thrown when trying to send reward from zenclusive", ex);
        } catch (ParseException ex) {
            logger.error("Exception thrown when trying to parse response of sent reward from zenclusive", ex);
        } finally {
            try {
                EntityUtils.consume(entity);
            } catch (IOException ex) {
                logger.error("Exception when consuming entity", ex);
            }
        }
        return false;
    }
    
    private void logRequest(HttpRequestBase request) {
        logger.debug("Executing request: " + request.getRequestLine());
    }
    
    private void logResponse(HttpEntity entity) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(entity.getContent(), Constants.DEFAULT_CHARSET));
            for (String line; (line = reader.readLine()) != null;) {
                logger.debug(line);
            }
        } catch (IOException ex) {
            logger.error("Exception thrown when logging response", ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    logger.error("Exception thrown when closing reader at logging response", ex);
                }
            }
        }
    }
    
    public static class Incentive {
        private final String name;
        private final long value;

        public Incentive(String name, long value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public long getValue() {
            return value;
        }
        
        @Override
        public String toString() {
            return name + " - " + value;
        }
    }
}
