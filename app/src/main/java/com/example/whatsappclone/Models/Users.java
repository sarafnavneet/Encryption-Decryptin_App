package com.example.whatsappclone.Models;


public class Users {

    private String profilePic, userName, mail, userId, password, lastMessage;

    public String getProfilePic() {
        return profilePic;
    }

    public Users(){}

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserId(String key) {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }


//   User sign Up info
    public Users( String userName,
                 String mail,
                 String password) {
        this.userName = userName;
        this.mail = mail;
        this.password = password;

    }


    public String getUserId() {
        return userId;
    }
}
