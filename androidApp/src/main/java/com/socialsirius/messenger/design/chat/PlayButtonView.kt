package com.socialsirius.messenger.design.chat

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.socialsirius.messenger.R
import kotlinx.android.synthetic.main.view_button_play.view.*


class PlayButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var state: State = State.Play
        private set
    private var whiteBackground = true

    init {
        View.inflate(context, R.layout.view_button_play, this)

        attrs?.let {
            val arr = context.obtainStyledAttributes(it, R.styleable.PlayButtonView, 0, 0)
            whiteBackground = arr.getBoolean(R.styleable.PlayButtonView_whiteBackground, true)
            arr.recycle()
        }

        setIsOnDarkBackground(whiteBackground)
    }

    fun setIsOnDarkBackground(darkBackground: Boolean) {
        whiteBackground = !darkBackground
        updateState(State.Play, false)
    }

    fun updateState(state: State, playAnimation: Boolean = true) {
        this.state = state
        playIconView.background = if (state == State.Play) {
            ContextCompat.getDrawable(this.context,
                if (whiteBackground) R.drawable.ic_play_circle_dark else R.drawable.ic_play_circle_light)
        }
        else {
            ContextCompat.getDrawable(this.context,
                if (whiteBackground) R.drawable.ic_pause_circle_dark else R.drawable.ic_pause_circle_light)
        }
    }

    enum class State {
        Play,
        Pause
    }
}