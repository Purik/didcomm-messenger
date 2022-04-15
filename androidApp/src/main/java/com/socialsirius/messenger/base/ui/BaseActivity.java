package com.socialsirius.messenger.base.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.socialsirius.messenger.design.BottomNavView;



import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.inject.Inject;

public abstract class BaseActivity<VB extends ViewDataBinding, M extends BaseActivityModel> extends AppCompatActivity {

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    public M model;

    boolean isBack = false;

    public VB getDataBinding() {
        return dataBinding;
    }

    VB dataBinding;

    public boolean isUseDataBinding() {
        return true;
    }

    public boolean isUseDefault() {
        return true;
    }


    @LayoutRes
    public abstract int getLayoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isUseDataBinding()) {
            dataBinding = DataBindingUtil.setContentView(this, getLayoutRes());
            dataBinding.setLifecycleOwner(this);
            //  model = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(M);
        } else if (isUseDefault()) {
            setContentView(getLayoutRes());
        }
        initDagger();
        initViewModel();
        setupViews();
        subscribe();
    }

    //FRAGMENTS
    @IdRes
    public int getRootFragmentId() {
        return 0;
    }

    public void pushPage(BaseFragment page) {
        pushPage(page, false);
    }

    public void pushPage(BaseFragment page, boolean withAnimation) {
        if (getRootFragmentId() == 0) {
            throw new IllegalArgumentException("Declare geRootFragmentId() to use this method");
        }
        page.setBack(true);
        isBack = true;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (withAnimation) {
            //  ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        }
        ft.addToBackStack(null).replace(getRootFragmentId(), page).commitAllowingStateLoss();
    }

    public void pushPage(BaseFragment page, boolean withAnimation, boolean isBack) {
        if (getRootFragmentId() == 0) {
            throw new IllegalArgumentException("Declare geRootFragmentId() to use this method");
        }
        page.setBack(isBack);
        this.isBack = isBack;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (withAnimation) {
            //  ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        }
        ft.addToBackStack(null).replace(getRootFragmentId(), page).commitAllowingStateLoss();
    }

    public void pushPageAdd(BaseFragment page) {
        pushPageAdd(page, false);
    }

    public void pushPageAdd(BaseFragment page, boolean withAnimation) {
        if (getRootFragmentId() == 0) {
            throw new IllegalArgumentException("Declare geRootFragmentId() to use this method");
        }
        page.setBack(true);
        isBack = true;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (withAnimation) {
            // ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        }
        ft.addToBackStack(null).add(getRootFragmentId(), page).commit();
    }

    public void showPage(BaseFragment page) {
        showPage(page, null, 0,0);
    }

    public void showPage(BaseFragment page, Bundle bundle, int enterAnimations, int exitAnimations) {
        if (getRootFragmentId() == 0) {
            throw new IllegalArgumentException("Declare geRootFragmentId() to use this method");
        }
        try {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        page.setBack(false);
        isBack = false;

            FragmentTransaction transaction =     getSupportFragmentManager().beginTransaction();
            if(enterAnimations !=0 && exitAnimations!=0){
                transaction.setCustomAnimations(enterAnimations,exitAnimations) ;
            }
        transaction.replace(getRootFragmentId(), page).commitAllowingStateLoss();
    }

    public void showPage(BaseFragment page, boolean isBack) {
        if (getRootFragmentId() == 0) {
            throw new IllegalArgumentException("Declare geRootFragmentId() to use this method");
        }
        try {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        page.setBack(isBack);
        this.isBack = isBack;
        getSupportFragmentManager().beginTransaction().replace(getRootFragmentId(), page).commitAllowingStateLoss();
    }

    public void popPage(BaseFragment page) {
        try {
            getSupportFragmentManager().popBackStackImmediate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pushPage(page);
        // page.setBack(false);
        // page.setPageManager(getPageManager());
        //getSupportFragmentManager().beginTransaction().replace(getRootFragmentId(), page).commit();
    }

    public boolean isBack() {
        List<Fragment> listFragment = getSupportFragmentManager().getFragments();
        if (listFragment != null) {
            if (listFragment.size() > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void popPage() {
        onBackPressed();
    }

    //Send  permisions, result  inside Fragments(NEED or NO?)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<Fragment> listFragment = getSupportFragmentManager().getFragments();
        if (listFragment != null) {
            for (Fragment fragmnet : listFragment) {
                fragmnet.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        List<Fragment> listFragment = getSupportFragmentManager().getFragments();
        if (listFragment != null) {
            for (Fragment fragmnet : listFragment) {
                fragmnet.onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onFragmentResult(int requestCode, int resultCode, @Nullable Intent data) {
        List<Fragment> listFragment = getSupportFragmentManager().getFragments();
        if (listFragment != null) {
            for (Fragment fragmnet : listFragment) {
                if (fragmnet instanceof BaseFragment) {
                    ((BaseFragment) fragmnet).onFragmentResult(requestCode, resultCode, data);
                }
            }
        }
    }

    public boolean isCustomBackEnabled = false;

    public CustomBackListener customBackListener;
    public  interface  CustomBackListener{
      public void  onBackPressed();
    }
    public void onBack() {
        if (isCustomBackEnabled) {
            if(customBackListener!=null){
                customBackListener.onBackPressed();
                return;
            }
        }
        try {
            getSupportFragmentManager().getFragments()
                    .get(getSupportFragmentManager().getBackStackEntryCount() - 1).onResume();
        } catch (Exception e) {
        }
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void initViewModel() {
        Class<M> vmClass = (Class<M>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        model = new ViewModelProvider(this, viewModelFactory).get(vmClass);
        model.onViewCreated();
    }


    boolean isBottomNavigationEnabled = true;
    public boolean isBottomNavigationEnabled() {
        return isBottomNavigationEnabled;
    }

    public  void setBottomNavigationEnabled(boolean isBottomNavigationEnabled) {
         this.isBottomNavigationEnabled = isBottomNavigationEnabled;
    }

    public void subscribe() {
        if (!isBottomNavigationEnabled()) {
            return;
        }
        model.getBottomNavClick().observe(this, new Observer<BottomNavView.BottomTab>() {
            @Override
            public void onChanged(BottomNavView.BottomTab bottomTab) {
                switch (bottomTab) {
                    case Contacts:
                    //    showPage(new ContactsFragment());
                        break;
                    case Menu:
                    //    showPage(new MenuFragment());
                        break;
                    case Credentials:
                  //      showPage(new CredentialsFragment());
                        break;
                }
            }
        });


    }

    public void openTab(BottomNavView.BottomTab tab) {
        model.getSelectedTab().postValue(tab);
    }

    public abstract void initDagger();


    @Override
    public void onDestroy() {
        model.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        model.onResume();
    }

    public void setupViews(){
        if(model!=null){
            model.setupViews();
        }
    }
}
