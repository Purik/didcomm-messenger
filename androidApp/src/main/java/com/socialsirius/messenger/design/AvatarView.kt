package com.socialsirius.messenger.design

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlendMode.SRC_ATOP
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.widget.FrameLayout
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.socialsirius.messenger.R


class AvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    var  activeStatusView : View
    var  bordersView : ImageView
    var  ownerStatusView : ImageView
    var  avatarLettersView : TextView
    var  imageView : ImageView
    var  backgroundView : View
    var  borderColor : Int
    enum class Mode {
        LARGE,
        MEDIUM,
        SMALL,
        EXTRALARGE
    }

    var showStatus: Boolean = true
        private set
    var ownerStatus: Boolean = false
        private set
    var showBorders: Boolean = false
        private set
    var mode: Mode = Mode.MEDIUM
        private set

/*    private var connectedRosterUser: RosterUser? = null
        set(value) {
            field = value
            tag = value?.jid
        }*/

    private val circleDrawable = ContextCompat.getDrawable(this.context, R.drawable.bg_circle_blue)

    init {
        View.inflate(context, R.layout.view_avatar, this)
        activeStatusView = findViewById(R.id.activeStatusView)
        bordersView = findViewById(R.id.bordersView)
        avatarLettersView = findViewById(R.id.avatarLettersView)
        imageView = findViewById(R.id.imageView)
        ownerStatusView = findViewById(R.id.ownerStatusView)
        backgroundView = findViewById(R.id.backgroundView)
        borderColor  =  context.resources.getColor(R.color.backgroundColor)
        attrs?.let {
            val arr = context.obtainStyledAttributes(it, R.styleable.AvatarView, 0, 0)
            showStatus = arr.getBoolean(R.styleable.AvatarView_showStatus, true)
            ownerStatus = arr.getBoolean(R.styleable.AvatarView_ownerStatus, false)
            showBorders = arr.getBoolean(R.styleable.AvatarView_showBorders, false)
            borderColor = arr.getColor(R.styleable.AvatarView_borderColorAvatar, context.resources.getColor(R.color.backgroundColor))
            mode = when (arr.getInt(R.styleable.AvatarView_mode, 1)) {
                0    -> Mode.LARGE
                2    -> Mode.SMALL
                3    -> Mode.EXTRALARGE
                else -> Mode.MEDIUM
            }
            arr.recycle()
        }

        updateWithParams()
    }

    private fun updateWithParams() {
        activeStatusView.visibility = View.GONE
        bordersView.visibility = if (showBorders) View.VISIBLE else View.GONE
        ownerStatusView.visibility = if (ownerStatus) View.VISIBLE else View.GONE
        bordersView.setColorFilter(borderColor)
        when (mode) {
            Mode.LARGE  -> avatarLettersView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.headline6_size))
            Mode.MEDIUM -> avatarLettersView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.subtitle1_size))
            Mode.SMALL  -> avatarLettersView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.overline_size))
            Mode.EXTRALARGE  -> avatarLettersView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.headline3_size))
        }
        invalidate()
    }

/*    fun update(user: RosterUser?) {
        connectedRosterUser = user

        imageView.setImageDrawable(null)
        if (user?.isRobot != true) {
            imageView.setPadding(0, 0, 0, 0)
            update(user?.contactName.orEmpty(), user?.fullname, user?.avatar, user?.bitmap)
        } else {
            setLetters("", "")
            if (!user.avatar.isNullOrEmpty()) {
                loadBitmap(user.avatar)
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_bot))
                imageView.setPadding(4, 4, 4, 4)
            }
        }
    }*/

   /* fun update(chat: Chats) {
        imageView.setImageDrawable(null)
        if (chat.isRoom) {
            imageView.setPadding(0, 0, 0, 0)
            val names = chat.title.split(" ")
            update(chat.title, names.getOrNull(1), chat.icon, null)
            activeStatusView.visibility = View.GONE
        } else {
            val members = chat.members?.filter { it.jid != AppPref.getUserJid() }
            if (!members.isNullOrEmpty()) {
                val user = members[0]
                connectedRosterUser = user
                update(user)
            } else {
                val names = chat.title?.split(" ")
                var name = ""
                if(names?.size?:0 >0){
                    name = names!![0]
                }
                update(name, names?.getOrNull(1))
            }
        }
    }*/

    fun update(name: String, surname: String?, imageUrl: String? = null) {
        update(name, surname, imageUrl, null)
    }

    fun setText(text: String) {
        imageView.setImageBitmap(null)
        avatarLettersView.text = text
        if (Build.VERSION.SDK_INT >= VERSION_CODES.Q) {
            circleDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(this.context, R.color.hintColor), SRC_ATOP)
        } else {
            circleDrawable?.setColorFilter(ContextCompat.getColor(this.context, R.color.hintColor), PorterDuff.Mode.SRC_ATOP)
        }
        backgroundView.background = circleDrawable
    }

    fun update(name: String, surname: String?, imageUrl: String?, imageBitmap: Bitmap?) {
        if (imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap)
        } else {
            setLetters(name, surname)
            if (!imageUrl.isNullOrEmpty()) {
                loadBitmap(imageUrl)
            }
        }
    }

    fun updateStatus(isOnline: Boolean) {
        activeStatusView.visibility = if (showStatus && isOnline) View.VISIBLE else View.GONE
    }

    private fun setLetters(name: String, surname: String?) {
        avatarLettersView.visibility = View.VISIBLE
        val firstLetter = name.firstOrNull()?.toString().orEmpty()
        val secondLetter = if (!surname.isNullOrEmpty()) {
            surname.first().toString()
        } else if (name.length > 1) {
            name[1].toString()
        } else {
            ""
        }

        val letters = (firstLetter + secondLetter).trim().toUpperCase()
        avatarLettersView.text = letters

        val hash = name.hashCode() + (surname?.hashCode() ?: 0)

        val color = when ((hash / 10) % 5) {
            0    -> R.color.green
            1    -> R.color.blue
            2    -> R.color.userViolet
            3    -> R.color.userRed
            else -> R.color.userYellow
        }
        if (Build.VERSION.SDK_INT >= VERSION_CODES.Q) {
            circleDrawable?.colorFilter = BlendModeColorFilter(ContextCompat.getColor(this.context, color), SRC_ATOP)
        } else {
            circleDrawable?.setColorFilter(ContextCompat.getColor(this.context, color), PorterDuff.Mode.SRC_ATOP)
        }
        backgroundView.background = circleDrawable
    }

    private fun loadBitmap(url: String) {
      /*  Glide.with(imageView)
            .load(ApiUtil.getGlideUrlWithAuthorize(url))
            .apply(RequestOptions().circleCrop()).into(imageView)*/
    }
}