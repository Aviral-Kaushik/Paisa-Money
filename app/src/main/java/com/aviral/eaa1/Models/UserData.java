package com.aviral.eaa1.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserData implements Parcelable {

    private String name;
    private String email;
    private String uid;
    private String disabled;
    private String referred;
    private String date;
    private String time;
    private String referredBy;
    private String referralCode;
    private String balance;
    private String referralEarning;
    private String lifetime;
    private String isRewarded;

    public UserData(String name,
                    String email,
                    String uid,
                    String disabled,
                    String referred,
                    String date,
                    String time,
                    String referredBy,
                    String referralCode,
                    String balance,
                    String referralEarning,
                    String lifetime,
                    String isRewarded) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.disabled = disabled;
        this.referred = referred;
        this.date = date;
        this.time = time;
        this.referredBy = referredBy;
        this.referralCode = referralCode;
        this.balance = balance;
        this.referralEarning = referralEarning;
        this.lifetime = lifetime;
        this.isRewarded = isRewarded;
    }

    public UserData() {

    }

    protected UserData(Parcel in) {
        name = in.readString();
        email = in.readString();
        uid = in.readString();
        disabled = in.readString();
        referred = in.readString();
        date = in.readString();
        time = in.readString();
        referredBy = in.readString();
        referralCode = in.readString();
        balance = in.readString();
        referralEarning = in.readString();
        lifetime = in.readString();
        isRewarded = in.readString();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

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

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getReferred() {
        return referred;
    }

    public void setReferred(String referred) {
        this.referred = referred;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getReferralEarning() {
        return referralEarning;
    }

    public void setReferralEarning(String referralEarning) {
        this.referralEarning = referralEarning;
    }

    public String getLifetime() {
        return lifetime;
    }

    public void setLifetime(String lifetime) {
        this.lifetime = lifetime;
    }

    public String getIsRewarded() {
        return isRewarded;
    }

    public void setIsRewarded(String isRewarded) {
        this.isRewarded = isRewarded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(uid);
        parcel.writeString(disabled);
        parcel.writeString(referred);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(referredBy);
        parcel.writeString(referralCode);
        parcel.writeString(balance);
        parcel.writeString(referralEarning);
        parcel.writeString(lifetime);
        parcel.writeString(isRewarded);
    }
}
