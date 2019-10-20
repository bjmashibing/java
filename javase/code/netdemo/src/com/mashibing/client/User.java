package com.mashibing.client;

import java.io.Serializable;

/**
 * @author: 马士兵教育
 * @create: 2019-10-13 15:22
 */
public class User implements Serializable {


    private static final long serialVersionUID = 1590020726742801370L;
    private String username;
    private String password;

    public User(){

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
