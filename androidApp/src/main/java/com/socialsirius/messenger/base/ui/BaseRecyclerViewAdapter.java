package com.socialsirius.messenger.base.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import com.socialsirius.messenger.base.App;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by IGOR on 15.03.2017.
 */

public abstract class BaseRecyclerViewAdapter<T, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {


    public LayoutInflater inflater;

    public OnAdapterItemClick<T> getOnAdapterItemClick() {
        return onAdapterItemClick;
    }

    public OnAdapterViewClick<T> getOnAdapterViewClick() {
        return onAdapterViewClick;
    }

    public void setOnAdapterViewClick(OnAdapterViewClick<T> onAdapterItemClick) {
        this.onAdapterViewClick = onAdapterItemClick;
    }

    public void setOnAdapterItemClick(OnAdapterItemClick<T> onAdapterItemClick) {
        this.onAdapterItemClick = onAdapterItemClick;
    }

    OnAdapterItemClick<T> onAdapterItemClick;
    OnAdapterViewClick<T> onAdapterViewClick;


    protected BaseRecyclerViewAdapter() {
        this.inflater = (LayoutInflater) App.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    protected BaseRecyclerViewAdapter(Context context) {
        setContext(context);
    }



    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public void addToDataList(List<T> dataList) {
        if (this.dataList != null) {
            this.dataList.addAll(dataList);
        }

    }

    public void addToDataList(int position, List<T> dataList) {
        this.dataList.addAll(position, dataList);
    }

    public void removeFromDataItem(T data) {
        this.dataList.remove(data);
    }

    public void removeFromDataList(List<T> dataList) {
        this.dataList.removeAll(dataList);
    }

    public void addToDataItem(T data) {
        this.dataList.add(data);
    }

    public void addToSelectedDataList(List<T> dataList) {
        this.selectedDataList.addAll(dataList);
    }

    public void addToSelectedDataItem(T data) {
        this.selectedDataList.add(data);
    }

    public void removeFromSelectedDataItem(T data) {
        this.selectedDataList.remove(data);
    }

    public void removeFromSelectedDataList(List<T> dataList) {
        this.selectedDataList.removeAll(dataList);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> dataList = null;

    public void setSelectedDataList(List<T> selectedDataList) {
        this.selectedDataList = selectedDataList;
    }

    public List<T> getSelectedDataList() {
        return selectedDataList;
    }

    public List<T> selectedDataList = new ArrayList<>();


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) App.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (context != null) {
            if (context instanceof Activity) {
                setActivity((Activity) context);
            }
        }
    }


    Context context;

    public Activity getActivity() {
        return activity;
    }



    public BaseActivity getBaseActivity() {
        if (activity instanceof BaseActivity) {
            return (BaseActivity) activity;
        }
        return null;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    Activity activity;


    @Override
    public abstract H onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(H holder, int position);

    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        } else {
            return dataList.size();
        }

    }

    public T getItem(int position) {
        try {
            if (position < dataList.size()) {
                return dataList.get(position);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
