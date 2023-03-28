package com.example.baocaoapi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.adapters.AdapterViewBindingAdapter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.baocaoapi.R;
import com.example.baocaoapi.model.SanPham;
import com.example.baocaoapi.model.Utils;
import com.example.baocaoapi.retrofit.ApiQuanLiSach;
import com.example.baocaoapi.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    private SanPham mSanPham;
    private ImageView imgItemImage;
    private ImageButton btnBack, btnEdit;
    private EditText edtItemName, edtItemSoluong, edtItemIntroduction;
    private AppCompatButton btnCancel, btnSave;
    private Spinner edtItemCategory;
    private LinearLayout ll_button;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiQuanLiSach apiQLS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        mSanPham = (SanPham) getIntent().getSerializableExtra("Item");
        initView();
        initData();
    }
    private void initView() {
        apiQLS = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiQuanLiSach.class);
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        ll_button = findViewById(R.id.ll_button);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        edtItemName = findViewById(R.id.edtItemName);
        imgItemImage = findViewById(R.id.imgItemImage);
        edtItemCategory = findViewById(R.id.edtItemCategory);
        edtItemSoluong = findViewById(R.id.edtSoLuong);
        edtItemIntroduction = findViewById(R.id.edtIntroduction);
        List<String> strTheLoai = new ArrayList<>();
        strTheLoai.add("Magazine");
        strTheLoai.add("Book");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, strTheLoai);
        edtItemCategory.setAdapter(adapter);
            for(int i = 0; i < strTheLoai.size(); i++) {
                if(strTheLoai.get(i).contentEquals(mSanPham.getTheLoai()))
                {
                    edtItemCategory.setSelection(i);
            }
        }

        setEditable(false);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditable(true);
                ll_button.setVisibility(View.VISIBLE);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_name = edtItemName.getText().toString().trim();
                String str_theloai = edtItemCategory.getSelectedItem().toString().trim();
                int SoLuong = Integer.parseInt(edtItemSoluong.getText().toString().trim());
                String str_gioithieu = edtItemIntroduction.getText().toString().trim();
                int id = mSanPham.getId();
                if(TextUtils.isEmpty(str_name) || TextUtils.isEmpty(str_theloai) || TextUtils.isEmpty(str_gioithieu)) {
                    Toast.makeText(ChiTietSanPhamActivity.this, "Insert the information", Toast.LENGTH_SHORT).show();
                } else {
                    compositeDisposable.add(apiQLS.updateSanPham(str_name, str_theloai, SoLuong, str_gioithieu,id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> {
                                        if (messageModel.isSuccess()) {
                                            Toast.makeText(ChiTietSanPhamActivity.this, "Sua thanh cong", Toast.LENGTH_SHORT).show();
                                            setEditable(false);
                                            ll_button.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(ChiTietSanPhamActivity.this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    throwable -> {
                                        Log.d("error", throwable.getMessage());
                                    }
                            ));
                }
            }
        });
    }

    private void initData() {
        edtItemName.setText(mSanPham.getTenSanPham());
//        edtItemCategory.setText(mSanPham.getTheLoai());
        edtItemSoluong.setText(String.valueOf(mSanPham.getSoLuong()));
        edtItemIntroduction.setText(mSanPham.getGioiThieu());
        if(mSanPham.getAnh().contains("http")) {
            Glide.with(ChiTietSanPhamActivity.this)
                    .load(mSanPham.getAnh())
                    .into(imgItemImage);
        } else {
            String hinh = Utils.BASE_URL + "images/" + mSanPham.getAnh();
            Glide.with(ChiTietSanPhamActivity.this)
                    .load(hinh)
                    .into(imgItemImage);
        }
    }
    private void setEditable(boolean editable) {
        edtItemName.setEnabled(editable);
        edtItemSoluong.setEnabled(editable);
        edtItemIntroduction.setEnabled(editable);
        edtItemCategory.setEnabled(editable);
    }
}