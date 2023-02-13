package org.example.domain;

import java.time.LocalDateTime;

public class PrietenieUI {
    private String user;
    private String friendsFrom;

    private String status;

    public PrietenieUI(String user,String friendsFrom) {
        this.user = user;
        this.friendsFrom = friendsFrom;
        this.status = "Pending";
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() { return friendsFrom;}

    public String getFriendsFrom() {
        return friendsFrom;
    }

    public void setFriendsFrom(String friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
