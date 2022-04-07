package com.socialsirius.messenger.design.chat

import android.R.color
import android.app.Activity
import android.content.Context
import android.graphics.BlendMode.SRC_ATOP
import android.graphics.BlendMode.SRC_IN
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuff.Mode
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.content.ContextCompat


import com.socialsirius.messenger.R
import com.socialsirius.messenger.utils.FileUtils

import kotlinx.android.synthetic.main.view_message_audio.view.*

import java.io.File
import java.io.IOException

class AudioMessageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var currentAudioFile: String = ""
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var onStartPlaying: (String) -> Unit = {}
    private var messageId: String = ""

    init {
        View.inflate(context, R.layout.view_message_audio, this)
        playButtonView.isEnabled = false
        playButtonView.updateState(PlayButtonView.State.Play)

        mediaPlayer.setOnPreparedListener { mp ->
            seekBar.max = mp.duration
            seekBarV.max = mp.duration
            playButtonView.isEnabled = true
            audioTotalTextView.text = convertSecondsToHMmSs(mp.duration / 1000.toLong())
        }
        mediaPlayer.setOnCompletionListener {
            playButtonView.updateState(PlayButtonView.State.Play, false)
        }

        val seekBarListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.let {
                        it.seekTo(progress)
                        seekBarV.updatePlayerPercent(it.currentPosition.toFloat() / it.duration)
                        update(it)
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                playButtonView.updateState(PlayButtonView.State.Play, false)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                start(mediaPlayer, onStartPlaying)
            }
        }

        playButtonView.setOnClickListener {
            if (playButtonView.state == PlayButtonView.State.Play) {
                start(mediaPlayer, onStartPlaying)
                update(mediaPlayer)

            } else {
                pausePlayer()
            }
        }

        seekBarV.setOnSeekBarChangeListener(seekBarListener)
    }

    fun setIsOnDarkBackground(darkBackground: Boolean) {
        if (darkBackground) {
            audioProgressTextView.setTextColor(ContextCompat.getColor(this.context, R.color.backgroundColor))
            audioTotalTextView.setTextColor(ContextCompat.getColor(this.context, R.color.backgroundColorWithAlpha))
            playButtonView.setIsOnDarkBackground(darkBackground)
            setProgressBarColors(R.color.backgroundColor, R.color.backgroundColorWithAlpha)
            progressIcon.setColorFilter(ContextCompat.getColor(this.context, R.color.backgroundColor), PorterDuff.Mode.SRC_ATOP)
            progressBar.progressDrawable = ContextCompat.getDrawable(this.context, R.drawable.bg_progress_circle_light)
        } else {
            audioProgressTextView.setTextColor(ContextCompat.getColor(this.context, R.color.defaultColor))
            audioTotalTextView.setTextColor(ContextCompat.getColor(this.context, R.color.hintColor))
            playButtonView.setIsOnDarkBackground(darkBackground)
            setProgressBarColors(R.color.defaultColor, R.color.hintColor)
            progressIcon.setColorFilter(ContextCompat.getColor(this.context, R.color.defaultColor), PorterDuff.Mode.SRC_ATOP)
            progressBar.progressDrawable = ContextCompat.getDrawable(this.context, R.drawable.bg_progress_circle_dark)
        }
    }

    fun setStartPlayingCallback(messageId: String, onStartPlaying: (String) -> Unit) {
        this.onStartPlaying = onStartPlaying
        this.messageId = messageId
    }

    private fun setProgressBarColors(progressColor: Int, backColor: Int) {
        val progressBarDrawable = seekBar.progressDrawable as LayerDrawable
        val backgroundDrawable: Drawable = progressBarDrawable.getDrawable(0)
        val progressDrawable: Drawable = progressBarDrawable.getDrawable(1)

        if (VERSION.SDK_INT >= VERSION_CODES.Q) {
            backgroundDrawable.colorFilter = BlendModeColorFilter(ContextCompat.getColor(this.context, backColor), SRC_ATOP)
            progressDrawable.colorFilter = BlendModeColorFilter(ContextCompat.getColor(this.context, progressColor), SRC_ATOP)
            seekBarV.progressDrawable.colorFilter = BlendModeColorFilter(ContextCompat.getColor(this.context, R.color.transparent), SRC_IN)
            seekBarV.thumb.colorFilter = BlendModeColorFilter(ContextCompat.getColor(this.context, R.color.transparent), SRC_IN)
        } else {
            backgroundDrawable.setColorFilter(ContextCompat.getColor(this.context, backColor), Mode.SRC_ATOP)
            progressDrawable.setColorFilter(ContextCompat.getColor(this.context, progressColor), Mode.SRC_ATOP)
            seekBarV.progressDrawable.setColorFilter(ContextCompat.getColor(this.context, color.transparent), Mode.SRC_IN)
            seekBarV.thumb.setColorFilter(ContextCompat.getColor(this.context, color.transparent), Mode.SRC_IN)
        }

        seekBarV.setColors(ContextCompat.getColor(this.context, progressColor), ContextCompat.getColor(this.context, backColor))
    }

    fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    fun pausePlayer() {
        playButtonView.updateState(PlayButtonView.State.Play)
        if (mediaPlayer.isPlaying) mediaPlayer.pause()
    }

    fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    fun updateAudioUrl(audioPath: String?) {
        if (audioPath != null) {
            if (currentAudioFile.isEmpty()) {
                currentAudioFile = audioPath.orEmpty()
                try {
                    mediaPlayer.setDataSource(currentAudioFile)
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setVolume(10f, 10f)

                    seekBarV.updateVisualizer(FileUtils.fileToBytes(File(currentAudioFile)))
                    // seekBarV.updateVisualizer(FileUtils.fileToBytes(File(currentAudioFile)))

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        }
    }

    fun updateProgress(progress: Int, isDownload: Boolean) {
        if (progress < 100) {
            if (isDownload) {
                progressIcon.setImageResource(R.drawable.ic_download_arrow)
            } else {
                progressIcon.setImageResource(R.drawable.ic_upload_arrow)
            }
            progressBar.visibility = View.VISIBLE
            progressIcon.visibility = View.VISIBLE
            playButtonView.visibility = View.INVISIBLE
            progressBar.progress = progress
        } else {
            progressBar.visibility = View.GONE
            progressIcon.visibility = View.GONE
            playButtonView.visibility = View.VISIBLE
        }
    }

    private fun start(mediaPlayer: MediaPlayer, onStartPlaying: (String) -> Unit) {
        //TODO  java.lang.IllegalStateException
        try {
            onStartPlaying.invoke(messageId)
            playButtonView.updateState(PlayButtonView.State.Pause, false)
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun update(mediaPlayer: MediaPlayer) {
        (context as? Activity)?.runOnUiThread {
            seekBar.progress = mediaPlayer.currentPosition
            seekBarV.progress = mediaPlayer.currentPosition
            seekBarV.updatePlayerPercent(mediaPlayer.currentPosition.toFloat() / mediaPlayer.duration)

            if (mediaPlayer.duration - mediaPlayer.currentPosition > 100) {
                audioProgressTextView.text = convertSecondsToHMmSs((mediaPlayer.currentPosition / 1000).toLong()).toString()
            } else {
                audioProgressTextView.text = convertSecondsToHMmSs((mediaPlayer.duration / 1000).toLong())
                seekBar.progress = 0
                seekBarV.progress = 0
                seekBarV.updatePlayerPercent(0f)
            }

            val handler = Handler()
            try {
                val runnable = Runnable {
                    try {
                        if (mediaPlayer.currentPosition > -1 && mediaPlayer.currentPosition < mediaPlayer.duration) {
                            try {
                                update(mediaPlayer)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            playButtonView.updateState(PlayButtonView.State.Play)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                handler.postDelayed(runnable, 10)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun convertSecondsToHMmSs(seconds: Long): String? {
        val s = seconds % 60
        val m = seconds / 60 % 60
        val h = seconds / (60 * 60) % 24
        return String.format("%02d:%02d:%02d", h, m, s)
    }
}