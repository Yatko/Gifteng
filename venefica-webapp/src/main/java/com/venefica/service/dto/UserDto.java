package com.venefica.service.dto;

import com.venefica.dao.ImageDao;
import com.venefica.model.Gender;
import com.venefica.model.Image;
import com.venefica.model.MemberUserData;
import com.venefica.model.User;
import java.util.Date;
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
    
    // TODO: add necessary fields
    // Required for JAX-WS
    public UserDto() {
    }

    /**
     * Constructs the DTO object form the domain object.
     *
     * @param user domain object
     */
    public UserDto(User user) {
        id = user.getId();
        name = user.getName();
        email = user.getEmail();
        phoneNumber = user.getPhoneNumber();
        joinedAt = user.getJoinedAt();
        
        firstName = ((MemberUserData) user.getUserData()).getFirstName();
        lastName = ((MemberUserData) user.getUserData()).getLastName();
        gender = ((MemberUserData) user.getUserData()).getGender();
        dateOfBirth = ((MemberUserData) user.getUserData()).getDateOfBirth();

        avatar = user.getAvatar() != null ? new ImageDto(user.getAvatar()) : null;
        address = user.getAddress() != null ? new AddressDto(user.getAddress()) : null;
    }

    /**
     * Updates the domain object using values from the DTO object.
     *
     * @param user domain object to update
     */
    public void update(User user, ImageDao imageDao) {
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        
        ((MemberUserData) user.getUserData()).setDateOfBirth(dateOfBirth);
        ((MemberUserData) user.getUserData()).setLastName(lastName);
        ((MemberUserData) user.getUserData()).setFirstName(firstName);
        ((MemberUserData) user.getUserData()).setGender(gender);

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
        
        if ( address != null ) {
            user.setAddress(address.getAddress());
        }
    }

    public User toUser(ImageDao imageDao) {
        User user = new User();
        user.setUserData(new MemberUserData());
        update(user, imageDao);
        return user;
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
}
