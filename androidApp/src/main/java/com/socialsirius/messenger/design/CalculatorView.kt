package com.socialsirius.messenger.design

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.socialsirius.messenger.R
import kotlinx.android.synthetic.main.view_calculator_layout.view.buttonBack
import kotlinx.android.synthetic.main.view_calculator_layout.view.buttonEight
import kotlinx.android.synthetic.main.view_calculator_layout.view.buttonFive
import kotlinx.android.synthetic.main.view_calculator_layout.view.buttonFour
import kotlinx.android.synthetic.main.view_calculator_layout.view.buttonIdentify
import kotlinx.android.synthetic.main.view_calculator_layout.view.buttonNine
import kotlinx.android.synthetic.main.view_calculator_layout.view.buttonOne
import kotlinx.android.synthetic.main.view_calculator_layout.view.buttonSeven
import kotlinx.android.synthetic.main.view_calculator_layout.view.buttonSix
import kotlinx.android.synthetic.main.view_calculator_layout.view.buttonThree
import kotlinx.android.synthetic.main.view_calculator_layout.view.buttonTwo
import kotlinx.android.synthetic.main.view_calculator_layout.view.buttonZero

class CalculatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var digitButtons: List<Button>

    init {
        View.inflate(context, R.layout.view_calculator_layout, this)
        initButtons()
    }

    private fun initButtons() {
        digitButtons = listOf(
            buttonZero,
            buttonOne,
            buttonTwo,
            buttonThree,
            buttonFour,
            buttonFive,
            buttonSix,
            buttonSeven,
            buttonEight,
            buttonNine
        )
    }

    fun enableAllButtons(isEnabled : Boolean, enableIdentity : Boolean = true){
        digitButtons.forEach {
            it.isEnabled = isEnabled
        }
        if (enableIdentity){
            enableIdentityButton(isEnabled)
        }
        enableBackButton(isEnabled)
    }


    fun enableIdentityButton(isEnabled : Boolean){
        buttonIdentify.isEnabled = isEnabled
    }

    fun enableBackButton(isEnabled : Boolean){
        buttonBack.isEnabled = isEnabled
    }

    fun setOnDigitClickListener(onDigitClick: (digit: Int) -> Unit) {
        digitButtons.forEachIndexed { index, btn ->
            btn.setOnClickListener { onDigitClick(index) }
        }
    }

    fun setDeleteClickListener(onBackClick: () -> Unit) {
        buttonBack.setOnClickListener { onBackClick() }
    }

    fun setDeleteLongClickListener(onBackClick: () -> Boolean) {
        buttonBack.setOnLongClickListener { onBackClick() }
    }

    fun setIdentifyClickListener(onCommaClick: () -> Unit) {
        buttonIdentify.setOnClickListener { onCommaClick() }
    }
}