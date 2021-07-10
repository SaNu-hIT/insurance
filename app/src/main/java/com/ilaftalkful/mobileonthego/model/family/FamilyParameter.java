
package com.ilaftalkful.mobileonthego.model.family;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class FamilyParameter {
    @SerializedName("DOB")
    private String mDOB;
    @SerializedName("FamilyMemberID")
    private String mFamilyMemberID;
    @SerializedName("FullName")
    private String mFullName;
    @SerializedName("PassportNumber")
    private String mPassportNumber;
    @SerializedName("RelationID")
    private int mRelationID=-1;



    public String getDOB() {
        return mDOB;
    }

    public void setDOB(String dOB) {
        mDOB = dOB;
    }

    public String getFamilyMemberID() {
        return mFamilyMemberID;
    }

    public void setFamilyMemberID(String familyMemberID) {
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

}
