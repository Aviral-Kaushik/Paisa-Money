package com.aviral.eaa1.Models;

public class ReferralEarning {

    private String friendsName;
    private String earning;

    public ReferralEarning(String friendsName, String earning) {
        this.friendsName = friendsName;
        this.earning = earning;
    }

    public String getFriendsName() {
        return friendsName;
    }

    public void setFriendsName(String friendsName) {
        this.friendsName = friendsName;
    }

    public String getEarning() {
        return earning;
    }

    public void setEarning(String earning) {
        this.earning = earning;
    }
}
