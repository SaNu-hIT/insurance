package com.ilaftalkful.mobileonthego.retrofit

import android.app.Application
import com.ilaftalkful.mobileonthego.model.*
import com.ilaftalkful.mobileonthego.model.family.FamilyParameter
import com.ilaftalkful.mobileonthego.model.health.Department
import com.ilaftalkful.mobileonthego.model.health.HealthCareCorporateQuoteParameter
import com.ilaftalkful.mobileonthego.model.health.claimassistance.Datum
import com.ilaftalkful.mobileonthego.model.health.hospital.Data
import com.ilaftalkful.mobileonthego.model.log.ModuleDetail
import com.ilaftalkful.mobileonthego.model.marine.MarineFgaParameter
import com.ilaftalkful.mobileonthego.model.marine.MarineRespo
import com.ilaftalkful.mobileonthego.model.mototqoute.MotorQouteParameter
import com.ilaftalkful.mobileonthego.model.mototqoute.carmodel.CarModel
import com.ilaftalkful.mobileonthego.model.mototqoute.cartype.CarMakes
import com.ilaftalkful.mobileonthego.model.mototqoute.cartype.CarType
import com.ilaftalkful.mobileonthego.model.regster.MotorPolicies
import com.ilaftalkful.mobileonthego.model.regster.RegisterUserDetails
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @FormUrlEncoded
    //login
    @POST("Login")
    fun doSignIn(
        @Field("UserName") userName: String,
        @Field("Password") password: String,
        @Field("grant_type") grant_type: String? = "password"
    ): Observable<Response<UserLoginResponse>>

    @POST("api/isUserExist")
    fun isUserExist(@Body details: RegisterUserDetails): Observable<Response<BaseResponseStatus<Boolean>>>
    //register
    @Headers(
        "accept: application/json",
        "Content-Type: application/json"
    )
    @POST("api/CreateUser")
    fun doRegisterIn(@Body details: RegisterUserDetails): Observable<Response<BaseResponse>>

    //DashBoard details
    @Headers(
        "accept: application/json",
        "Content-Type: application/json"
    )
    @GET("api/GetDashboardResponse")
    fun getDashBoardDetails(): Observable<Response<BaseResponseStatus<DashBoardResponse>>>

    //Get Travel Plans
    @GET("api/TravelPlans")
    fun getTravelPlans(): Observable<Response<BaseResponseStatus<ArrayList<TravelPlansResponse>>>>

    //Log out

    @POST("api/Logout")
    fun Logout(): Observable<Response<BaseResponse>>

    //manufacturing year

    @GET("api/ManufacturingYears")
    fun getManufacturingYear(): Observable<Response<BaseResponseStatus<ArrayList<ManufacturingYears>>>>


    //CarTypes
    @GET("api/CarTypes")
    fun getMotorCarTypes(): Observable<Response<BaseResponseStatus<ArrayList<CarType>>>>


    //CarModels
    @GET("api/CarModels")
    fun getMotorCarModels(): Observable<Response<BaseResponseStatus<ArrayList<CarModel>>>>

    //CarMakes
    @GET("api/CarMakes")
    fun getMotorCarMakes(): Observable<Response<BaseResponseStatus<ArrayList<CarMakes>>>>

    //Motor Quote
    @POST("api/CreateMotorQuote")
    fun CreateMotorQuote(@Body details: MotorQouteParameter): Observable<Response<BaseResponse>>

    //Motor Policies
    @GET("api/MotorPolicies")
    fun MotorPolicies(): Observable<Response<BaseResponseStatus<MotorPolicies>>>

    //Policy Types
    @GET("api/master/travelPolicyOptions")
    fun travelPolicyOptions(): Observable<Response<BaseResponseStatus<ArrayList<TravelPolicyType>>>>

    //Policy Types
    @GET("api/master/travelPolicyTypes")
    fun travelPolicyTypes(): Observable<Response<BaseResponseStatus<ArrayList<TravelPolicyType>>>>


    @GET("api/master/travelPolicyPeriods")
    fun travelPolicyPeriods(): Observable<Response<BaseResponseStatus<ArrayList<TravelPolicyType>>>>

    //Motor Policy details
    @GET("api/MotorPolicyDetails/{MotorPolicyId}")
    fun motorPolicyDetails(@Path(value = "MotorPolicyId") motorPolicyId: String): Observable<Response<BaseResponseStatus<MotorPolicyDetails>>>

    //Upload File
    @POST("api/UploadFile")
    fun uploadFile(@Body details: ImageUpload): Observable<Response<BaseResponseStatus<ImageUpload>>>

    //GetDepartmentsDropdownList
    @GET("api/GetDepartmentsDropdownList")
    fun getDepartmentsDropdownList(): Observable<Response<BaseResponseStatus<ArrayList<Department>>>>


    //GetHospitalNetworks
    @GET("api/GetHospitalNetworks")
    fun GetHospitalNetworks(): Observable<Response<BaseResponseStatus<Data>>>


    //register
    @Headers(
        "accept: application/json",
        "Content-Type: application/json"
    )
    @POST("api/HealthCareCorporateQuote")
    fun HealthCareCorporateQuote(@Body details: HealthCareCorporateQuoteParameter): Observable<Response<BaseResponse>>


    //GetDepartmentsDropdownList
    @GET("api/GetHealthClaimAssistance")
    fun getHealthClaimAssistance(): Observable<Response<BaseResponseStatus<ArrayList<Datum>>>>


    //GetMarineInsuranceDescriptions
    @GET("api/GetMarineInsuranceDescriptions")
    fun GetMarineInsuranceDescriptions(): Observable<Response<MarineRespo>>

    //GetFGAInsuranceDescriptions
    @GET("api/GetFGAInsuranceDescriptions")
    fun GetFGAInsuranceDescriptions(): Observable<Response<MarineRespo>>


    //GetMarineInsuranceDescriptions
    @Headers(
        "accept: application/json",
        "Content-Type: application/json"
    )
    @POST("api/MarineQuote")
    fun postMarineQoute(@Body param: MarineFgaParameter): Observable<Response<MarineRespo>>

    //GetFGAInsuranceDescriptions
    @Headers(
        "accept: application/json",
        "Content-Type: application/json"
    )
    @POST("api/FGAQuote")
    fun postFgaQoute(@Body param: MarineFgaParameter): Observable<Response<MarineRespo>>


    //GetFGAInsuranceDescriptions
    @GET("api/DepartmentContactList")
    fun DepartmentContactList(): Observable<Response<BaseResponseStatus<ArrayList<com.ilaftalkful.mobileonthego.model.deptcontact.Datum>>>>


    //CarModels
    @GET("api/master/relationships")
    fun getRelation(): Observable<Response<BaseResponseStatus<ArrayList<TravelPolicyType>>>>


    //Motor Policy Claim
    @POST("api/CreateMotorClaim")
    fun CreateMotorClaim(@Body details: MotorClaim): Observable<Response<BaseResponse>>

    //get Garage List
    @GET("api/GetGarages")
    fun GetGarages(): Observable<Response<BaseResponseStatus<GarageResponse>>>
    //get Garage List


    @GET("api/familyMember/get")
    fun GetfamilyMember(): Observable<Response<BaseResponseStatus<ArrayList<com.ilaftalkful.mobileonthego.model.family.Datum>>>>

    @GET("api/Log")
    fun GetLog(): Observable<Response<BaseResponseStatus<ArrayList<ModuleDetail>>>>


    //GetMarineInsuranceDescriptions
    @Headers(
        "accept: application/json",
        "Content-Type: application/json"
    )
    @POST("api/familyMember/add")
    fun postFamilyCreate(@Body param: FamilyParameter): Observable<Response<MarineRespo>>


    @POST("api/familyMember/delete?")
    fun postFamilyDelete(@Query("FamilyMemberID") userName: String): Observable<Response<MarineRespo>>


    //get Garage List

    @POST("api/AddMotorPolicyRenewal")
    fun AddMotorPolicyRenewal(@Body renewPolicyRequest: RenewPolicyRequest): Observable<Response<BaseResponse>>

    //Motor Policy Claim
    @POST("api/travelClaim/add")
    fun AddTravelClaim(@Body details: TravelClaimSubmitRequest): Observable<Response<BaseResponse>>

    @GET("api/master/nationalities")
    fun nationalities(): Observable<Response<BaseResponseStatus<ArrayList<TravelPolicyType>>>>

    @GET("api/travelPolicy/travelPremiumAmount")
    fun travelPremiumAmount(
        @Query("policyPeriodId") policyPeriodId: Int?,
        @Query("policyTypeId") policyTypeId: Int?,
        @Query("policyOptionId") policyOptionId: Int?
    ): Observable<Response<BaseResponseStatus<String>>>

    @POST("api/travelPolicy/add")
    fun travelPolicyAdd(@Body paymentRequest: PaymentRequest): Observable<Response<BaseResponseStatus<String>>>

    @POST("api/SplashMessage")
    fun splashMessage(): Observable<Response<BaseResponseStatus<ArrayList<OptionMarketingModel>>>>

    @GET("api/user/resetPassword")
    fun resetPassword(@Query("emailId") emailId: String): Observable<Response<BaseResponse>>


    @POST("api/user/ChangePassword")
    fun changePassword(@Body password: ResetParameter): Observable<Response<BaseResponse>>



    @POST("api/UpdateUserPassword")
    fun updatePassword(@Body password: PasswordParameter): Observable<Response<BaseResponse>>
    @GET("api/master/appSettings")
    fun getAppSettings():  Observable<Response<BaseResponseStatus<ArrayList<AppSettingsResponse>>>>
    @GET("api/travelPolicy/getTravelPDF")
    fun getTravelPDF(@Query("policyID") policyID: String?): Observable<Response<BaseResponseStatus<PdfDataResponse>>>


    companion object Factory {
        fun create(application: Application, isAuth: Boolean): UserService? {
            val retrofit = RetrofitClient.getRetrofitClient(application, isAuth)
            return retrofit!!.create(UserService::class.java)
        }
    }


}