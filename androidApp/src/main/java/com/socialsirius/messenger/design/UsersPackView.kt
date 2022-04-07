package com.socialsirius.messenger.design

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.socialsirius.messenger.R

import kotlin.math.max
import kotlin.math.min

class UsersPackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var maxCount = 4
    private var packMode = 0
    private var showOwner = false

    init {
  /*      attrs?.let {
            val arr = context.obtainStyledAttributes(it, R.styleable.UsersPackView, 0, 0)
            when (arr.getInt(R.styleable.UsersPackView_packMode, 0)) {
                0    -> {
                    packMode = 0
                    View.inflate(context, R.layout.view_users_pack_left_to_right, this)
                }
                else -> {
                    packMode = 1
                    View.inflate(context, R.layout.view_users_pack_right_to_left, this)
                }
            }
            maxCount = min(arr.getInt(R.styleable.UsersPackView_maxCount, 4), 5)
            showOwner = arr.getBoolean(R.styleable.UsersPackView_showOwner, false)
            arr.recycle()
        }

        for (i in 0 until maxCount) {
            (when (i) {
                0    -> findViewById<AvatarView>(R.id.user1)
                1    -> findViewById<AvatarView>(R.id.user2)
                2    -> findViewById<AvatarView>(R.id.user3)
                3    -> findViewById<AvatarView>(R.id.user4)
                else -> findViewById<AvatarView>(R.id.user5)
            }).visibility = View.VISIBLE
        }
        val userOwner = findViewById<AvatarView>(R.id.userOwner)
        if(showOwner){
            userOwner.visibility = View.VISIBLE
        }else{
            userOwner.visibility = View.GONE
        }*/
    }
  /*  fun setOwner(user : RosterUser?) {
        val userOwner = findViewById<AvatarView>(R.id.userOwner)
        if(user != null){
            userOwner.update(user)
        }else{
            userOwner.visibility = View.GONE
        }
    }

    fun setUsers(users: List<RosterUser>) {
        findViewById<AvatarView>(R.id.user1).visibility = View.GONE
        findViewById<AvatarView>(R.id.user2).visibility = View.GONE
        findViewById<AvatarView>(R.id.user3).visibility = View.GONE
        findViewById<AvatarView>(R.id.user4).visibility = View.GONE
        findViewById<AvatarView>(R.id.user5).visibility = View.GONE

        val overflow = if (users.size > maxCount) users.size - maxCount + 1 else null

        (if (overflow != null) users.take(maxCount - 1) else users).forEachIndexed { index, user ->
            val avatarView = when (index + packMode) {
                0    -> findViewById<AvatarView>(R.id.user1)
                1    -> findViewById<AvatarView>(R.id.user2)
                2    -> findViewById<AvatarView>(R.id.user3)
                3    -> findViewById<AvatarView>(R.id.user4)
                else -> findViewById<AvatarView>(R.id.user5)
            }

            avatarView.visibility = View.VISIBLE
            avatarView.update(user)
        }

        if (overflow != null) {
            (if (packMode == 0) findViewById<AvatarView>(R.id.user5) else findViewById<AvatarView>(R.id.user1)).let {
                it.setText("+${overflow}")
                it.visibility = View.VISIBLE
            }
        }
    }*/
}