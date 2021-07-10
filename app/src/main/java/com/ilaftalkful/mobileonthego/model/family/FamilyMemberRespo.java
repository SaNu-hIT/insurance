
package com.ilaftalkful.mobileonthego.model.family;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class FamilyMemberRespo {

    @SerializedName("Data")
    private List<Datum> mData;
    @SerializedName("IsSuccess")
    private Boolean mIsSuccess;
    @SerializedName("Message")
    private String mMessage;

    public List<Datum> getData() {
        return mData;
    }

    public void setData(List<Datum> data) {
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
