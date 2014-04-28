/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.UserSocialActivity;
import com.venefica.model.SocialActivityType;

/**
 *
 * @author gyuszi
 */
public interface UserSocialActivityDao {
    
    public Long save(UserSocialActivity socialActivity);
    
    public UserSocialActivity getBySocialPointAndActivityType(Long userSocialPointId, SocialActivityType activityType);
}
