
package com.ilaftalkful.mobileonthego.model.health.hospital;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Data {

    @SerializedName("HospitalNetworks")
    private List<HospitalNetwork> mHospitalNetworks;
    @SerializedName("HospitalRegions")
    private List<HospitalRegion> mHospitalRegions;

    public List<HospitalNetwork> getHospitalNetworks() {
        return mHospitalNetworks;
    }

    public void setHospitalNetworks(List<HospitalNetwork> hospitalNetworks) {
        mHospitalNetworks = hospitalNetworks;
    }

    public List<HospitalRegion> getHospitalRegions() {
        return mHospitalRegions;
    }

    public void setHospitalRegions(List<HospitalRegion> hospitalRegions) {
        mHospitalRegions = hospitalRegions;
    }

}
