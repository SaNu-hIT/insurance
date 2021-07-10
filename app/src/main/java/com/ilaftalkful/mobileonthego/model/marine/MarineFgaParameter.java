
package com.ilaftalkful.mobileonthego.model.marine;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class MarineFgaParameter {

    @SerializedName("CompanyName")
    private String mCompanyName;
    @SerializedName("ContactPersonEmail")
    private String mContactPersonEmail;
    @SerializedName("ContactPersonMobile")
    private String mContactPersonMobile;
    @SerializedName("ContactPersonName")
    private String mContactPersonName;
    @SerializedName("MessageDetail")
    private String mMessageDetail;

    public String getCompanyName() {
        return mCompanyName;
    }

    public void setCompanyName(String companyName) {
        mCompanyName = companyName;
    }

    public String getContactPersonEmail() {
        return mContactPersonEmail;
    }

    public void setContactPersonEmail(String contactPersonEmail) {
        mContactPersonEmail = contactPersonEmail;
    }

    public String getContactPersonMobile() {
        return mContactPersonMobile;
    }

    public void setContactPersonMobile(String contactPersonMobile) {
        mContactPersonMobile = contactPersonMobile;
    }

    public String getContactPersonName() {
        return mContactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        mContactPersonName = contactPersonName;
    }

    public String getMessageDetail() {
        return mMessageDetail;
    }

    public void setMessageDetail(String messageDetail) {
        mMessageDetail = messageDetail;
    }

}
