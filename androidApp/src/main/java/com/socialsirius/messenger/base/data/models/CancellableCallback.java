package com.socialsirius.messenger.base.data.models;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Igor on 17.10.2017.
 */

public class CancellableCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (!canceled) {
            callback.onResponse(call, response);
        }else{
            if (cancelCallback != null) {
                cancelCallback.onCancel();
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (!canceled) {
            callback.onFailure(call,t);
        }else{
            if (cancelCallback != null) {
                cancelCallback.onCancel();
            }
        }
    }

    public interface CancelCallback {
        public void onCancel();
    }

    public void setCancelCallback(CancelCallback cancelCallback) {
        this.cancelCallback = cancelCallback;
    }

    CancelCallback cancelCallback;

    public void setCallback(Callback<T> callback) {
        this.callback = callback;
    }

    private Callback<T> callback;

    private boolean canceled;

    public CancellableCallback() {
    }

    public CancellableCallback(Callback<T> callback, CancelCallback cancelCallback) {
        this.callback = callback;
        this.cancelCallback = cancelCallback;
        canceled = false;
    }

    public CancellableCallback(Callback<T> callback) {
        this.callback = callback;
        canceled = false;
    }


    public void cancel() {
        canceled = true;
        callback = null;
        if (cancelCallback != null) {
            cancelCallback.onCancel();
        }
    }


}
