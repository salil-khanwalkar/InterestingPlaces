package net.atos.interestingplaces.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "PLACE".
 */
public class Place {

    private Long id;
    private int placeId;
    private String title;
    private String transport;
    private String email;
    private String geocoordinates;
    private String description;
    private String phone;
    private String address;
    private Integer level;

    public Place() {
    }

    public Place(Long id) {
        this.id = id;
    }

    public Place(Long id, int placeId, String title, String transport, String email, String geocoordinates, String description, String phone, String address, Integer level) {
        this.id = id;
        this.placeId = placeId;
        this.title = title;
        this.transport = transport;
        this.email = email;
        this.geocoordinates = geocoordinates;
        this.description = description;
        this.phone = phone;
        this.address = address;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGeocoordinates() {
        return geocoordinates;
    }

    public void setGeocoordinates(String geocoordinates) {
        this.geocoordinates = geocoordinates;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

}
