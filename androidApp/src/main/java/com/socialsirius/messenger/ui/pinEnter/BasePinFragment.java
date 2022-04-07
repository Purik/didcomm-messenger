package com.socialsirius.messenger.ui.pinEnter;

import androidx.databinding.ViewDataBinding;

import com.socialsirius.messenger.base.ui.BaseFragment;
import com.socialsirius.messenger.base.ui.BaseViewModel;

public abstract class BasePinFragment<VB extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment<VB, VM> {
    public void setOnWalletOpenListener(OnWalletOpenListener onWalletOpenListener) {
        this.onWalletOpenListener = onWalletOpenListener;
    }

    public OnWalletOpenListener getOnWalletOpenListener() {
        return onWalletOpenListener;
    }

    OnWalletOpenListener onWalletOpenListener;

    public interface OnWalletOpenListener {
        void OnWalletOpen();
    }

}
