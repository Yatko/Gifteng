/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import com.venefica.service.dto.AdDto;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;

/**
 *
 * @author gyuszi
 */
@Entity
@ForeignKey(name = "memberaddata_fk")
@Table(name = MemberAdData.TABLE_NAME)
public class MemberAdData extends AdData {
    
    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    public static final String TABLE_NAME = "memberaddata";
    
    private boolean requestLimitIncreased;
    
    public MemberAdData() {
        super(AdType.MEMBER);
    }
    
    @Override
    public void updateAd(AdDto adDto) {
    }
    
    @Override
    public void updateAdDto(AdDto adDto) {
    }
    
    /**
     * Returns the ad value by using a very simple calculation.
     * The actual math algorithm is 10 % of the ad price.
     * 
     * @return 
     */
    @Override
    public BigDecimal getValue() {
        BigDecimal price = getPrice();
        if ( price == null ) {
            price = BigDecimal.ZERO;
        }
        
        BigDecimal percent = BigDecimal.TEN;
        BigDecimal HUNDRED = new BigDecimal(100);
        BigDecimal value = price.multiply(percent).divide(HUNDRED);
        return value;
    }
    
    // getters/setters

    public boolean isRequestLimitIncreased() {
        return requestLimitIncreased;
    }

    public void setRequestLimitIncreased(boolean requestLimitIncreased) {
        this.requestLimitIncreased = requestLimitIncreased;
    }
}
