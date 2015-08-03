package atos.net.interestingplaces.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by a551481 on 03-08-2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceOfInterest {
    @JsonProperty("id")
    private int mId;
    @JsonProperty("title")
    private String mTitle;
    @JsonProperty("transport")
    private String mTransport;
    @JsonProperty("email")
    private String mEmail;
    @JsonProperty("geocoordinates")
    private String mGeoCoordinates;
    @JsonProperty("description")
    private String mDescription;
    @JsonProperty("phone")
    private String mPhone;


    public int getId() {
        return mId;
    }

    public void setId(final int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(final String title) {
        mTitle = title;
    }

    public String getTransport() {
        return mTransport;
    }

    public void setTransport(final String transport) {
        mTransport = transport;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(final String email) {
        mEmail = email;
    }

    public String getGeoCoordinates() {
        return mGeoCoordinates;
    }

    public void setGeoCoordinates(final String geoCoordinates) {
        mGeoCoordinates = geoCoordinates;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(final String description) {
        mDescription = description;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(final String phone) {
        mPhone = phone;
    }

    @Override
    public String toString() {
        return "PlaceOfInterest{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mTransport='" + mTransport + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mGeoCoordinates='" + mGeoCoordinates + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mPhone='" + mPhone + '\'' +
                '}';
    }
}
