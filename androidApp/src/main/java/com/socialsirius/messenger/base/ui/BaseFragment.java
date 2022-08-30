package com.socialsirius.messenger.base.ui;


import android.content.Intent;

import android.os.Bundle;
import android.speech.RecognizerIntent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.google.android.material.appbar.AppBarLayout;


import com.socialsirius.messenger.R;
import com.socialsirius.messenger.base.App;
import com.socialsirius.messenger.design.dialogs.ProgressDialog;
import com.socialsirius.messenger.ui.activities.main.MainActivity;


import java.lang.reflect.ParameterizedType;
import java.util.List;



import org.jetbrains.annotations.NotNull;

import kotlin.Pair;

import static android.app.Activity.RESULT_OK;

import javax.inject.Inject;

public abstract class BaseFragment<VB extends ViewDataBinding, VM extends BaseViewModel> extends Fragment {

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    protected VM model;

    public View rootView;
    public LayoutInflater inflater;
    private boolean isBack;
    private ProgressDialog progressDialog;

    VB dataBinding;

    public VB getDataBinding() {
        return dataBinding;
    }

    public boolean isUseDataBinding() {
        return true;
    }

    public boolean isBack() {
        return isBack;
    }

    public void setBack(boolean back) {
        isBack = back;
    }

    @LayoutRes
    public abstract int getLayoutRes();

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onPause() {
        model.onPause();
        super.onPause();
    }

    public void setupViews() {
        if(model !=null){
            model.setupViews();
        }
    }

    private static final int SPEECH_REQUEST_CODE = 101;

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// This starts the activity and populates the intent with the speech text.
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public int getBackIcon() {
        return R.drawable.ic_arrow_back;
    }

    public boolean isShowLogo() {
        return false;
    }

    public boolean isShowSearch() {
        return true;
    }


    public void setupToolbar() {
      //  LinearLayout toolBar = rootView.findViewById(R.id.toolbar);
      //  TextView toolbarTitle = rootView.findViewById(R.id.toolbar_title);
       // ImageView toolbarBackImage = rootView.findViewById(R.id.toolbar_left);
      //  ImageView logoBar = rootView.findViewById(R.id.appBarLogo);
        LinearLayout toolBar = null;
        TextView toolbarTitle = null;
        ImageView toolbarBackImage =  null;
        ImageView logoBar =  null;


    //    AppBarLayout appBarLayout = rootView.findViewById(R.id.appBar);
        AppBarLayout appBarLayout = null;

        model.isHideActionBarLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isHide) {
                if (appBarLayout == null) {
                    return;
                }
                if (isHide) {
                    appBarLayout.setVisibility(View.GONE);
                } else {
                    appBarLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        if (logoBar != null && toolBar != null) {
            if (isShowLogo()) {
                logoBar.setVisibility(View.VISIBLE);
                toolBar.setVisibility(View.GONE);
            } else {
                logoBar.setVisibility(View.GONE);
                toolBar.setVisibility(View.VISIBLE);
            }
        }


        if (toolbarBackImage != null) {
            toolbarBackImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.getOnBackClickLiveData().postValue(true);
                }
            });
            toolbarBackImage.setImageResource(getBackIcon());
            if (isBack()) {
                toolbarBackImage.setVisibility(View.VISIBLE);
            } else {
                toolbarBackImage.setVisibility(View.GONE);
            }

        }

        model.getTitleLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (toolbarTitle != null) {
                    toolbarTitle.setText(s);
                }
            }
        });


        model.getOnBackClickLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    onBackPressed();
                }
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        try {
            rootView = inflater.inflate(getLayoutRes(), null, false);
            if (isUseDataBinding()) {
                dataBinding = DataBindingUtil.bind(rootView);
                dataBinding.setLifecycleOwner(this);
            }
            initDagger();
            initViewModel();
            setModel();
            model.onCreateview();
            setupViews();
            baseSubscribe();
            subscribe();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return rootView;
    }

    private void initViewModel() {
        Class<VM> vmClass = (Class<VM>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        model = new ViewModelProvider(this, viewModelFactory).get(vmClass);
    }

    public abstract void subscribe();

    public abstract void setModel();

    /**
     * add inside for fragment with viewModel "App.getInstance().getAppComponent().inject(this);"
     */
    public abstract void initDagger();

    public void onFragmentResult(int requestCode, int resultCode, Intent data) {

    }

    public void baseSubscribe(){
        model.getOnShowToastLiveData().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        });

        model.getOnShowAlertDialogLiveData().observe(getViewLifecycleOwner(), new Observer<Pair<String, String>>() {
            @Override
            public void onChanged(Pair<String, String> stringStringPair) {
                if (stringStringPair != null) {
                    model.getOnShowAlertDialogLiveData().setValue(null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(stringStringPair.getSecond());
                    builder.setTitle(stringStringPair.getFirst());
                    builder.setPositiveButton(R.string.ok_button, null);
                    builder.show();
                }
            }
        });

        model.getOnShowProgressLiveData().observe(getViewLifecycleOwner(), show -> {
            if (show) {
                showProgressDialog();
            } else hideProgressDialog();
        });

        model.getFinishActivityLiveData().observe(getViewLifecycleOwner(), isFinish -> {
            if (isFinish) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        model.getOpenTabLiveData().observe(getViewLifecycleOwner(), bottomTab -> {
            if (bottomTab != null) {
                getBaseActivity().openTab(bottomTab);
                model.getOpenTabLiveData().postValue(null);
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model.onViewCreated();

        subscribe();

    }

    @Override
    public void onDestroy() {
        model.onDestroy();
        super.onDestroy();
    }


    public void showProgressDialog() {
        hideProgressDialog();
        try {
            progressDialog = new ProgressDialog(requireContext(), App.getContext().getResources().getString(R.string.please_wait));
          //  progressDialog.setTitle(R.string.please_wait);
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        getBaseActivity().onBackPressed();
    }


    boolean isBottomNavigationEnabled = true;


    public boolean isBottomNavigationEnabled() {
        return isBottomNavigationEnabled;
    }

    public void setupBottomNavigationView() {
        if (getActivity() != null) {

            if(getActivity() instanceof MainActivity){
              //  ((MainActivity) getActivity()).setBottomNavigationEnabled(isBottomNavigationEnabled());
            }
        }
    }


 /*   public AppBarLayout getAppBarLayout() {
        if (getActivity() != null) {
            return ((BaseActivity) getActivity()).getAppBarLayout();
        }
        return null;
    }*/


    public void setupUnauthBottomBar() {
       // boolean isVisible = model.isVisibleUnauthBottomBar() && !AppPref.getInstance().isLoggedIn();
        boolean isVisible = false;
        getBaseActivity().model.isVisibleUnauthBottomBar().postValue(new Pair<Boolean, Boolean>(isVisible, false));
    }

    @Override
    public void onResume() {
        //  getMainActivity().setOnBackPressedListener(getOnBackPressedListener());
        super.onResume();
        setupToolbar();
        setupUnauthBottomBar();
        setupBottomNavigationView();
        model.onResume();
    }

}
