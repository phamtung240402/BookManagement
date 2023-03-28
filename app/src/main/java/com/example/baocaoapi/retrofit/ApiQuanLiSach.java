package com.example.baocaoapi.retrofit;


import com.example.baocaoapi.model.MessageModel;
import com.example.baocaoapi.model.SanPham;
import com.example.baocaoapi.model.SanPhamModel;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiQuanLiSach {
    @GET("getSanp.php")
    Observable<SanPhamModel> getSanPhamModel();

    @FormUrlEncoded
    @POST("getTheoLoai.php")
    Observable<SanPhamModel> getTheoLoai(
            @Field("page") int page,
            @Field("TheLoai") String loai
    );

    @FormUrlEncoded
    @POST("search.php")
    Observable<SanPhamModel> getSearchedModel(
            @Field("search") String search
    );

    @FormUrlEncoded
    @POST("insert.php")
    Observable<MessageModel> insertSanPham(
            @Field("TenSanPham") String TenSanPham,
            @Field("TheLoai") String TheLoai,
            @Field("SoLuong") int SoLuong,
            @Field("GioiThieu") String GioiThieu,
            @Field("Anh") String Anh
    );

    @Multipart
    @POST("uploadImage.php")
    Call<MessageModel> uploadFile(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("update.php")
    Observable<MessageModel> updateSanPham(
            @Field("TenSanPham") String TenSanPham,
            @Field("TheLoai") String TheLoai,
            @Field("SoLuong") int SoLuong,
            @Field("GioiThieu") String GioiThieu,
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("delete.php")
    Observable<MessageModel> deleteSanPham(
            @Field("id") int id
    );
}
