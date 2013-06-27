package com.venefica.service.dto;

import com.venefica.dao.AddressWrapperDao;
import com.venefica.dao.ImageDao;
import com.venefica.model.AddressWrapper;
import com.venefica.model.BusinessUserData;
import com.venefica.model.Gender;
import com.venefica.model.Image;
import com.venefica.model.MemberUserData;
import com.venefica.model.User;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * User data transfer object.
 *
 * @author Sviatoslav Grebenchukov
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDto extends DtoBase {
    
    // out
    private Long id;
    // in, out
    private String name;
    // in, out
    private String firstName;
    // in, out
    private String lastName;
    // in, out
    private String email;
    // in, out
    private String phoneNumber;
    // in, out
    private Date dateOfBirth;
    // in, out
    private String about;
    // in, out
    private ImageDto avatar;
    // out
    private Date joinedAt;
    // out
    private boolean inFollowers;
    // out
    private boolean inFollowings;
    // in, out
    private Gender gender;
    // in, out
    private AddressDto address;
    // in, out
    private boolean businessAccount;
    // out
    private BigDecimal score;
    // out
    private BigDecimal pendingScore;
    // out
    private UserStatisticsDto statistics;
    
    // business user data
    
    // in, out
    private String businessName;
    // in, out
    private String contactName;
    // in, out
    private Long businessCategoryId;
    // in, out
    private String businessCategory;
    // in, out
    private List<AddressDto> addresses;
    
    // Required for JAX-WS
    public UserDto() {
    }
    
    /**
     * Constructs the DTO object form the domain object.
     *
     * @param user domain object
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public UserDto(User user) {
        this(user, true);
    }
    
    public UserDto(User user, boolean includeUserPoints) {
        id = user.getId();
        name = user.getName();
        email = user.getEmail();
        phoneNumber = user.getPhoneNumber();
        about = user.getAbout();
        joinedAt = user.getJoinedAt();
        avatar = user.getAvatar() != null ? new ImageDto(user.getAvatar()) : null;
        address = new AddressDto(user.getAddress(), user.getLocation());
        businessAccount = user.isBusinessAccount();
        user.getUserData().updateUserDto(this);
        
        if ( includeUserPoints && user.getUserPoint() != null ) {
            score = user.getUserPoint().getScore();
            pendingScore = user.getUserPoint().getPendingScore();
        }
    }

    /**
     * Updates the domain object using values from the DTO object.
     *
     * @param user domain object to update
     */
    public void update(User user, ImageDao imageDao, AddressWrapperDao addressWrapperDao) {
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setAbout(about);
        user.setAddress(address != null ? address.getAddress() : null);
        user.setLocation(address != null ? address.getLocation() : null);
        user.getUserData().updateUser(this);
        
        // Handle avatar image
        if (avatar != null && avatar.getImgType() != null && avatar.getData() != null) {
            if (user.getAvatar() != null) {
                Image avatarImage = user.getAvatar();
                user.setAvatar(null);
                imageDao.delete(avatarImage);
            }

            Image avatarImage = avatar.toImage();
            imageDao.save(avatarImage);

            // Set new avatar image
            user.setAvatar(avatarImage);
        }
        
        if ( user.isBusinessAccount() ) {
            List<AddressWrapper> addressWrappers = ((BusinessUserData) user.getUserData()).getAddresses();
            if ( addressWrappers != null && !addressWrappers.isEmpty() ) {
                for ( AddressWrapper addressWrapper : addressWrappers ) {
                    addressWrapperDao.saveOrUpdate(addressWrapper);
                }
            }
        }
    }

    public User toMemberUser(ImageDao imageDao, AddressWrapperDao addressWrapperDao) {
        User user = new User();
        user.setUserData(new MemberUserData());
        update(user, imageDao, addressWrapperDao);
        return user;
    }
    
    public User toBusinessUser(ImageDao imageDao, AddressWrapperDao addressWrapperDao) {
        User user = new User();
        user.setUserData(new BusinessUserData());
        update(user, imageDao, addressWrapperDao);
        return user;
    }
    
    public void addAddress(AddressDto addressDto) {
        if ( addresses == null ) {
            addresses = new LinkedList<AddressDto>();
        }
        addresses.add(addressDto);
    }
    
    // getter/setter
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ImageDto getAvatar() {
        return avatar;
    }

    public void setAvatar(ImageDto avatar) {
        this.avatar = avatar;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }

    //public void setJoinedAt(Date joinedAt) {
    //    this.joinedAt = joinedAt;
    //}

    public boolean isInFollowers() {
        return inFollowers;
    }

    public void setInFollowers(boolean inFollowers) {
        this.inFollowers = inFollowers;
    }

    public boolean isInFollowings() {
        return inFollowings;
    }

    public void setInFollowings(boolean inFollowings) {
        this.inFollowings = inFollowings;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public boolean isBusinessAccount() {
        return businessAccount;
    }

    public void setBusinessAccount(boolean businessAccount) {
        this.businessAccount = businessAccount;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Long getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setBusinessCategoryId(Long businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public String getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

    public List<AddressDto> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDto> addresses) {
        this.addresses = addresses;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public UserStatisticsDto getStatistics() {
        return statistics;
    }

    public void setStatistics(UserStatisticsDto statistics) {
        this.statistics = statistics;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getPendingScore() {
        return pendingScore;
    }

    public void setPendingScore(BigDecimal pendingScore) {
        this.pendingScore = pendingScore;
    }
}
