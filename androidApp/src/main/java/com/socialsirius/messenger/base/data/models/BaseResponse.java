package com.socialsirius.messenger.base.data.models;

import androidx.annotation.NonNull;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseResponse<T> {


    boolean ok;
    int count;
    String previous;
    String next;
    String hash;
    String comment;
    @SerializedName(value = "push", alternate = {"data"})
    List<T> items;
    @SerializedName(value = "notifications", alternate = {"portfolio", "recognition","mam"})
    T item;

    public int getCount() {
        return count;
    }
    public boolean isOk() {
        return ok;
    }
    public String getNext() {
        return next;
    }
    public String getComment() {
        return comment;
    }
    public T getItem() {
        return item;
    }
    public List<T> getItems() { return items; }
    public String getPrevious() { return previous; }
    public String getHash() { return hash; }

    @NonNull
    @Override
    public String toString() {
        return "hash="+hash +"  getItems()="+ getItems();
    }
}
