package com.app.healthybee.models;

/*
 * Created by Amod on 20/9/18.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {

    private int id;
    private String AddressType;
    private String AddressLine1;
    private String AddressLine2;
    private String AddressCity;
    private String AddressState;
    private String AddressZipCode;
    private String AddressLandmark;

    public Address() {
    }

    public Address(String addressType, String addressLine1, String addressLine2, String addressCity, String addressState, String addressZipCode, String addressLandmark) {
        AddressType = addressType;
        AddressLine1 = addressLine1;
        AddressLine2 = addressLine2;
        AddressCity = addressCity;
        AddressState = addressState;
        AddressZipCode = addressZipCode;
        AddressLandmark = addressLandmark;
    }

    protected Address(Parcel in) {
        id = in.readInt();
        AddressType = in.readString();
        AddressLine1 = in.readString();
        AddressLine2 = in.readString();
        AddressCity = in.readString();
        AddressState = in.readString();
        AddressZipCode = in.readString();
        AddressLandmark = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(AddressType);
        dest.writeString(AddressLine1);
        dest.writeString(AddressLine2);
        dest.writeString(AddressCity);
        dest.writeString(AddressState);
        dest.writeString(AddressZipCode);
        dest.writeString(AddressLandmark);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddressType() {
        return AddressType;
    }

    public void setAddressType(String addressType) {
        AddressType = addressType;
    }

    public String getAddressLine1() {
        return AddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        AddressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return AddressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        AddressLine2 = addressLine2;
    }

    public String getAddressCity() {
        return AddressCity;
    }

    public void setAddressCity(String addressCity) {
        AddressCity = addressCity;
    }

    public String getAddressState() {
        return AddressState;
    }

    public void setAddressState(String addressState) {
        AddressState = addressState;
    }

    public String getAddressZipCode() {
        return AddressZipCode;
    }

    public void setAddressZipCode(String addressZipCode) {
        AddressZipCode = addressZipCode;
    }

    public String getAddressLandmark() {
        return AddressLandmark;
    }

    public void setAddressLandmark(String addressLandmark) {
        AddressLandmark = addressLandmark;
    }

    public static Creator<Address> getCREATOR() {
        return CREATOR;
    }
}
