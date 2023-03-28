package com.example.baocaoapi.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.baocaoapi.R;
import com.example.baocaoapi.adapter.SanPhamAdapter;
import com.example.baocaoapi.model.OnClickListener;
import com.example.baocaoapi.model.SanPham;
import com.example.baocaoapi.model.Utils;
import com.example.baocaoapi.retrofit.ApiQuanLiSach;
import com.example.baocaoapi.retrofit.RetrofitClient;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private EditText edtSearch;
    private RecyclerView rvListSearch;
    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void OnClickListener(int position) {
            Intent intent = new Intent(SearchActivity.this, ChiTietSanPhamActivity.class);
            intent.putExtra("Item", mSanPhams.get(position));
            startActivity(intent);
        }
    };
    private ArrayList<SanPham> mSanPhams = new ArrayList<>();
    private SanPhamAdapter spAdapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiQuanLiSach apiQLS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initData();
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
        edtSearch = findViewById(R.id.edtSearch);
        rvListSearch = findViewById(R.id.rvListSearch);
        rvListSearch.setLayoutManager(new LinearLayoutManager(this));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getDataSearch();
            }
        });
    }

    private void initData() {
        apiQLS = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiQuanLiSach.class);
    }

    private void getDataSearch() {
        String str_search = edtSearch.getText().toString().trim();
        compositeDisposable.add(apiQLS.getSearchedModel(str_search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if (sanPhamModel.isSuccess()) {
                                mSanPhams = sanPhamModel.getResult();
                                spAdapter = new SanPhamAdapter(this, mSanPhams, mOnClickListener);
                                rvListSearch.setAdapter(spAdapter);
                            }
                        },
                        throwable -> {
                            Log.d("erroe", throwable.getMessage());
                        }
                ));
    }
}
