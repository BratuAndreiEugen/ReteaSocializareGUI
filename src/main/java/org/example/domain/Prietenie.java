package org.example.domain;


import java.time.LocalDateTime;

public class Prietenie extends Entity<Long> {
    private Utilizator user1;
    private Utilizator user2;
    private LocalDateTime friendsFrom;

    private Long status;

    public Prietenie(Utilizator user1, Utilizator user2, LocalDateTime friendsFrom) {
        this.user1 = user1;
        this.user2 = user2;
        this.friendsFrom = friendsFrom;
        this.status = 0L;
    }

    public Utilizator getUser1() {
        return user1;
    }

    public void setUser1(Utilizator user1) {
        this.user1 = user1;
    }

    public Utilizator getUser2() {
        return user2;
    }

    public void setUser2(Utilizator user2) {
        this.user2 = user2;
    }

    public LocalDateTime getDate() { return friendsFrom;}

    @Override
    public String toString() {
        return "Prietenie{" +
                "user1=" + user1 +
                ", user2=" + user2 +
                ", friendsFrom=" + friendsFrom +
                '}';
    }

    public Long getStatus(){
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
