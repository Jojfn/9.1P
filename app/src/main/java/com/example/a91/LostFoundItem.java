package com.example.a91;

import java.io.Serializable;

public class LostFoundItem implements Serializable {
    private int id;
    private String type;
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;
    private double latitude;
    private double longitude;
    private String category;
    private byte[] image;
    private long timestamp;

    public LostFoundItem(int id, String type, String name, String phone, String description, String date, String location, double latitude, double longitude, String category, byte[] image, long timestamp) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.image = image;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getLocation() { return location; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getCategory() { return category; }
    public byte[] getImage() { return image; }
    public long getTimestamp() { return timestamp; }
}
