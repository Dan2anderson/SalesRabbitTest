package com.anderson.daniel.salesrabbittest;

/**
 * Created by Daniel on 1/20/2016.
 */
public class Friend {

    private String img;
    private String name;
    private String status;
    private boolean available;

    public Friend(String name, String imgStr, String stat, boolean isAvailable){
        this.name = name;
        this.img = imgStr;
        this.status = stat;
        this.available = isAvailable;
    }


    public String getImgUrlStr() {
        return this.img;
    }

    public String getName() {
        return this.name;
    }



    public String getStatus() {
        return this.status;
    }

    public boolean isAvailable() {
        return this.available;
    }


}
