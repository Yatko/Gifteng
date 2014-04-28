/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.UserSocialActivity;
import com.venefica.model.SocialActivityType;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gyuszi
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class UserSocialActivityDaoImpl extends DaoBase<UserSocialActivity> implements UserSocialActivityDao {
    
    @Override
    public Long save(UserSocialActivity socialActivity) {
        socialActivity.setCreatedAt(new Date());
        return saveEntity(socialActivity);
    }
    
    @Override
    public UserSocialActivity getBySocialPointAndActivityType(Long userSocialPointId, SocialActivityType activityType) {
        List<UserSocialActivity> socialActivities = createQuery(""
                + "from " + UserSocialActivity.class.getSimpleName() + " usa where "
                + "usa.userSocialPoint.id = :userSocialPointId and "
                + "usa.activityType = :activityType"
                + "")
                .setParameter("userSocialPointId", userSocialPointId)
                .setParameter("activityType", activityType)
                .list();
        return socialActivities.isEmpty() ? null : socialActivities.get(0);
    }
}
