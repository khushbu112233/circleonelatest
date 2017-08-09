package com.amplearch.circleonet.Model;


public class UserObject {

    private String username;
    private String email;
    private String password;
    private String userId;
    private String gender;
    private String image;
    private boolean loginOption;

    public UserObject(String username, String email, String password, String userId, String gender, String image, boolean loginOption) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.gender = gender;
        this.image = image;
        this.loginOption = loginOption;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isLoginOption() {
        return loginOption;
    }

    public void setLoginOption(boolean loginOption) {
        this.loginOption = loginOption;
    }
}
