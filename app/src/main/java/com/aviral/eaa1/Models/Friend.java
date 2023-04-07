package com.aviral.eaa1.Models;

public class Friend {

    private String name;
    private String email;
    private String balance;
    private String uid;
    private String lifetime;

    public Friend() {
    }

    public Friend(String name, String email, String balance, String uid, String lifetime) {
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.uid = uid;
        this.lifetime = lifetime;
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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLifetime() {
        return lifetime;
    }

    public void setLifetime(String lifetime) {
        this.lifetime = lifetime;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", balance='" + balance + '\'' +
                ", uid='" + uid + '\'' +
                ", lifetime='" + lifetime + '\'' +
                '}';
    }
}
