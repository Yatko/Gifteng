/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

import com.venefica.model.Request;
import com.venefica.model.RequestStatus;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author gyuszi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestDto extends DtoBase {
    
    // out
    private Long id;
    // out
    private UserDto user;
    // out
    private Date requestedAt;
    // out
    private RequestStatus status;

    public RequestDto() {
    }
    
    public RequestDto(Request request) {
        this.id = request.getId();
        this.requestedAt = request.getRequestedAt();
        this.status = request.getStatus();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Date getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Date requestedAt) {
        this.requestedAt = requestedAt;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
    
}
