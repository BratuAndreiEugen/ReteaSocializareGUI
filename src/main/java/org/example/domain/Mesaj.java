package org.example.domain;

import java.time.LocalDateTime;

public class Mesaj extends Entity<Long>{
    private Utilizator sender;

    private Utilizator receiver;

    private String content;

    private LocalDateTime date;


    public Mesaj(Utilizator sender, Utilizator receiver, String content, LocalDateTime date) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.date = date;
    }

    public Utilizator getSender() {
        return sender;
    }

    public void setSender(Utilizator sender) {
        this.sender = sender;
    }

    public Utilizator getReceiver() {
        return receiver;
    }

    public void setReceiver(Utilizator receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
