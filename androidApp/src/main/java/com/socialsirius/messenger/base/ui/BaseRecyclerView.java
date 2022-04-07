package com.socialsirius.messenger.base.ui;


import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;


public abstract class BaseRecyclerView<VB extends ViewDataBinding, BM > {


    public BaseRecyclerView() {

    }

    public boolean isUseDataBinding() {
        return true;
    }

    public VB getDataBinding() {
        return dataBinding;
    }


    VB dataBinding;

    public abstract int getViewType();

    @LayoutRes
    public abstract int getLayoutRes();


    public void bindDataBinding(View view) {
        dataBinding = DataBindingUtil.bind(view);
    }

    public abstract void onBind(View view, BM model, int position);
}
