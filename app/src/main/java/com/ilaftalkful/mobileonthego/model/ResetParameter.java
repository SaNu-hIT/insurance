
package com.ilaftalkful.mobileonthego.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ResetParameter {
    @SerializedName("Password")
    private String mPassword;
    @SerializedName("ResetToken")
    private String mResetToken;

    public void setResetToken(String mResetToken) {
        this.mResetToken = mResetToken;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }


}
