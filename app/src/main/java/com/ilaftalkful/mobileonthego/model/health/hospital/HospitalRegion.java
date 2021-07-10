
package com.ilaftalkful.mobileonthego.model.health.hospital;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class HospitalRegion {

    @SerializedName("Id")
    private Long mId;
    @SerializedName("Text")
    private String mText;
    @SerializedName("TextAr")
    private Object mTextAr;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Object getTextAr() {
        return mTextAr;
    }

    public void setTextAr(Object textAr) {
        mTextAr = textAr;
    }

}
