package com.example.badrjobs.Utils;

import com.example.badrjobs.Model.ModelHousingRequest;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public class Api {
    public static String ROOT_URL = "https://developersite1.com/admin/";

    //registration
    public interface RetrofitRegister {
        @POST("api/v1/auth/signUp")
        Call<String> putParam(@Body HashMap<String, String> param);
    }
    //FixedAvailability
    public interface RetrofitFixedAvailability {
        @GET("api/v1/auth/isFixNameAvalible")
        Call<String> putParam(@QueryMap HashMap<String, String> param);
    }

    //login
    public interface RetrofitLogin {
        @POST("api/v1/auth/signIn")
        Call<String> putParam(@Body HashMap<String, String> param);
    }
    //settings
    public interface RetrofitSettings {
        @GET("api/v1/settings")
        Call<String> putParam();
    }
    //logOut
    public interface RetrofitLogOut {
        @POST("api/v1/auth/logOut")
        Call<String> putParam();
    }
    //update profile
    public interface RetrofitUpdateProfile {
        @PATCH("api/v1/updateMyProfile")
        Call<String> putParam(@Body HashMap<String, String> param);
    }
    //report job
    public interface RetrofitReportJob {
        @POST("api/v1/reportJob")
        Call<String> putParam(@Body HashMap<String, String> param);
    }
    //get my profile
    public interface RetrofitGetMyProfile {
        @GET("api/v1/profile")
        Call<String> putParam();
    }
    //get nationality
    public interface RetrofitGetNationality {
        @GET("api/v1/nationality")
        Call<String> putParam();
    }
    //get prevContract
    public interface RetrofitGetPrevContracts {
        @GET("api/v1/prevContract")
        Call<String> putParam();
    }
    //add job
    public interface RetrofitAddJob {
        @POST("api/v1/job")
        Call<String> putParam(@Body HashMap<String,String> param);
    }

    //country
    public interface RetrofitGetMainCountry {
        @GET("api/v1/countries")
        Call<String> putParam();
    }

    //favorite
    public interface RetrofitGetFavorite {
        @GET("api/v1/favorite")
        Call<String> putParam();
    }
    //setting
    public interface RetrofitGetSetting {
        @GET("api/v1/settings")
        Call<String> putParam();
    }
    //notification
    public interface RetrofitGetNotification {
        @GET("api/v1/notifactions")
        Call<String> putParam();
    }

    //get category
    public interface RetrofitGetCategory {
        @GET("api/v1/jobCategory")
        Call<String> putParam(@QueryMap HashMap<String,String> hashMap);
    }
    //get category
    public interface RetrofitConfirmContract {
        @GET("api/v1/confirmContract")
        Call<String> putParam(@QueryMap HashMap<String,String> hashMap);
    }
    //del account
    public interface RetrofitDeleteAccount {
        @GET("api/v1/profile")
        Call<String> putParam(@QueryMap HashMap<String,String> hashMap);
    }
    //get sub category
    public interface RetrofitGetSubCategory {
        @GET("api/v1/jobSubCategory")
        Call<String> putParam(@QueryMap HashMap<String,String> hashMap);
    }
    //get jobs
    public interface RetrofitGetJobs {
        @GET("api/v1/job")
        Call<String> putParam(@QueryMap HashMap<String,String> hashMap);
    }

    public interface RetrofitSignContract {
        @PUT("api/v1/contract/{contractId}")
        Call<String> putParam(@Path("contractId") String search,@Body HashMap<String, String> param);
    }
    //show some user profile
    public interface RetrofitShowProfileOther {
        @GET("api/v1/user/{userId}")
        Call<String> putParam(@Path("userId") String userId);
    }

    public interface RetrofitContractDetails {
        @GET("api/v1/contractDetails/{contractId}")
        Call<String> putParam(@Path("contractId") String search);
    }
    public interface RetrofitGetJobDetails {
        @GET("api/v1/job/{JobId}")
        Call<String> putParam(@Path("JobId") String search);
    }

    public interface RetrofitCreateContract {
        @POST("api/v1/contract")
        Call<String> putParam(@Body HashMap<String, String> param);
    }


    public interface RetrofitAddToFavorite {
        @POST("api/v1/favorite")
        Call<String> putParam(@Body HashMap<String, String> param);
    }
    public interface RetrofitDoActivation {
        @POST("api/v1/jobActivation")
        Call<String> putParam(@Body HashMap<String, String> param);
    }

    //blacklist
    public interface RetrofitBlockSomeone {
        @POST("api/v1/blackList")
        Call<String> putParam(@Body HashMap<String, String> param);
    }
    public interface RetrofitGetBlockedList {
        @GET("api/v1/blackList")
        Call<String> putParam();
    }
    public interface RetrofitUnBlockUser {
        @PUT("api/v1/unBlackList/{userId}")
        Call<String> putParam(@Path("userId") String search);
    }

    //housing
    public interface RetrofitHousingList {
        @GET("api/v1/housingTypes")
        Call<String> putParam();
    }



    //city
    public interface RetrofitCity {
        @GET("api/cities")
        Call<String> putParam(@QueryMap HashMap<String, String> params);
    }
    //add estate
    public interface RetrofitAddEstate {
        @POST("api/add-realestate")
        Call<String> putParam(@Body HashMap<String, String> params);
    }
    //add car
    public interface RetrofitAddCar {
        @POST("api/add-car")
        Call<String> putParam(@Body HashMap<String, String> params);
    }
    //car companies
    public interface RetrofitCarCompanies {
        @GET("api/companies")
        Call<String> putParam();
    }
    //get popular
    public interface RetrofitGetPopular {
        @GET("api/ads")
        Call<String> putParam(@QueryMap HashMap<String, String> params);
    }
    //get all
    public interface RetrofitGetAll{
        @GET("api/ads")
        Call<String> putParam();
    }
    //get cars or estate
    public interface RetrofitGetCarsOrEstate {
        @GET("api/ads")
        Call<String> putParam(@QueryMap HashMap<String, String> params);
    }
    //get cars details
    public interface RetrofitGetCarDetails {
        @GET("api/car")
        Call<String> putParam(@QueryMap HashMap<String, String> params);
    }
    //get estate details
    public interface RetrofitGetEstateDetails {
        @GET("api/realestate")
        Call<String> putParam(@QueryMap HashMap<String, String> params);
    }

    //get my ads
    public interface RetrofitMyAds {
        @GET("api/my-ads")
        Call<String> putParam(@QueryMap HashMap<String, String> params);
    }
    //get profile
    public interface RetrofitGetProfile {
        @GET("api/account-details")
        Call<String> putParam();
    }
    //search
    public interface RetrofitSearch {
        @GET("api/search")
        Call<String> putParam(@QueryMap HashMap<String, String> params);
    }







}
