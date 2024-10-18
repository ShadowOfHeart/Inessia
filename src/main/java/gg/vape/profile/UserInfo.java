package gg.vape.profile;

import java.sql.Date;

public class UserInfo {

    private String username;
    private int uid;
    private String title;
    private String version;
    private String till;
    private int balance;
    private boolean subscription;
    private String password;

    public UserInfo(String username, int uid, String title, String version, String till, int balance, boolean subscription, String password) {
        this.username = username;
        this.uid = uid;
        this.title = title;
        this.version = version;
        this.till = till;
        this.balance = balance;
        this.subscription = subscription;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public int getUid() {
        return uid;
    }

    public String getVersion() {
        return version;
    }

    public String getTill() {
        return till;
    }

    public String getTitle() {
        return title;
    }

    public int getBalance() {
        return balance;
    }

    public boolean getSubscription() {
        return subscription;
    }

    public String getPassword() {
        return password;
    }
}
