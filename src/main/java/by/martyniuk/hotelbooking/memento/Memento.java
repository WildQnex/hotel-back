package by.martyniuk.hotelbooking.memento;

import java.util.ArrayDeque;

public class Memento {

    private ArrayDeque<String> states;

    public Memento() {
        this.states = new ArrayDeque<>();
    }

    public String getState() {
        return states.getLast();
    }

    public void addState(String state) {
        this.states.addLast(state);
    }

    public ArrayDeque<String> getStates() {
        return states.clone();
    }

}