package com.aviral.eaa1.Models;

public class RegisterUser {

    private String name;
    private String email;
    private String uid;
    private String token;
    private String password;
    private String referralCode;
    private String referredBy;
    private String model;
    private String brand;

    public RegisterUser(String name,
                        String email,
                        String uid,
                        String token,
                        String password,
                        String referralCode,
                        String referredBy,
                        String model,
                        String brand) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.token = token;
        this.password = password;
        this.referralCode = referralCode;
        this.referredBy = referredBy;
        this.model = model;
        this.brand = brand;
    }

    public RegisterUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}

