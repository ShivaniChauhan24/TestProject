package com.shipgig.thegun.pos.utilities;
import com.shipgig.thegun.pos.model.FogetPasswordData;
import com.shipgig.thegun.pos.inventoryAPI.SendInventoryData;
import com.shipgig.thegun.pos.inventoryAPI.StockResponse;
import com.shipgig.thegun.pos.model.ForgotPassModel;
import com.shipgig.thegun.pos.model.LoginResponseModel;
import com.shipgig.thegun.pos.model.ModelChangePassword;
import com.shipgig.thegun.pos.model.SendMailModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Afroz Ahmad on 12/9/19.
 */
public interface Api {

    @POST("payuMoney_php")
    Call<String>getHashToken(@Body GetHashModel hashModel);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("login")
    Call<LoginResponseModel>userLogin(@Header("user") String username);

    @Multipart
    @POST("uploadFile")
    Call<SendMailModel>sendMail(@Part MultipartBody.Part file,
                                 @Part("email") RequestBody email,
                                 @Part("total_amount")RequestBody totalamount,
                                 @Part("name") RequestBody name);
    @FormUrlEncoded
    @POST("forgotPassward")
    Call<ForgotPassModel>forgotPassword(@Field("email") String email);


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @POST("resetPassword")
    Call<ModelChangePassword>changePassword(@Body FogetPasswordData jsonObject);


    @Headers({
            "Content-Type: application/json"
    })
    @POST("stockReport")
    Call<StockResponse>stockReport(@Header("token") String token, @Body SendInventoryData hashModel);

}




