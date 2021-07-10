
package com.ilaftalkful.mobileonthego.model.health.claimassistance;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Datum {

    @SerializedName("EmailID")
    private String mEmailID;
    @SerializedName("HealthClaimAssistanceID")
    private Long mHealthClaimAssistanceID;
    @SerializedName("HealthClaimAssistanceName")
    private String mHealthClaimAssistanceName;
    @SerializedName("HealthClaimAssistanceRegion")
    private String mHealthClaimAssistanceRegion;
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

    public Long getHealthClaimAssistanceID() {
        return mHealthClaimAssistanceID;
    }

    public void setHealthClaimAssistanceID(Long healthClaimAssistanceID) {
        mHealthClaimAssistanceID = healthClaimAssistanceID;
    }

    public String getHealthClaimAssistanceName() {
        return mHealthClaimAssistanceName;
    }

    public void setHealthClaimAssistanceName(String healthClaimAssistanceName) {
        mHealthClaimAssistanceName = healthClaimAssistanceName;
    }

    public String getHealthClaimAssistanceRegion() {
        return mHealthClaimAssistanceRegion;
    }

    public void setHealthClaimAssistanceRegion(String healthClaimAssistanceRegion) {
        mHealthClaimAssistanceRegion = healthClaimAssistanceRegion;
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
