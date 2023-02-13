package org.example.utils;


import events.Event;

public interface Observable{
    void addObserver(Observer e);
    void removeObserver(Observer e);
    void notifyObservers();
}
