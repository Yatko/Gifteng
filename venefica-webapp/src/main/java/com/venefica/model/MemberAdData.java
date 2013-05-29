/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import com.venefica.service.dto.AdDto;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;

/**
 *
 * @author gyuszi
 */
@Entity
@ForeignKey(name = "memberaddata_fk")
@Table(name = "memberaddata")
public class MemberAdData extends AdData {
    
    public MemberAdData() {
        super(AdType.MEMBER);
    }
    
    @Override
    public void updateAd(AdDto adDto) {
    }
    
    @Override
    public void updateAdDto(AdDto adDto) {
    }
    
    // getters/setters
}
