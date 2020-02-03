package com.project.dreamsquad.trackmykid.models;

/**
 * Created by this pc on 12-05-17.
 */

public class Student {

    private int image;
    private String name;
    private String class_section;
    private String status;



    private String school;

    public Student(int image, String name, String class_section, String status) {
        this.image = image;
        this.name = name;
        this.class_section = class_section;
        this.status = status;
    }

    public Student(String name, String school, String status) {
        this.name = name;
        this.class_section = class_section;
        this.status = status;
        this.school = school;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClass_section() {
        return class_section;
    }

    public void setClass_section(String class_section) {
        this.class_section = class_section;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
