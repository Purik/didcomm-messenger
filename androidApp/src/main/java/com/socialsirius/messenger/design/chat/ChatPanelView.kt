package com.socialsirius.messenger.design.chat

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.media.MediaPlayer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity

import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.utils.Utils
import kotlinx.android.synthetic.main.view_chat_panel.view.addAttachmentButton
import kotlinx.android.synthetic.main.view_chat_panel.view.bigVoiceButton
import kotlinx.android.synthetic.main.view_chat_panel.view.emodjiButton
import kotlinx.android.synthetic.main.view_chat_panel.view.messageEditText
import kotlinx.android.synthetic.main.view_chat_panel.view.recordImageView
import kotlinx.android.synthetic.main.view_chat_panel.view.recordTimerText
import kotlinx.android.synthetic.main.view_chat_panel.view.sendButton
import kotlinx.android.synthetic.main.view_chat_panel.view.textVoiceButton
import kotlinx.android.synthetic.main.view_chat_panel.view.voiceMessageTrashImageView
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit


class ChatPanelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    var onSendClick: OnSendClick? = null
    var onSoundListener: OnSoundListener? = null

    //Need Enum
    enum class SendType {
        TEXT, VOICE, RECOGNIZE, RECORD_START, RECORD_CANCEL
    }

    interface OnSendClick {
        fun onMessageSend(message: String)
        fun onSoundSend(time: Long,type : SendType)
    }

    interface OnSoundListener {
        fun onSoundStart(): Boolean
        fun onSoundStartRecord()
        fun onSoundCancel()
        fun onSoundStopRecord(isSend: Boolean, time: Long, type: SendType)
    }

    var type = SendType.TEXT
    private var recordFuture: Future<*>? = null

    init {
        View.inflate(context, R.layout.view_chat_panel, this)
        messageEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (TextUtils.isEmpty(text)) {
                    type = SendType.VOICE
                    setViewsByType()
                } else {
                    type = SendType.TEXT
                    setViewsByType()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, count: Int) {}
        })

        textVoiceButton.setOnClickListener {
            if (type == SendType.RECOGNIZE) {
                type = SendType.VOICE
            } else if (type == SendType.VOICE) {
                type = SendType.RECOGNIZE
            }
            setViewsByType()
        }

        voiceMessageTrashImageView.setOnClickListener {
            type = SendType.VOICE
            setViewsByType()
            messageEditText.requestFocus()
            onSoundListener?.onSoundCancel()
        }

        textVoiceButton.setOnLongClickListener {
            if (onSoundListener?.onSoundStart() == true) {
                type = SendType.RECORD_START
                bigVoiceButton.visibility = View.VISIBLE
                scaleUpMessageCircle()

                //Start Record sound
                val soundCLickMainMenu = MediaPlayer.create(getContext(), R.raw.beepbeep)
                soundCLickMainMenu.setOnCompletionListener { mp -> mp.release() }
                soundCLickMainMenu.setOnCompletionListener {
                    onSoundListener?.onSoundStartRecord()
                    setViewsByType()
                    startRecordTimer()
                }
                soundCLickMainMenu.start()

            }
            return@setOnLongClickListener true
        }

        sendButton.setOnClickListener {
            if (type == SendType.TEXT) {
                onSendClick?.onMessageSend(getMessage())
                setMessage("")
            } else {
                onSendClick?.onSoundSend(recordTimerText.tag as Long, type)
                type = SendType.VOICE
                setViewsByType()
                messageEditText.requestFocus()
            }
        }

        textVoiceButton.setOnTouchListener { _, event ->
            val X = event.rawX.toInt()
            val Y = event.rawY.toInt()

            if (event.action == MotionEvent.ACTION_UP) {
                if (type == SendType.RECORD_START) {
                    type = SendType.VOICE
                    onSoundListener?.onSoundStopRecord(true, recordTimerText.tag as Long,type)
                    stopRecordTimer()
                    setViewsByType()
                }
            }
            if (event.action == MotionEvent.ACTION_MOVE) {
                if (type == SendType.RECORD_START) {
                    val currentNextX: Float = event.rawX - bigVoiceButton.width / 2
                    bigVoiceButton.animate().x(currentNextX)
                        .setDuration(0)
                        .start()

                    if (X <= 100) {
                        //Error sound
                        val soundCLickMainMenu = MediaPlayer.create(getContext(), R.raw.error)
                        soundCLickMainMenu.setOnCompletionListener { mp -> mp.release() }
                        soundCLickMainMenu.start()
                        stopRecordTimer()
                        onSoundListener?.onSoundStopRecord(false, recordTimerText.tag as Long,type)

                        type = SendType.RECORD_CANCEL

                        setViewsByType()
                        event.action = MotionEvent.ACTION_UP
                    }
                }
            }
            false
        }
    }

    fun hideTextVoiceButton() {
        textVoiceButton.visibility = View.INVISIBLE
    }

    fun hideKeyboard(activity: FragmentActivity?){
        Utils.hideKeyboard(activity,messageEditText)
    }

    private fun setSoundTimeFromLong() {
        val time = recordTimerText.tag as Long
        val minutes = TimeUnit.MILLISECONDS.toMinutes(time)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(minutes)
        val formatTime = "${context.resources.getString(R.string.record)} ${String.format("%02d:%02d", minutes, seconds)}"
        recordTimerText.text = formatTime
    }

    private fun startRecordTimer() {
        val service = Executors.newSingleThreadScheduledExecutor()
        recordTimerText.tag = 0L
        setSoundTimeFromLong()
        recordFuture?.cancel(true)
        recordFuture = service.scheduleAtFixedRate({
            handler.post {
                val time = recordTimerText.getTag() as Long
                recordTimerText.tag = time + 1000
                setSoundTimeFromLong()
                //Запись больше 2х минут не принимаем
                if (time >= 2 * 60 * 1000) {
                    stopRecordTimer()
                    onSoundListener?.onSoundStopRecord(true, recordTimerText.getTag() as Long,type)
                    type = SendType.VOICE
                    setViewsByType()

                }
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS)
    }

    private fun stopRecordTimer() {
        recordFuture?.cancel(true)
    }

    fun setBigVoiceBtnToStart(){
        bigVoiceButton.post {
            val currentNextX: Float = (x + width) -  bigVoiceButton.width / 2 -  App.getContext().resources.getDimensionPixelSize(R.dimen.common_gap_32)
            Log.d("mylog2080","currentNextX="+currentNextX +" x+"+x+ "widt="+width +"  bigVoiceButton.width / 2="+ (bigVoiceButton.width / 2))
            bigVoiceButton.animate().x(currentNextX)
                .setDuration(0)
                .start()
        }
    }
    fun setViewsByType() {
        when (type) {
            SendType.RECOGNIZE -> {
                setBigVoiceBtnToStart()
                textVoiceButton.setImageResource(R.drawable.ic_voice_to_text)
                textVoiceButton.visibility = View.VISIBLE
                sendButton.visibility = View.GONE
                recordTimerText.visibility = View.GONE
                recordImageView.visibility = View.GONE
                messageEditText.visibility = View.VISIBLE
                addAttachmentButton.visibility = View.VISIBLE
                voiceMessageTrashImageView.visibility = View.GONE
                bigVoiceButton.visibility = View.GONE
                scaleDown?.end()
            }
            SendType.VOICE -> {
                setBigVoiceBtnToStart()
                textVoiceButton.setImageResource(R.drawable.ic_text_to_voice)
                textVoiceButton.visibility = View.VISIBLE
                sendButton.visibility = View.GONE
                recordTimerText.visibility = View.GONE
                recordImageView.visibility = View.GONE
                messageEditText.visibility = View.VISIBLE
                addAttachmentButton.visibility = View.VISIBLE
                voiceMessageTrashImageView.visibility = View.GONE
                bigVoiceButton.visibility = View.GONE
                scaleDown?.end()
            }
            SendType.TEXT -> {
                setBigVoiceBtnToStart()
                sendButton.visibility = View.VISIBLE
                textVoiceButton.visibility = View.GONE
                recordTimerText.visibility = View.GONE
                recordImageView.visibility = View.GONE
                messageEditText.visibility = View.VISIBLE
                addAttachmentButton.visibility = View.VISIBLE
                voiceMessageTrashImageView.visibility = View.GONE
                bigVoiceButton.visibility = View.GONE
                scaleDown?.end()
            }

            SendType.RECORD_START -> {
                setBigVoiceBtnToStart()
                sendButton.visibility = View.GONE
                textVoiceButton.visibility = View.GONE
                recordImageView.visibility = View.VISIBLE
                recordTimerText.visibility = View.VISIBLE
                voiceMessageTrashImageView.visibility = View.GONE
                messageEditText.visibility = View.INVISIBLE
                addAttachmentButton.visibility = View.INVISIBLE
                bigVoiceButton.visibility = View.VISIBLE
            }

            SendType.RECORD_CANCEL -> {
                setBigVoiceBtnToStart()
                sendButton.visibility = View.VISIBLE
                textVoiceButton.visibility = View.GONE
                recordImageView.visibility = View.GONE
                recordTimerText.visibility = View.VISIBLE
                voiceMessageTrashImageView.visibility = View.VISIBLE
                messageEditText.visibility = View.INVISIBLE
                addAttachmentButton.visibility = View.INVISIBLE
                bigVoiceButton.visibility = View.GONE
                scaleDown?.end()
            }
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        addAttachmentButton.isEnabled = enabled
        messageEditText.isEnabled = enabled
        textVoiceButton.isEnabled = enabled
        sendButton.isEnabled  = enabled
        this.alpha = if (enabled) 1f else 0.5f
    }

    fun setOnAttachmentClickListener(onClick: View.OnClickListener) {
        addAttachmentButton.setOnClickListener(onClick)
    }

    fun setOnEmodjiButtonClickListener(onClick: View.OnClickListener) {
        emodjiButton.setOnClickListener(onClick)
    }

    fun getMessage(): String {
        return messageEditText.text.toString()
    }

    fun setMessage(text: String) {
        messageEditText.setText(text)
    }

    private var scaleDown: ObjectAnimator? = null

    private fun scaleUpMessageCircle() {
        if (scaleDown == null) {
            scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                bigVoiceButton,
                PropertyValuesHolder.ofFloat("scaleX", 1.1f),
                PropertyValuesHolder.ofFloat("scaleY", 1.1f))
            scaleDown?.duration = 500
            scaleDown?.repeatCount = ObjectAnimator.INFINITE
            scaleDown?.repeatMode = ObjectAnimator.REVERSE
        }

        scaleDown?.start()
    }
}