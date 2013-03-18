package com.venefica.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
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
    
    @Column(unique = true)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(unique = true)
    private String email;
    
    @Embedded
    private UserData userData;
    
    @Column(nullable = false)
    private boolean businessAcc; //TODO: this should be rewritten into group based account management
    
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
        userData = new UserData();
        businessAcc = false;
        password = generatePassword();
        sentMessages = new LinkedList<Message>();
        receivedMessages = new LinkedList<Message>();
    }

    public User(String name, String firstName, String lastName, String email) {
        this();
        this.name = name;
        this.email = email;
        this.userData.setFirstName(firstName);
        this.userData.setLastName(lastName);
    }

    public boolean isComplete() {
        return name != null && password != null && userData != null
                && userData.getFirstName() != null && userData.getLastName() != null
                && email != null && userData.getPhoneNumber() != null
                && userData.getDateOfBirth() != null && userData.getCountry() != null
                && userData.getCity() != null;
        // TODO: place other checks here!!
    }
    
    private String generatePassword() {
        return "12345"; // TODO: Generate difficult password!!!
    }
    
    private void initUserData() {
        if ( userData == null ) {
            userData = new UserData();
        }
    }

    public String getFullName() {
        return userData != null ? userData.getFullName() : null;
    }
    
    public String getFirstName() {
        return userData != null ? userData.getFirstName() : null;
    }
    
    public String getLastName() {
        return userData != null ? userData.getLastName() : null;
    }
    
    public String getPhoneNumber() {
        return userData != null ? userData.getPhoneNumber() : null;
    }
    
    public Date getDateOfBirth() {
        return userData != null ? userData.getDateOfBirth() : null;
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
    
    public Date getJoinedAt() {
        return userData != null ? userData.getJoinedAt() : null;
    }
    
    public void setFirstName(String firstName) {
        initUserData();
        this.userData.setFirstName(firstName);
    }
    
    public void setLastName(String lastName) {
        initUserData();
        this.userData.setLastName(lastName);
    }
    
    public void setPhoneNumber(String phoneNumber) {
        initUserData();
        this.userData.setPhoneNumber(phoneNumber);
    }
    
    public void setDateOfBirth(Date dateOfBirth) {
        initUserData();
        this.userData.setDateOfBirth(dateOfBirth);
    }
    
    public void setCountry(String country) {
        initUserData();
        this.userData.setCountry(country);
    }
    
    public void setCity(String city) {
        initUserData();
        this.userData.setCity(city);
    }
    
    public void setArea(String area) {
        initUserData();
        this.userData.setArea(area);
    }
    
    public void setZipCode(String zipCode) {
        initUserData();
        this.userData.setZipCode(zipCode);
    }
    
    public void setJoinedAt(Date joinedAt) {
        initUserData();
        this.userData.setJoinedAt(joinedAt);
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

    public boolean isBusinessAcc() {
        return businessAcc;
    }

    public void setBusinessAcc(boolean businessAcc) {
        this.businessAcc = businessAcc;
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
