
package com.ilaftalkful.mobileonthego.model.log;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ModuleDetail {


    String type="Travel Insurance";
    String motorClaim="Motor Claim";

    @SerializedName("Amount")
    private String mAmount;
    @SerializedName("InitiatedDate")
    private String mInitiatedDate;
    @SerializedName("LogType")
    private String mLogType;
    @SerializedName("LogTypeEng")
    private String mLogTypeEnglish;
    @SerializedName("PaymentMethod")
    private String mPaymentMethod;
    @SerializedName("PaymentOption")
    private String mPaymentOption;
    @SerializedName("PolicyDateEnd")
    private String mPolicyDateEnd;
    @SerializedName("PolicyDateStart")
    private String mPolicyDateStart;
    @SerializedName("PolicyID")
    private String mPolicyID;
    @SerializedName("Status")
    private String mStatus;
    @SerializedName("TravelDate")
    private String mTravelDate;



    public String getAmount() {
        return mAmount;
    }

  public Serializable getType() {
        return type;
    }
  public Serializable getMotor() {
        return motorClaim;
    }

    public void setAmount(String amount) {
        mAmount = amount;
    }

    public String getInitiatedDate() {
        return mInitiatedDate;
    }

    public void setInitiatedDate(String initiatedDate) {
        mInitiatedDate = initiatedDate;
    }

    public String getLogType() {
        return mLogType;
    }

    public void setLogType(String logType) {
        mLogType = logType;
    }

    public String getPaymentMethod() {
        return mPaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        mPaymentMethod = paymentMethod;
    }

    public String getPaymentOption() {
        return mPaymentOption;
    }

    public void setPaymentOption(String paymentOption) {
        mPaymentOption = paymentOption;
    }

    public String getPolicyDateEnd() {
        return mPolicyDateEnd;
    }

    public void setPolicyDateEnd(String policyDateEnd) {
        mPolicyDateEnd = policyDateEnd;
    }

    public String getPolicyDateStart() {
        return mPolicyDateStart;
    }

    public void setPolicyDateStart(String policyDateStart) {
        mPolicyDateStart = policyDateStart;
    }

    public String getPolicyID() {
        return mPolicyID;
    }

    public void setPolicyID(String policyID) {
        mPolicyID = policyID;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTravelDate() {
        return mTravelDate;
    }

    public void setTravelDate(String travelDate) {
        mTravelDate = travelDate;
    }

    public String getLogTypeEnglish() {
        return mLogTypeEnglish;
    }

    public void setLogTypeEnglish(String mLogTypeEnglish) {
        this.mLogTypeEnglish = mLogTypeEnglish;
    }
}
