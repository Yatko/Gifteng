package com.venefica.service.dto;

import com.venefica.dao.ImageDao;
import com.venefica.model.Image;
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
    private String country;
    // in, out
    private String city;
    // in, out
    private String area;
    // in, out
    private String zipCode;
    // in, out
    private boolean businessAcc;
    // in, out
    private ImageDto avatar;
    // out
    private Date joinedAt;

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
        name = user.getName();
        firstName = user.getFirstName();
        lastName = user.getLastName();

        email = user.getEmail();
        phoneNumber = user.getPhoneNumber();

        dateOfBirth = user.getDateOfBirth();
        country = user.getCountry();
        city = user.getCity();
        area = user.getArea();
        zipCode = user.getZipCode();
        businessAcc = user.isBusinessAcc();
        joinedAt = user.getJoinedAt();
        avatar = user.getAvatar() != null ? new ImageDto(user.getAvatar()) : null;
    }

    /**
     * Updates the domain object using values from the DTO object.
     *
     * @param user domain object to update
     */
    public void update(User user, ImageDao imageDao) {
        user.setName(name);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setDateOfBirth(dateOfBirth);
        user.setCountry(country);
        user.setCity(city);
        user.setArea(area);
        user.setZipCode(zipCode);
        user.setBusinessAcc(businessAcc);
        //user.setJoinedAt(joinedAt);

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
    }

    public User toUser(ImageDao imageDao) {
        User user = new User();
        update(user, imageDao);
        return user;
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
}
