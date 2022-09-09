package com.socialsirius.messenger.base;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingRegistrar;
import com.socialsirius.messenger.base.dagger.AppComponent;
import com.socialsirius.messenger.base.dagger.DaggerAppComponent;


public class App extends MultiDexApplication {

    private static App instance;
    private AppComponent appComponent;


    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return getInstance().getApplicationContext();
    }




    public static void initialize() {
        initializeDagger();
    //    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
     //   StrictMode.setVmPolicy(builder.build());

    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        initialize();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    String token = task.getResult();
                    Log.d("mylog2090","token="+token);
                }
            }
        });
       // db = AppDatabase.Companion.getDatabase(getContext());
    }


    private static void initializeDagger() {
       getInstance().appComponent = DaggerAppComponent
            .builder()
            .withApplication(getInstance())
            .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
