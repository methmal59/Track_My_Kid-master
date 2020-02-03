package com.project.dreamsquad.trackmykid.models;

/**
 * Created by this pc on 16-05-17.
 */

public class Profile {

    String name;
    int image;
    String relation;
    String phone_no;
    String email;

    public Profile(String name, String relation, String phone_no, String email) {
        this.name = name;
        this.relation = relation;
        this.phone_no = phone_no;
        this.email = email;
    }

    public Profile(String name, int image, String relation, String phone_no, String email) {
        this.name = name;
        this.image = image;
        this.relation = relation;
        this.phone_no = phone_no;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
