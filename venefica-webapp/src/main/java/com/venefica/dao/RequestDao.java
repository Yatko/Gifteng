/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Request;
import java.util.List;

/**
 *
 * @author gyuszi
 */
public interface RequestDao {
    
    /**
     * Stores the request in the database.
     *
     * @param request request to store
     * @return id of the stored request
     */
    Long save(Request request);
    
    /**
     * Returns the request with the given ID.
     *
     * @param requestId id of the request
     * @return request object or null if not found.
     */
    Request get(Long requestId);
    
    /**
     * Returns the request by the user and ad. Only one request for an ad
     * by the same user.
     *
     * @param userId id of the user who requested
     * @param adId id of the ad
     * @return request object or null if not found.
     */
    Request get(Long userId, Long adId);
    
    /**
     * Returns the available requests for the given ad.
     * 
     * @param adId
     * @return list of requests
     */
    List<Request> getByAd(Long adId);
    
    /**
     * Returns the available requests for the given user.
     * 
     * @param userId
     * @return list of requests
     */
    List<Request> getByUser(Long userId);
    
    /**
     * Returns a list of requests that were made for ads created by the
     * given user.
     * 
     * @param userId
     * @return list of requests
     */
    List<Request> getForUser(Long userId);
    
    /**
     * Mark the request as hidden
     * 
     * @param requestId  
     */
    void hide(Long requestId);
}
