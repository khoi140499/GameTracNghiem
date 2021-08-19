/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.time.LocalTime;

/**
 *
 * @author DUONGNV
 */
public class Game implements Serializable {

    private int count;
    private String time;
    private User user;

    public Game(int count, String time) {
        this.count = count;
        this.time = time;
    }

    public Game(int count, String time, User user) {
        this.count = count;
        this.time = time;
        this.user = user;
    }

    public Game() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
