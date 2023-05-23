package com.example.a71;

import android.os.Parcel;
import android.os.Parcelable;

public class Advert implements Parcelable {

    private int id;
    private String postType;
    private String description;
    private String date;
    private String location;
    private String name;
    private String phoneNumber;
    private double latitude;
    private double longitude;

    public Advert(int id, String postType, String description, String date, String location, double latitude, double longitude) {
        this.id = id;
        this.postType = postType;
        this.description = description;
        this.date = date;
        this.location = location;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Advert(Parcel in) {
        id = in.readInt();
        postType = in.readString();
        description = in.readString();
        date = in.readString();
        location = in.readString();
        name = in.readString();
        phoneNumber = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Advert> CREATOR = new Creator<Advert>() {
        @Override
        public Advert createFromParcel(Parcel in) {
            return new Advert(in);
        }

        @Override
        public Advert[] newArray(int size) {
            return new Advert[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(postType);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(location);
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public String toString() {
        return "Advert{" +
                "id=" + id +
                ", postType='" + postType + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

