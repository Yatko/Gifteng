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
    
    @Column(unique = true)
    private String name;
    @Column(nullable = false)
    private String password;
    
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String phoneNumber;
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    private String country;
    private String city;
    private String area;
    private String zipCode;
    @Column(nullable = false)
    private boolean businessAcc;
    
    private boolean emailsAllowed;
    private boolean smsAllowed;
    private boolean callsAllowed;
    
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
        businessAcc = false;
        password = generatePassword();
        sentMessages = new LinkedList<Message>();
        receivedMessages = new LinkedList<Message>();
    }

    public User(String name, String firstName, String lastName, String email) {
        this();
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public boolean isComplete() {
        return name != null && password != null && firstName != null && lastName != null
                && email != null && phoneNumber != null && dateOfBirth != null && country != null
                && city != null;
        // TODO: place other checks here!!
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof User)) {
            return false;
        }

        User other = (User) obj;

        return id != null && id.equals(other.id);
    }

    private String generatePassword() {
        return "12345"; // TODO: Generate difficult password!!!
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    public boolean areEmailsAllowed() {
        return emailsAllowed;
    }

    public void setEmailsAllowed(boolean emailsAllowed) {
        this.emailsAllowed = emailsAllowed;
    }

    public boolean areSmsAllowed() {
        return smsAllowed;
    }

    public void setSmsAllowed(boolean smsAllowed) {
        this.smsAllowed = smsAllowed;
    }

    public boolean areCallsAllowed() {
        return callsAllowed;
    }

    public void setCallsAllowed(boolean callsAllowed) {
        this.callsAllowed = callsAllowed;
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
}
