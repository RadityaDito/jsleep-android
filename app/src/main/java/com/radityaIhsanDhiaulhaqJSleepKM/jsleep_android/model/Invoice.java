package com.radityaIhsanDhiaulhaqJSleepKM.jsleep_android.model;

public class Invoice extends Serializable {
    public int buyerId;
    public int renterId;
    public RoomRating rating;
    public PaymentStatus status;

    public enum RoomRating {
        NONE, BAD, NEUTRAL, GOOD
    }


    public enum PaymentStatus {
        FAILED, WAITING, SUCCESS
    }
}


