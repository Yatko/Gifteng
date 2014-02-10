/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

import com.venefica.model.NotificationType;
import com.venefica.model.User;
import com.venefica.model.UserSetting;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author gyuszi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserSettingDto extends DtoBase {
    
    // in, out
    private Set<NotificationType> notifiableTypes;
    
    public UserSettingDto() {
    }

    public UserSettingDto(User user, UserSetting userSetting) {
        this.notifiableTypes = userSetting != null ? userSetting.getNotifiableTypes() : null;
    }
    
    // getters/setters
    
    public Set<NotificationType> getNotifiableTypes() {
        return notifiableTypes;
    }

    public void setNotifiableTypes(Set<NotificationType> notifiableTypes) {
        this.notifiableTypes = notifiableTypes;
    }
    
}
