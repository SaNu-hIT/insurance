
package com.ilaftalkful.mobileonthego.model.marine;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class MarineRespo {

    @SerializedName("Data")
    private String mData;
    @SerializedName("IsSuccess")
    private Boolean mIsSuccess;
    @SerializedName("Message")
    private String mMessage;

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }

    public Boolean getIsSuccess() {
        return mIsSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        mIsSuccess = isSuccess;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

}
