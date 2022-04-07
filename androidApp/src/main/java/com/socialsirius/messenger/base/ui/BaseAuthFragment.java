package com.socialsirius.messenger.base.ui;/*
package com.webprofy.mosokna.base.ui;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.databinding.ViewDataBinding;

import androidx.lifecycle.Observer;
import com.webprofy.mosokna.R;

public abstract class BaseAuthFragment<VB extends ViewDataBinding, VM extends BaseAuthViewModel> extends BaseFragment<VB, VM> {

    @Override
    public boolean isBack() {
        return true;
    }

    public abstract void onRightTitleClick();

    @Override
    public void setupToolbar() {
        super.setupToolbar();
        TextView toolbarRightTitle = rootView.findViewById(R.id.toolbar_title_right);
        ImageView imageRightTitle = rootView.findViewById(R.id.toolbar_right);
        LinearLayout rightToolbar = rootView.findViewById(R.id.rightToolbar);
        model.getTitleRightLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toolbarRightTitle.setText(s);
            }
        });

        model.getImageRightLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer s) {
                imageRightTitle.setImageResource(s);
            }
        });

        rightToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.getOnRightTitleClickLiveData().postValue(true);
            }
        });


        model.getOnRightTitleClickLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    onRightTitleClick();
                    model.getOnRightTitleClickLiveData().postValue(false);
                }
            }
        });
    }
}
*/
