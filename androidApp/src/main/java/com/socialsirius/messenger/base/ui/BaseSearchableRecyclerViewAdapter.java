package com.socialsirius.messenger.base.ui;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IGOR on 15.03.2017.
 */

public abstract class BaseSearchableRecyclerViewAdapter<T, H extends RecyclerView.ViewHolder> extends BaseRecyclerViewAdapter<T, H> {
    public List<T> getDataOriginal() {
        return dataOriginal;
    }

    public List<T> dataOriginal = new ArrayList<>();
    public String search;



    @Override
    public void setDataList(List<T> dataList) {
        super.setDataList(dataList);
        dataOriginal = new ArrayList<>(dataList);
    }
    protected BaseSearchableRecyclerViewAdapter() {
        super();
    }
    protected BaseSearchableRecyclerViewAdapter(Context context) {
        super(context);
    }





    @Override
    public abstract H onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);


}
