package com.example.baocaoapi.model;

import java.util.ArrayList;
import java.util.List;

public class SanPhamModel {
    boolean success;
    String message;
    ArrayList<SanPham> result;
    public ArrayList<SanPham> getResult() {
        return result;
    }

    public void setResult(ArrayList<SanPham> result) {
        this.result = result;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
