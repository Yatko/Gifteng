package com.venefica.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.ForeignKey;
//import javax.persistence.SequenceGenerator;

/**
 * Local user model.
 *
 * @author Sviatoslav Grebenchukov
 */
@Entity
//@SequenceGenerator(name = "user_gen", sequenceName = "user_seq", allocationSize = 1)
@Table(name = "local_user")
public class User {

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(unique = true, updatable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date joinedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginAt;
    
    @OneToOne
    @ForeignKey(name = "userdata_fk")
    private UserData userData;
    
    @OneToMany(mappedBy = "from")
    @OrderBy
    private List<Message> sentMessages;
    
    @OneToMany(mappedBy = "to")
    @OrderBy
    private List<Message> receivedMessages;
    
    @ManyToOne
    @ForeignKey(name = "local_user_avatar_fk")
    private Image avatar;

    public User() {
        password = generatePassword();
        sentMessages = new LinkedList<Message>();
        receivedMessages = new LinkedList<Message>();
    }

    public User(String name, String email) {
        this();
        this.name = name;
        this.email = email;
    }

    public boolean isComplete() {
        return name != null && password != null && email != null
                && userData != null && userData.isComplete();
    }
    
    private String generatePassword() {
        return "12345"; // TODO: Generate difficult password!!!
    }
    
    public String getFullName() {
        return userData != null ? userData.getFullName() : null;
    }
    
    public String getPhoneNumber() {
        return userData != null ? userData.getPhoneNumber() : null;
    }
    
    public String getCountry() {
        return userData != null ? userData.getCountry() : null;
    }
    
    public String getCity() {
        return userData != null ? userData.getCity() : null;
    }
    
    public String getArea() {
        return userData != null ? userData.getArea() : null;
    }
    
    public String getZipCode() {
        return userData != null ? userData.getZipCode() : null;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.userData.setPhoneNumber(phoneNumber);
    }
    
    public void setCountry(String country) {
        this.userData.setCountry(country);
    }
    
    public void setCity(String city) {
        this.userData.setCity(city);
    }
    
    public void setArea(String area) {
        this.userData.setArea(area);
    }
    
    public void setZipCode(String zipCode) {
        this.userData.setZipCode(zipCode);
    }
    
    public boolean isBusinessAcc() {
        return userData.isBusinessAccount();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof User)) {
            return false;
        }

        User other = (User) obj;

        return id != null && id.equals(other.id);
    }

    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public Date getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }
    
    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
