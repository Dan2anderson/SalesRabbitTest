package com.anderson.daniel.salesrabbittest;

/**
 * Created by Daniel on 1/20/2016.
 */
public class DetailsModel {
    private String first_name;
    private String last_name;
    private String phone;
    private String bio;

    public DetailsModel(String fName,String lName,String phoneNum,String biography){
        first_name=fName;
        last_name=lName;
        phone=phoneNum;
        bio=biography;
    }

    public String getBio() {
        return bio;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhone() {
        return phone;
    }


}


