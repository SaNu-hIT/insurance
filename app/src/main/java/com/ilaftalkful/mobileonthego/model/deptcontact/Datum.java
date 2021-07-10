
package com.ilaftalkful.mobileonthego.model.deptcontact;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Datum {

    @SerializedName("DepartmentID")
    private Long mDepartmentID;
    @SerializedName("DepartmentLocationLat")
    private Double mDepartmentLocationLat;
    @SerializedName("DepartmentLocationLon")
    private Double mDepartmentLocationLon;
    @SerializedName("DepartmentName")
    private String mDepartmentName;
    @SerializedName("EmailID")
    private String mEmailID;
    @SerializedName("PersonName")
    private String mPersonName;
    @SerializedName("PhoneNumber")
    private String mPhoneNumber;
    @SerializedName("AddressLine1")
    private String addressLine1;
    @SerializedName("AddressLine2")
    private String addressLine2;
    @SerializedName("ShowInContactUs")
    private Integer shownInContactUs;


    public Integer ShownInContactUs() {
        return shownInContactUs;
    }

    public void setShownInContactUs(Integer shownInContactUs) {
        this.shownInContactUs = shownInContactUs;
    }

    public Long getDepartmentID() {
        return mDepartmentID;
    }

    public void setDepartmentID(Long departmentID) {
        mDepartmentID = departmentID;
    }

    public Double getDepartmentLocationLat() {
        return mDepartmentLocationLat;
    }

    public void setDepartmentLocationLat(Double departmentLocationLat) {
        mDepartmentLocationLat = departmentLocationLat;
    }

    public Double getDepartmentLocationLon() {
        return mDepartmentLocationLon;
    }

    public void setDepartmentLocationLon(Double departmentLocationLon) {
        mDepartmentLocationLon = departmentLocationLon;
    }

    public String getDepartmentName() {
        return mDepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        mDepartmentName = departmentName;
    }

    public String getEmailID() {
        return mEmailID;
    }

    public void setEmailID(String emailID) {
        mEmailID = emailID;
    }

    public String getPersonName() {
        return mPersonName;
    }

    public void setPersonName(String personName) {
        mPersonName = personName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String geAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        addressLine1 = addressLine1;
    }
 public String geAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        addressLine2 = addressLine2;
    }

}
