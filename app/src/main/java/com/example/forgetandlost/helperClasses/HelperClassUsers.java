package com.example.forgetandlost.helperClasses;

public class HelperClassUsers {
    String email, name, uid, image;

    public HelperClassUsers() {
    }

    public HelperClassUsers(String email, String name, String uid, String image) {
        this.email = email;
        this.name = name;
        this.uid = uid;
        this.image = image;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String password) {
        this.uid = password;
    }

}
