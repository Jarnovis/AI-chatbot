package com.groepb.project2.AI;

import com.groepb.project2.Windows.IResponse;
import com.groepb.project2.data.Conversation;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public abstract class Observer {
    private List<IResponse> observers = new ArrayList<IResponse>();

    private static Observer INSTANCE;

    public static void initInstance() {
        if (Observer.INSTANCE == null) {
            Observer.INSTANCE = new Observer() {};
        }
    }

    public static Observer getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("ACS_Updater instance not initialized. Call initInstance() first.");
        }
        return INSTANCE;
    }

    public void addObserver(IResponse observable){
        observers.add(observable);
    }

    public void removeObserver(IResponse observable){
        observers.remove(observable);
    }

    public void notifyObservers(String message){
        for (IResponse observer : observers){
            observer.update(message);
        }
    }

    public void notifyObserversFullResponse(String message, Conversation conversation){
        for (IResponse observer : observers){
            observer.fullResponse(message, conversation);
        }
    }
}
