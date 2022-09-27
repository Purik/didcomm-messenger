package com.socialsirius.messenger.base.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnProtocolMessage;
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnRequest;
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation;
import com.sirius.library.mobile.SiriusSDK;
import com.sirius.library.mobile.helpers.ScenarioHelper;
import com.socialsirius.messenger.R;
import com.socialsirius.messenger.base.App;
import com.socialsirius.messenger.databinding.ViewErrorBootomSheetBinding;
import com.socialsirius.messenger.databinding.ViewInvitationBootomSheetBinding;
import com.socialsirius.messenger.design.BottomNavView;
import com.socialsirius.messenger.design.InvitationBottomSheet;
import com.socialsirius.messenger.models.Chats;
import com.socialsirius.messenger.sirius_sdk_impl.SDKUseCase;
import com.socialsirius.messenger.ui.activities.main.MainActivity;
import com.socialsirius.messenger.ui.activities.message.MessageActivity;


import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import kotlin.Pair;

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
        showPage(page, null, 0, 0);
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

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (enterAnimations != 0 && exitAnimations != 0) {
            transaction.setCustomAnimations(enterAnimations, exitAnimations);
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

    public interface CustomBackListener {
        public void onBackPressed();
    }

    public void onBack() {
        if (isCustomBackEnabled) {
            if (customBackListener != null) {
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

    public void setBottomNavigationEnabled(boolean isBottomNavigationEnabled) {
        this.isBottomNavigationEnabled = isBottomNavigationEnabled;
    }


    public void showConnectionSuccess(String id) {
        //   model.getMessageRepository().getItemBy(id);
        if (id != null) {
          /*  model.getMessageRepository().getInvitationSuccessLiveData.value = null
            model.isConnectionInvit = true
            val item = model.getMessage(it)
            dialog?.cancel()
            baseActivity.finish()
            MessageActivity.newInstance(requireContext(), item)*/

        }
    }


    public void showConnectedSheet() {
        InvitationBottomSheet invitationSheet = new InvitationBottomSheet(this);
        View view = LayoutInflater.from(this).inflate(R.layout.view_invitation_bootom_sheet, null, false);
        ViewInvitationBootomSheetBinding binding = DataBindingUtil.bind(view);
        binding.invitationTitle.setText("Connection with %s created");
    //    binding.labelText.setText(connRequest.getLabel());
     //   binding.receipentLabel.setText(String.format(App.getContext().getString(R.string.recipient_keys), connRequest.getLabel()));
    /*    try {
            ConnProtocolMessage.ExtractTheirInfoRes theirInfo = connRequest.extractTheirInfo();
            binding.receipentKeyText.setText(theirInfo.getVerkey());
            binding.endpointText.setText(theirInfo.getEndpoint());
            String myEndpoint = SiriusSDK.INSTANCE.getContext().getEndpointAddressWithEmptyRoutingKeys();
            if (Objects.equals(theirInfo.getEndpoint(), myEndpoint)) {
                binding.connectButton.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
/*        binding.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScenarioHelper.INSTANCE.acceptScenario(SDKUseCase.Scenario.PersistentInvitation.getName(), connRequest.getId(),
                        null, null);
                invitationSheet.dismiss();
            }
        });*/
    /*    binding.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.receipentLabel.setVisibility(View.VISIBLE);
                binding.receipentKeyText.setVisibility(View.VISIBLE);
                //    binding?.endpointLabel?.visibility = View.VISIBLE
                //    binding?.endpointText?.visibility = View.VISIBLE
                binding.moreBtn.setVisibility(View.GONE);
            }
        });*/

        invitationSheet.setContentView(view);
        invitationSheet.show();
        invitationSheet.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //mScannerView?.resumeCameraPreview(this)
            }
        });
    }

    public void showInvitationSheet(ConnRequest connRequest) {
        InvitationBottomSheet invitationSheet = new InvitationBottomSheet(this);
        View view = LayoutInflater.from(this).inflate(R.layout.view_invitation_bootom_sheet, null, false);
        ViewInvitationBootomSheetBinding binding = DataBindingUtil.bind(view);
        binding.invitationTitle.setText("Request for connection from:");
        binding.labelText.setText(connRequest.getLabel());
        binding.receipentLabel.setText(String.format(App.getContext().getString(R.string.recipient_keys), connRequest.getLabel()));
        try {
            ConnProtocolMessage.ExtractTheirInfoRes theirInfo = connRequest.extractTheirInfo();
            binding.receipentKeyText.setText(theirInfo.getVerkey());
            binding.endpointText.setText(theirInfo.getEndpoint());
            String myEndpoint = SiriusSDK.INSTANCE.getContext().getEndpointAddressWithEmptyRoutingKeys();
            if (Objects.equals(theirInfo.getEndpoint(), myEndpoint)) {
                binding.connectButton.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScenarioHelper.INSTANCE.acceptScenario(SDKUseCase.Scenario.PersistentInvitation.getName(), connRequest.getId(),
                        null, null);
                invitationSheet.dismiss();
            }
        });
        binding.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.receipentLabel.setVisibility(View.VISIBLE);
                binding.receipentKeyText.setVisibility(View.VISIBLE);
                //    binding?.endpointLabel?.visibility = View.VISIBLE
                //    binding?.endpointText?.visibility = View.VISIBLE
                binding.moreBtn.setVisibility(View.GONE);
            }
        });

        invitationSheet.setContentView(view);
        invitationSheet.show();
        invitationSheet.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //mScannerView?.resumeCameraPreview(this)
            }
        });
    }

    public void subscribe() {
        /*if (!isBottomNavigationEnabled()) {
            return;
        }*/
        model.getInvitationStartLiveData().observe(this, new Observer<ConnRequest>() {
            @Override
            public void onChanged(ConnRequest connRequest) {
                if (connRequest != null) {
                    model.getInvitationStartLiveData().setValue(null);
                    showInvitationSheet(connRequest);
                }

            }
        });

        model.getInvitationSuccessLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    model.getInvitationSuccessLiveData().setValue(null);
                    if(dialog!=null){
                        dialog.cancel();
                    }

                    if(model.isConnecting()){
                        Chats item = model.getMessage(s);
                        if(BaseActivity.this instanceof MainActivity){
                            MessageActivity.Companion.newInstance( BaseActivity.this, item);
                        }else{
                            BaseActivity.this.finish();
                            MessageActivity.Companion.newInstance( BaseActivity.this, item);
                        }
                    }else{
                        showConnectedSheet();
                         // showInvitationSheet(connRequest);
                    }

                    model.setConnecting(false);

                }

            }
        });

    /*    model.invitationSuccessLiveData.observe(this, Observer {
            if (it != null) {
                model.invitationSuccessLiveData.value = null
                model.isConnectionInvit = true
                val item = model.getMessage(it)
                dialog?.cancel()
                baseActivity.finish()
                MessageActivity.newInstance(requireContext(), item)
                //   popPage(ChatsFragment.newInstance(item))
            }
        })*/

        model.getInvitationStartInviteeLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    model.getInvitationStartInviteeLiveData().setValue(null);
                    model.acceptInvitation(s);
                    model.startInvitationTimeout();
                    openDialog();
                }
            }
        });

        model.getInvitationErrorLiveData().observe(this, new Observer<Pair<Boolean, String>>() {
            @Override
            public void onChanged(Pair<Boolean, String> booleanStringPair) {
                if(booleanStringPair!=null){
                    model.getInvitationErrorLiveData().setValue(null);
                    model.getShowErrorBottomSheetLiveData().postValue(booleanStringPair.getSecond());
                }
            }
        });


        model.getShowInvitationBottomSheetLiveData().observe(this, new Observer<Invitation>() {
            @Override
            public void onChanged(Invitation invitation) {
                if (invitation != null) {
                    // mScannerView ?.stopCameraPreview()
                    model.getShowInvitationBottomSheetLiveData().setValue(null);
                    showInvitationSheet(invitation);
                }
            }
        });

        model.getShowErrorBottomSheetLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (s != null) {
                    model.setConnecting(false);
                    if(dialog!=null){
                        dialog.cancel();
                    }
                    model.getShowErrorBottomSheetLiveData().setValue(null);
                    //  mScannerView ?.stopCameraPreview()
                    showErrorSheet(s);
                }
            }
        });

 /*
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
        });*/


    }

    AlertDialog dialog = null;

    public void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Connecting...");
        builder.setMessage("Please wait,secure connection is being established");
        builder.setCancelable(false);
        dialog = builder.show();
    }

    public void showErrorSheet(String text) {
        BottomSheetDialog invitationSheet = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.view_error_bootom_sheet, null, false);
        ViewErrorBootomSheetBinding binding = DataBindingUtil.bind(view);
        binding.errorText.setText(text);
        binding.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invitationSheet.dismiss();
            }
        });

        invitationSheet.setContentView(view);
        invitationSheet.show();
        invitationSheet.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
             //   mScannerView ?.resumeCameraPreview(this);
            }
        });

    }

    public void showInvitationSheet(Invitation invitation) {
        InvitationBottomSheet invitationSheet = new InvitationBottomSheet(this);
        View view = getLayoutInflater().inflate(R.layout.view_invitation_bootom_sheet, null, false);
        ViewInvitationBootomSheetBinding binding = DataBindingUtil.bind(view);
        binding.labelText.setText(invitation.label());
        binding.receipentLabel.setText(String.format(App.getContext().getString(R.string.recipient_keys), invitation.label()));
        if (Objects.equals(invitation.endpoint(), model.getMyEndpoint())) {
            binding.connectButton.setVisibility(View.GONE);
        }
        binding.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.connectToInvitation(invitation);
            }
        });
        binding.moreBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.receipentLabel.setVisibility(View.VISIBLE);
                        binding.receipentKeyText.setVisibility(View.VISIBLE);
                        //    binding?.endpointLabel?.visibility = View.VISIBLE
                        //    binding?.endpointText?.visibility = View.VISIBLE
                        binding.moreBtn.setVisibility(View.GONE);
                    }
                }
        );
        invitationSheet.setContentView(view);
        invitationSheet.show();
        invitationSheet.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
             //   mScannerView ?.resumeCameraPreview(this);
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

    public void setupViews() {
        if (model != null) {
            model.setupViews();
        }
    }
}
