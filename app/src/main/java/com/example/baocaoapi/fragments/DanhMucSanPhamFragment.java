package com.example.baocaoapi.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.baocaoapi.R;
import com.example.baocaoapi.activity.ChiTietSanPhamActivity;
import com.example.baocaoapi.activity.SearchActivity;
import com.example.baocaoapi.adapter.SanPhamAdapter;
import com.example.baocaoapi.model.OnClickListener;
import com.example.baocaoapi.model.SanPham;
import com.example.baocaoapi.model.SanPhamModel;
import com.example.baocaoapi.model.Utils;
import com.example.baocaoapi.retrofit.ApiQuanLiSach;
import com.example.baocaoapi.retrofit.RetrofitClient;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DanhMucSanPhamFragment extends Fragment {
    private TabLayout mTabLayout;
    private RecyclerView rvListSanPham;
    private View mView;
    private ArrayList<SanPham> mSanPhams;
    private SanPhamAdapter spAdapter;
    private ImageButton btnSearch;
    private OnClickListener mOnClickLister = new OnClickListener() {
        @Override
        public void OnClickListener(int position) {
            Intent intent = new Intent(getActivity(), ChiTietSanPhamActivity.class);
            intent.putExtra("Item", mSanPhams.get(position));
            startActivity(intent);
        }
    };
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiQuanLiSach apiQLS;
    int page = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_danh_muc_san_pham, container, false);
        initView();
        return mView;
    }
    private void initView() {
        mTabLayout = mView.findViewById(R.id.mTabLayout);
        rvListSanPham = mView.findViewById(R.id.rvListSanPham);
        btnSearch = mView.findViewById(R.id.btnSearch);
        mSanPhams = new ArrayList<>();
        apiQLS = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiQuanLiSach.class);
        initData();

        mTabLayout.addTab(mTabLayout.newTab().setText("All"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Magazine"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Book"));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if ("All".equals(tab.getText())) {
                    initData();
                } else if("Magazine".equals(tab.getText())) {
                    compositeDisposable.clear();
                    compositeDisposable.add(apiQLS.getTheoLoai(page, "Magazine")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    sanPhamModel -> {
                                        if(sanPhamModel.isSuccess()) {
                                            mSanPhams = sanPhamModel.getResult();
                                            spAdapter = new SanPhamAdapter(getContext(), mSanPhams, mOnClickLister);
                                            rvListSanPham.setAdapter(spAdapter);
                                            rvListSanPham.setLayoutManager(new LinearLayoutManager(getContext()));
                                        } else {
                                            mSanPhams.clear();
                                            spAdapter.notifyDataSetChanged();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            )
                    );
                } else if("Book".equals(tab.getText())){
                    compositeDisposable.clear();
                    compositeDisposable.add(apiQLS.getTheoLoai(page, "Book")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    sanPhamModel -> {
                                        if(sanPhamModel.isSuccess()) {
                                            mSanPhams = sanPhamModel.getResult();
                                            spAdapter = new SanPhamAdapter(getContext(), mSanPhams, mOnClickLister);
                                            rvListSanPham.setAdapter(spAdapter);
                                            rvListSanPham.setLayoutManager(new LinearLayoutManager(getContext()));
                                        } else {
                                            mSanPhams.clear();
                                            spAdapter.notifyDataSetChanged();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            )
                    );
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = mSanPhams.get(viewHolder.getAdapterPosition()).getId();
                compositeDisposable.add(apiQLS.deleteSanPham(position)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                messageModel -> {
                                    if(messageModel.isSuccess()) {
                                        Toast.makeText(getActivity(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        initData();
                                    } else {
                                        Toast.makeText(getActivity(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        ));
            }
        }
        );
        itemTouchHelper.attachToRecyclerView(rvListSanPham);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
        compositeDisposable.add(apiQLS.getSanPhamModel()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if(sanPhamModel.isSuccess()) {
                                mSanPhams = sanPhamModel.getResult();
                                spAdapter = new SanPhamAdapter(getContext(), mSanPhams, mOnClickLister);
                                rvListSanPham.setAdapter(spAdapter);
                                rvListSanPham.setLayoutManager(new LinearLayoutManager(getContext()));
                            }
                        }
                ));
    }
}