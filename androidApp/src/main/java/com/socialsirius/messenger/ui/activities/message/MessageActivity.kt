package com.socialsirius.messenger.ui.activities.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseActivity
import com.socialsirius.messenger.databinding.ActivityMessageBinding
import com.socialsirius.messenger.models.Chats
import com.socialsirius.messenger.ui.chats.chat.ChatFragment.Companion.newInstance

class MessageActivity :
    BaseActivity<ActivityMessageBinding, MessageActivityModel>() {
    override fun getLayoutRes(): Int {
        return R.layout.activity_message
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun getRootFragmentId(): Int {
        return R.id.mainFrame
    }

    override fun setupViews() {
        super.setupViews()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        if (intent.hasExtra("chat")) {
            val chat = intent.getSerializableExtra("chat") as Chats?
            model!!.chat = chat
            if (chat != null) {
                showPage(newInstance(chat))
            } else {
                finish()
            }
        } else {
            finish()
        }
    }

    companion object {
        fun newInstance(context: Context, chats: Chats?) {
            val intent = createIntent(context,chats)
            context.startActivity(intent)
        }

        fun createIntent(context: Context, chats: Chats?): Intent {
            val intent = Intent(context, MessageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("chat", chats)
            return intent
        }
    }
}
