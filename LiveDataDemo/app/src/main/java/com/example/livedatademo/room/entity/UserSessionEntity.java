package com.example.livedatademo.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "user_session_table")
public class UserSessionEntity {

    @PrimaryKey
    @NonNull
    private int uid = 1;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "email_address")
    private String emailAddress;

    @ColumnInfo(name = "favorite_color")
    private String favoriteColor;

    /**
     * Getter for retrieving primary key
     *
     * @return Primary key
     */
    public int getUid() {
        return uid;
    }

    /**
     * Setter for setting primary key
     *
     * @param uid Primary key
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * Getter for retrieving name
     *
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for setting name
     *
     * @param name Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for retrieving email address
     *
     * @return Email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Setter for setting email
     *
     * @param emailAddress Email address
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Getter for retrieving favorite color
     *
     * @return Favorite color
     */
    public String getFavoriteColor() {
        return favoriteColor;
    }

    /**
     * Setter for setting favorite color
     *
     * @param favoriteColor Favorite color
     */
    public void setFavoriteColor(String favoriteColor) {
        this.favoriteColor = favoriteColor;
    }

}
