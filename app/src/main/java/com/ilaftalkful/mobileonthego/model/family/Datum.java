package com.ilaftalkful.mobileonthego.model.family;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Datum {
    private Boolean isSelected=false;
    @SerializedName("DOB")
    private String mDOB;
    @SerializedName("FamilyMemberID")
    private Long mFamilyMemberID;
    @SerializedName("FullName")
    private String mFullName;
    @SerializedName("PassportNumber")
    private String mPassportNumber;
    @SerializedName("RelationID")
    private int mRelationID;
    @SerializedName("RelationName")
    private String relationName;

    private MutableLiveData<ArrayList<String>> memberList;

    public MutableLiveData<ArrayList<String>> getMemberList() {
        return memberList;
    }

    public void setMemberList(MutableLiveData<ArrayList<String>> memberList) {
        this.memberList = memberList;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getDOB() {
        return mDOB;
    }

    public void setDOB(String dOB) {
        mDOB = dOB;
    }

    public Long getFamilyMemberID() {
        return mFamilyMemberID;
    }

    public void setFamilyMemberID(Long familyMemberID) {
        mFamilyMemberID = familyMemberID;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }

    public String getPassportNumber() {
        return mPassportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        mPassportNumber = passportNumber;
    }

    public int getRelationID() {
        return mRelationID;
    }

    public void setRelationID(int relationID) {
        mRelationID = relationID;
    }

    public String getRelationDescription() {
        return relationName;
    }

    public void setRelationDescription(String relationName) {
        this.relationName = relationName;
    }
}
