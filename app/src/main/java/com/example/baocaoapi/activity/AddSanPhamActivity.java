package com.example.baocaoapi.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.baocaoapi.R;
import com.example.baocaoapi.databinding.ActivityAddSanPhamBinding;
import com.example.baocaoapi.model.MessageModel;
import com.example.baocaoapi.model.Utils;
import com.example.baocaoapi.retrofit.ApiQuanLiSach;
import com.example.baocaoapi.retrofit.RetrofitClient;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSanPhamActivity extends AppCompatActivity {
    private ActivityAddSanPhamBinding binding;
    private Spinner spinnerTheLoai;
    private String TheLoai = "0";
    private ApiQuanLiSach apiQLS;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String mediaPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSanPhamBinding.inflate(getLayoutInflater());
        apiQLS = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiQuanLiSach.class);
        setContentView(binding.getRoot());
        initView();
        initData();
    }
    private void initView() {
        spinnerTheLoai = findViewById(R.id.spinnerTheLoai);
        List<String> strTheLoai = new ArrayList<>();
        strTheLoai.add("Choose the category");
        strTheLoai.add("Magazine");
        strTheLoai.add("Book");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, strTheLoai);
        spinnerTheLoai.setAdapter(adapter);
    }
    private void initData() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themsanpham();
                finish();
            }
        });

        binding.btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AddSanPhamActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = data.getDataString();
        uploadMultipleFiles();
        Log.d("done", "onActivityResult: " + mediaPath);
    }

    private String getPath(Uri uri) {
        String result;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if(cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

    private void themsanpham() {
        String TenSanPham = binding.edtName.getText().toString().trim();
        int SoLuong = Integer.parseInt(binding.edtSoLuong.getText().toString().trim());
        String GioiThieu = binding.edtIntroduction.getText().toString().trim();
        TheLoai = spinnerTheLoai.getSelectedItem().toString().trim();
        String hinhanh = binding.edtImage.getText().toString().trim();
        if(TextUtils.isEmpty(TenSanPham) || TextUtils.isEmpty(TheLoai) || TextUtils.isEmpty(GioiThieu)) {
            Toast.makeText(this, "enter again", Toast.LENGTH_SHORT).show();
        } else {
            compositeDisposable.add(apiQLS.insertSanPham(TenSanPham, TheLoai, SoLuong, GioiThieu, hinhanh)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        messageModel -> {
                            if(messageModel.isSuccess()) {
                                Toast.makeText(AddSanPhamActivity.this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddSanPhamActivity.this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(AddSanPhamActivity.this, "add " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    )
            )       ;
        }
    }

    private void uploadMultipleFiles() {
        Uri uri = Uri.parse(mediaPath);

        File file = new File(getPath(uri));
        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file1", file.getName(), requestBody1);
        Call<MessageModel> call = apiQLS.uploadFile(fileToUpload1);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call <MessageModel> call, Response<MessageModel> response) {
                MessageModel serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.isSuccess()) {
                        binding.edtImage.setText(serverResponse.getName());
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.v("Response", serverResponse.toString());
                }
            }
            @Override
            public void onFailure(Call <MessageModel> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }
}