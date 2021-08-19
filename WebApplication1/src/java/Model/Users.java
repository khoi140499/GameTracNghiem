/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;

/**
 *
 * @author Khoi
 */
public class Users implements Serializable{
    private int Id;
    private String userName;
    private String passWord;
    private int online;
    private float point;

    public Users() {
    }

    public Users(int Id, String userName, String passWord, int online, float point) {
        this.Id = Id;
        this.userName = userName;
        this.passWord = passWord;
        this.online = online;
        this.point = point;
    }

    public Users(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }
    
    public Users(String userName, float point) {
        this.userName = userName;
        this.point = point;
    }
    
    public int getId() {
        return Id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public int getOnline() {
        return online;
    }

    public float getPoint() {
        return point;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    
    
}
