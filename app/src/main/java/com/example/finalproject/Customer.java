package com.example.finalproject;

public class Customer {

    String email;
    String name;
    String pet;
    String phone;
    String uri;

    public Customer() {
    }

    public Customer(String email, String name, String pet, String phone, String uri) {
        this.email = email;
        this.name = name;
        this.pet = pet;
        this.phone = phone;
        this.uri = uri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPet() {
        return pet;
    }

    public void setPet(String pet) {
        this.pet = pet;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String geturi() {
        return uri;
    }

    public void seturi(String uri) {
        this.uri = uri;
    }
}
