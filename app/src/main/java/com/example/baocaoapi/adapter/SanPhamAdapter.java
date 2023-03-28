package com.example.baocaoapi.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baocaoapi.R;
import com.example.baocaoapi.model.OnClickListener;
import com.example.baocaoapi.model.SanPham;
import com.example.baocaoapi.model.Utils;

import java.util.ArrayList;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder> {
    private Context context;
    private ArrayList<SanPham> mSanPhams;
    private LayoutInflater layoutInflater;
    private OnClickListener mOnClickListener;

    public SanPhamAdapter(Context context, ArrayList<SanPham> mSanPhams, OnClickListener mOnClickListener) {
        this.context = context;
        this.mSanPhams = mSanPhams;
        this.mOnClickListener = mOnClickListener;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_sanpham, parent, false);
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        SanPham mSanPham = mSanPhams.get(position);
        holder.txtTenSanPham.setText(mSanPham.getTenSanPham());
        holder.txtGioiThieu.setText(mSanPham.getGioiThieu());
        if(mSanPham.getAnh().contains("http")) {
            Glide.with(context)
                    .load(mSanPham.getAnh())
                    .into(holder.imgSanPham);
        } else {
            String hinh = Utils.BASE_URL + "images/" + mSanPham.getAnh();
            Glide.with(context)
                    .load(hinh)
                    .into(holder.imgSanPham);
        }

    }

    @Override
    public int getItemCount() {
        if(mSanPhams.size() != 0) return mSanPhams.size();
        return 0;
    }

    public class SanPhamViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgSanPham;
        private TextView txtTenSanPham, txtGioiThieu;
        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            txtTenSanPham = itemView.findViewById(R.id.txtTenSanPham);
            txtGioiThieu = itemView.findViewById(R.id.txtGioiThieu);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.OnClickListener(getAdapterPosition());
                }
            });
        }
    }
}
