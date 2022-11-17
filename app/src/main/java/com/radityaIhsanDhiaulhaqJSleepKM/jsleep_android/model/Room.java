package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model;

import java.util.ArrayList;
import java.util.Date;

public class Room extends Serializable {
    public int size;
    public String name;
    public Facility facility;

    public Price price;
    public String address;
    public City city;

    public BedType bedType;
    public ArrayList<Date> booked;
    public int accountId;

    public Room(int id) {
        super(id);
    }
}
