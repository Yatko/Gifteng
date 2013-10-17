package com.venefica.model;

import com.venefica.common.RandomGenerator;
import com.venefica.config.Constants;
import com.vividsolutions.jts.geom.Point;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * Local user model.
 *
 * @author Sviatoslav Grebenchukov
 */
@Entity
@Table(name = "local_user")
@org.hibernate.annotations.Table(appliesTo = "local_user", indexes = {
    @Index(name = "idx_email_pass", columnNames = {"email", "password"}),
    @Index(name = "idx_name_pass", columnNames = {"name", "password"}),
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, updatable = false)
    @Index(name = "idx_name")
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    @Index(name = "idx_email")
    private String email;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date joinedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginAt;
    
    @OneToMany(mappedBy = "creator")
    private Set<Ad> ads;
    @OneToMany(mappedBy = "user")
    private Set<Request> requests;
    
    @OneToOne
    @ForeignKey(name = "userdata_fk")
    private UserData userData;
    
    @OneToOne
    @ForeignKey(name = "userpoint_fk")
    private UserPoint userPoint;
    
    //see: http://stackoverflow.com/questions/8830279/hibernate-onetomany-relationship-mapping
    //see: http://stackoverflow.com/questions/13708271/self-referencing-manytomany-with-hibernate-and-annotations
    
    @ManyToMany
    @JoinTable(name = "followers", joinColumns = @JoinColumn(name = "following_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ForeignKey(name = "local_user_following_fk")
    private Set<User> followers;
    
    @ManyToMany
    @JoinTable(name = "followers", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "following_id"))
    @ForeignKey(name = "local_user_follower_fk")
    private Set<User> followings;
    
    @OneToMany(mappedBy = "from")
    @OrderBy
    private Set<Message> sentMessages;
    
    @OneToMany(mappedBy = "to")
    @OrderBy
    private Set<Message> receivedMessages;
    
    @Column(name = "adminn", nullable = false)
    @Index(name = "idx_admin")
    private boolean admin;
    @Column(nullable = false)
    private boolean verified;
    
    @ManyToOne
    @ForeignKey(name = "local_user_avatar_fk")
    private Image avatar;
    
    @Index(name = "idx_deleted")
    private boolean deleted;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;
    
    public User() {
        password = RandomGenerator.generateNumeric(Constants.USER_DEFAULT_PASSWORD_LENGTH);
        followers = new HashSet<User>(0);
        followings = new HashSet<User>(0);
        sentMessages = new LinkedHashSet<Message>();
        receivedMessages = new LinkedHashSet<Message>();
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
    
    public String getFullName() {
        return userData != null ? userData.getFullName() : null;
    }
    
    public String getPhoneNumber() {
        return userData != null ? userData.getPhoneNumber() : null;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.userData.setPhoneNumber(phoneNumber);
    }
    
    public String getAbout() {
        return userData != null ? userData.getAbout(): null;
    }
    
    public void setAbout(String about) {
        this.userData.setAbout(about);
    }
    
    public Address getAddress() {
        return userData != null ? userData.getAddress() : null;
    }
    
    public void setAddress(Address address) {
        this.userData.setAddress(address);
    }
    
    public Point getLocation() {
        return userData != null ? userData.getLocation(): null;
    }
    
    public void setLocation(Point location) {
        this.userData.setLocation(location);
    }
    
    public boolean isBusinessAccount() {
        return userData.isBusinessAccount();
    }
    
    public void addFollower(User user) {
        initFollowers();
        followers.add(user);
    }
    
    public void removeFollower(User user) {
        initFollowers();
        followers.remove(user);
    }
    
    public boolean inFollowers(User user) {
        initFollowers();
        return followers.contains(user);
    }
    
    public void addFollowing(User user) {
        initFollowings();
        followings.add(user);
    }
    
    public void removeFollowing(User user) {
        initFollowings();
        followings.remove(user);
    }
    
    public boolean inFollowings(User user) {
        initFollowings();
        return followings.contains(user);
    }
    
    public void addAd(Ad ad) {
        if ( ads == null ) {
            ads = new HashSet<Ad>(0);
        }
        ads.add(ad);
    }
    
    public void addRequest(Request request) {
        if ( requests == null ) {
            requests = new HashSet<Request>(0);
        }
        requests.add(request);
    }
    
    private void initFollowers() {
        if ( followers == null ) {
            followers = new HashSet<User>(0);
        }
    }
    
    private void initFollowings() {
        if ( followings == null ) {
            followings = new HashSet<User>(0);
        }
    }
    
    public void markAsDeleted() {
        deleted = true;
        deletedAt = new Date();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof User)) {
            return false;
        }

        User other = (User) obj;

        return id != null && id.equals(other.id);
    }

    // getters/setters
    
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

    public Set<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(Set<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public Set<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(Set<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }
    
    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }
    
    public Set<User> getFollowings() {
        return followings;
    }

    public void setFollowings(Set<User> followings) {
        this.followings = followings;
    }

    public UserPoint getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(UserPoint userPoint) {
        this.userPoint = userPoint;
    }

    public Set<Ad> getAds() {
        return ads;
    }

    public void setAds(Set<Ad> ads) {
        this.ads = ads;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
    
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }
}
