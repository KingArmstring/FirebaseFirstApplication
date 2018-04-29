package com.armstring.firebasefirstapplication;

public class User {

    String userName;
    String emailAddress;
    String userImage;


    public User() {
    }

    public User(String userName, String emailAddress, String userImage) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
