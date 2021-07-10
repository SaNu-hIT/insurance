
package com.ilaftalkful.mobileonthego.model.health.hospital;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class HospitalNetwork {

    @SerializedName("EmailID")
    private String mEmailID;
    @SerializedName("HospitalID")
    private Long mHospitalID;
    @SerializedName("HospitalName")
    private String mHospitalName;
    @SerializedName("HospitalRegion")
    private String mHospitalRegion;
    @SerializedName("LogoFileName")
    private String mLogoFileName;
    @SerializedName("LogoFilePath")
    private String mLogoFilePath;
    @SerializedName("OrderNo")
    private Long mOrderNo;
    @SerializedName("PhoneNumber")
    private String mPhoneNumber;

    public String getEmailID() {
        return mEmailID;
    }

    public void setEmailID(String emailID) {
        mEmailID = emailID;
    }

    public Long getHospitalID() {
        return mHospitalID;
    }

    public void setHospitalID(Long hospitalID) {
        mHospitalID = hospitalID;
    }

    public String getHospitalName() {
        return mHospitalName;
    }

    public void setHospitalName(String hospitalName) {
        mHospitalName = hospitalName;
    }

    public String getHospitalRegion() {
        return mHospitalRegion;
    }

    public void setHospitalRegion(String hospitalRegion) {
        mHospitalRegion = hospitalRegion;
    }

    public String getLogoFileName() {
        return mLogoFileName;
    }

    public void setLogoFileName(String logoFileName) {
        mLogoFileName = logoFileName;
    }

    public String getLogoFilePath() {
        return mLogoFilePath;
    }

    public void setLogoFilePath(String logoFilePath) {
        mLogoFilePath = logoFilePath;
    }

    public Long getOrderNo() {
        return mOrderNo;
    }

    public void setOrderNo(Long orderNo) {
        mOrderNo = orderNo;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

}
