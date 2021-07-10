
package com.ilaftalkful.mobileonthego.model.log;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Datum {

    @SerializedName("ModuleDetails")
    private List<ModuleDetail> mModuleDetails;
    @SerializedName("ModuleName")
    private String mModuleName;

    public List<ModuleDetail> getModuleDetails() {
        return mModuleDetails;
    }

    public void setModuleDetails(List<ModuleDetail> moduleDetails) {
        mModuleDetails = moduleDetails;
    }

    public String getModuleName() {
        return mModuleName;
    }

    public void setModuleName(String moduleName) {
        mModuleName = moduleName;
    }

}
