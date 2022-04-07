package com.socialsirius.messenger.utils.extensions

import android.text.SpannableString
import android.text.style.StrikethroughSpan
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

fun Double.asPrice(currencySymbol: String = "₽"): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault())
    symbols.setGroupingSeparator(' ')
    val format = DecimalFormat("###,###,###.##",symbols)
    format.isGroupingUsed = true
    format.maximumIntegerDigits = 8
    format.maximumFractionDigits = 2
    val text = format.format(this) + " $currencySymbol"
    return text
}


fun Double.asPriceStrike(currencySymbol: String = "₽"): SpannableString {
    val symbols = DecimalFormatSymbols(Locale.getDefault())
    symbols.setGroupingSeparator(' ')
    val format = DecimalFormat("###,###,###.##",symbols)
    format.isGroupingUsed = true
    format.maximumIntegerDigits = 8
    format.maximumFractionDigits = 2
    val text = format.format(this) + " $currencySymbol"
    val spannableString = SpannableString(text)
    spannableString.setSpan(StrikethroughSpan(), 0, text.length, 0);
    return spannableString
}


fun Double.asWeight(currencySymbol: String = "кг", withMinText : Boolean = false): String {
    val format = DecimalFormat()
    format.isGroupingUsed = false
    format.maximumIntegerDigits = 8
    format.maximumFractionDigits = 1
    var minText = ""
    if( withMinText){
        minText = "мин заказ "
   }
    return minText + format.format(this) + " $currencySymbol"
}



fun Int.asWeight(currencySymbol: String = "кг", withMinText : Boolean = false): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault())
    symbols.setGroupingSeparator(' ')
    val format = DecimalFormat("###,###,###.#",symbols)
    format.isGroupingUsed = true
    format.maximumIntegerDigits = 8
    format.maximumFractionDigits = 1
    var minText = ""
    if(currencySymbol =="кг" && withMinText){
        minText = "мин заказ "
    }

    return minText + format.format(this) + " $currencySymbol"
}
