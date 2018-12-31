package com.app.healthybee.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressModule implements Parcelable {
    private String id;
    private String user;
    private String addressType;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String zipcode;
    private String landmark;
    private String createdAt;
    private String updatedAt;



    public AddressModule() {
    }


    protected AddressModule(Parcel in) {
        id = in.readString();
        user = in.readString();
        addressType = in.readString();
        line1 = in.readString();
        line2 = in.readString();
        city = in.readString();
        state = in.readString();
        zipcode = in.readString();
        landmark = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user);
        dest.writeString(addressType);
        dest.writeString(line1);
        dest.writeString(line2);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(zipcode);
        dest.writeString(landmark);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddressModule> CREATOR = new Creator<AddressModule>() {
        @Override
        public AddressModule createFromParcel(Parcel in) {
            return new AddressModule(in);
        }

        @Override
        public AddressModule[] newArray(int size) {
            return new AddressModule[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static Creator<AddressModule> getCREATOR() {
        return CREATOR;
    }
}
