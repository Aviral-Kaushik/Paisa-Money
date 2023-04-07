package com.aviral.eaa1.Models;

public class WithdrawRequest {

    private String userId;
    private String name;
    private String number;
    private String id;
    private double amount;
    private String bankName;
    private String method;
    private String uniqueID;

    public WithdrawRequest(String userId,
                           String name,
                           String number,
                           String id,
                           double amount,
                           String bankName,
                           String method,
                           String uniqueID) {
        this.userId = userId;
        this.name = name;
        this.number = number;
        this.id = id;
        this.amount = amount;
        this.bankName = bankName;
        this.method = method;
        this.uniqueID = uniqueID;
    }

    public WithdrawRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }
}
