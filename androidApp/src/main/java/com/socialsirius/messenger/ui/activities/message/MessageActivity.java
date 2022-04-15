package com.socialsirius.messenger.ui.activities.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.socialsirius.messenger.R;
import com.socialsirius.messenger.base.App;
import com.socialsirius.messenger.base.ui.BaseActivity;
import com.socialsirius.messenger.databinding.ActivityMessageBinding;
import com.socialsirius.messenger.models.Chats;
import com.socialsirius.messenger.models.ui.ItemContacts;
import com.socialsirius.messenger.ui.activities.main.MainActivity;
import com.socialsirius.messenger.ui.chats.chat.ChatFragment;


public class MessageActivity extends BaseActivity<ActivityMessageBinding, MessageActivityModel> {

    public static  void newInstance(Context context, Chats chats){
        Intent intent = new Intent(context, MessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("chat",chats);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutRes() {
        return R.layout.activity_message;
    }

    @Override
    public void initDagger() {
        App.getInstance().getAppComponent().inject(this);
    }


    @Override
    public int getRootFragmentId() {
        return R.id.mainFrame;
    }

    @Override
    public void setupViews() {
        super.setupViews();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if(intent.hasExtra("chat")){
            Chats chat = (Chats) intent.getSerializableExtra("chat");
            model.setChat(chat);
            if (chat!=null ){
                showPage(ChatFragment.newInstance(chat));
            }else{
                finish();
            }
        }else{
            finish();
        }
    }

}
