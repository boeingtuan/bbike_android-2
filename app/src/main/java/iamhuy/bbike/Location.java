package iamhuy.bbike;

import java.util.Date;

/**
 * Created by Tuân on 27/01/2016.
 */
public class Location {
    public int id;
    public String name;
    public int userCreateId;
    public boolean check;
    public double rate;
    public int likeNum;
    public String district;
    public String address;
    public Date openingTime;
    public Date closingTime;
    public int mainImageId;
    public double latitude;
    public double longitude;

    Location(int id, String name, String address, double rate, int likeNum) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rate = rate;
        this.likeNum = likeNum;
    }
}
