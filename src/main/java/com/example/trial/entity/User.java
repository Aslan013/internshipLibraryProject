package com.example.trial.entity;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id
    private String username;

    @Column(name="userpassword")
    private String password;

    @Column(name="realname")
    private String firstName;

    @Column(name="surname")
    private String lastName;

    @Column(name="idnumber")
    private String tc;

    @Column(name="isadmin")
    private Boolean isAdmin = false;

    @Column(name="email")
    private String email;


    // getters and setters
    
    
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    


    
}

