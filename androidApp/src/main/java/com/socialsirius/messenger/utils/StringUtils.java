/**
 *
 * Copyright 2003-2007 Jive Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.socialsirius.messenger.utils;

import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Patterns;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * A collection of utility methods for String objects.
 */
public class StringUtils {

    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String UTF8 = "UTF-8";
    public static final String USASCII = "US-ASCII";

    public static final String QUOTE_ENCODE = "&quot;";
    public static final String APOS_ENCODE = "&apos;";
    public static final String AMP_ENCODE = "&amp;";
    public static final String LT_ENCODE = "&lt;";
    public static final String GT_ENCODE = "&gt;";

    public static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    /**
     * Escapes all necessary characters in the String so that it can be used
     * in an XML doc.
     *
     * @param string the string to escape.
     * @return the string with appropriate characters escaped.
     */
    public static CharSequence escapeForXML(final String string) {
        if (string == null) {
            return null;
        }
        final char[] input = string.toCharArray();
        final int len = input.length;
        final StringBuilder out = new StringBuilder((int)(len*1.3));
        CharSequence toAppend;
        char ch;
        int last = 0;
        int i = 0;
        while (i < len) {
            toAppend = null;
            ch = input[i];
            switch(ch) {
            case '<':
                toAppend = LT_ENCODE;
                break;
            case '>':
                toAppend = GT_ENCODE;
                break;
            case '&':
                toAppend = AMP_ENCODE;
                break;
            case '"':
                toAppend = QUOTE_ENCODE;
                break;
            case '\'':
                toAppend = APOS_ENCODE;
                break;
            default:
                break;
            }
            if (toAppend != null) {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                out.append(toAppend);
                last = ++i;
            } else {
                i++;
            }
        }
        if (last == 0) {
            return string;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out;
    }



    /**
     * Encodes an array of bytes as String representation of hexadecimal.
     *
     * @param bytes an array of bytes to convert to a hex string.
     * @return generated hex string.
     */
    public static String encodeHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_CHARS[v >>> 4];
            hexChars[j * 2 + 1] = HEX_CHARS[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] toBytes(String string) {
        try {
            return string.getBytes(StringUtils.UTF8);
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not supported by platform", e);
        }
    }
 
    /**
     * Pseudo-random number generator object for use with randomString().
     * The Random class is not considered to be cryptographically secure, so
     * only use these random Strings for low to medium security applications.
     */
    private static Random randGen = new Random();

    /**
     * Array of numbers and letters of mixed case. Numbers appear in the list
     * twice so that there is a more equal chance that a number will be picked.
     * We can use the array to get a random number or letter by picking a random
     * array index.
     */
    private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
                    "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();

    /**
     * Returns a random String of numbers and letters (lower and upper case)
     * of the specified length. The method uses the Random class that is
     * built-in to Java which is suitable for low to medium grade security uses.
     * This means that the output is only pseudo random, i.e., each number is
     * mathematically generated so is not truly random.<p>
     *
     * The specified length must be at least one. If not, the method will return
     * null.
     *
     * @param length the desired length of the random String to return.
     * @return a random String of numbers and letters of the specified length.
     */
    public static String randomString(int length) {
        if (length < 1) {
            return null;
        }
        // Create a char buffer to put random letters and numbers in.
        char [] randBuffer = new char[length];
        for (int i=0; i<randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(numbersAndLetters.length)];
        }
        return new String(randBuffer);
    }

    /**
     * Returns true if CharSequence is not null and is not empty, false otherwise
     * Examples:
     *    isNotEmpty(null) - false
     *    isNotEmpty("") - false
     *    isNotEmpty(" ") - true
     *    isNotEmpty("empty") - true
     *
     * @param cs checked CharSequence
     * @return true if string is not null and is not empty, false otherwise
     */
    public static boolean isNotEmpty(CharSequence cs) {
        return !isNullOrEmpty(cs);
    }

    /**
     * Returns true if the given CharSequence is null or empty.
     *
     * @param cs
     * @return true if the given CharSequence is null or empty
     */
    public static boolean isNullOrEmpty(CharSequence cs) {
        return cs == null || isEmpty(cs);
    }

    /**
     * Returns true if the given CharSequence is empty
     * 
     * @param cs
     * @return true if the given CharSequence is empty
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs.length() == 0;
    }

    public static String collectionToString(Collection<String> collection) {
        StringBuilder sb = new StringBuilder();
        for (String s : collection) {
            sb.append(s);
            sb.append(" ");
        }
        String res = sb.toString();
        // Remove the trailing whitespace
        res = res.substring(0, res.length() - 1);
        return res;
    }

    public static String returnIfNotEmptyTrimmed(String string) {
        if (string == null)
            return null;
        String trimmedString = string.trim();
        if (trimmedString.length() > 0) {
            return trimmedString;
        } else {
            return null;
        }
    }

    public static boolean nullSafeCharSequenceEquals(CharSequence csOne, CharSequence csTwo) {
        return nullSafeCharSequenceComperator(csOne, csTwo) == 0;
    }

    public static int nullSafeCharSequenceComperator(CharSequence csOne, CharSequence csTwo) {
        if (csOne == null ^ csTwo == null) {
            return (csOne == null) ? -1 : 1;
        }
        if (csOne == null && csTwo == null) {
            return 0;
        }
        return csOne.toString().compareTo(csTwo.toString());
    }

    public static <CS extends CharSequence> CS requireNotNullOrEmpty(CS cs, String message) {
        if (isNullOrEmpty(cs)) {
            throw new IllegalArgumentException(message);
        }
        return cs;
    }

    /**
     * Return the String representation of the given char sequence if it is not null.
     *
     * @param cs the char sequence or null.
     * @return the String representation of <code>cs</code> or null.
     */
    public static String maybeToString(CharSequence cs) {
        if (cs == null) {
            return null;
        }
        return cs.toString();
    }

    public static String makeStringFromList(List<String> stringList) {
        String settingsStr = "";
        if(stringList == null){
            return settingsStr;
        }
        for (int i = 0; i < stringList.size(); i++) {
            if (i == 0) {
                settingsStr = stringList.get(i);
            } else {
                settingsStr += ", " + stringList.get(i);
            }
        }
        return settingsStr;
    }


    public static String makeStringFromListInteger(List<Integer> stringList) {
        String settingsStr = "";
        for (int i = 0; i < stringList.size(); i++) {
            if (i == 0) {
                settingsStr = stringList.get(i) + "";
            } else {
                settingsStr += "," + stringList.get(i);
            }
        }
        return settingsStr;
    }

    public static String makeStringFromListWithN(List<String> stringList) {
        String settingsStr = "";
        for (int i = 0; i < stringList.size(); i++) {
            if (i == 0) {
                settingsStr = stringList.get(i);
            } else {
                settingsStr += "\n" + stringList.get(i);
            }
        }
        return settingsStr;
    }

    public static List<String> makeListFromStringWitnN(String string) {
        List<String> calendarList = new ArrayList<>();
        if (string != null) {
            String[] dateArray = string.split("\n");
            for (String date : dateArray) {
                date = date.trim();
                if (!date.isEmpty()) {
                    calendarList.add(date);
                }
            }
        }
        return calendarList;
    }

    public static List<String> makeListFromString(String string) {
        List<String> calendarList = new ArrayList<>();
        if (string != null) {
            String[] dateArray = string.split(",");
            for (String date : dateArray) {
                date = date.trim();
                if (!date.isEmpty()) {
                    calendarList.add(date);
                }
            }
        }
        return calendarList;
    }


    public static Set<String> makeSetFromString(String string) {
        Set<String> calendarList = new HashSet<>();
        if (string != null) {
            String[] dateArray = string.split(",");
            for (String date : dateArray) {
                date = date.trim();
                if (!date.isEmpty()) {
                    calendarList.add(date);
                }
            }
        }
        return calendarList;
    }


    public static String makeStringFromSet(Set<String> stringSet) {
        String settingsStr = "";
        int i = 0;
        if (stringSet != null) {
            for (String setStr : stringSet) {
                if (i == 0) {
                    settingsStr = setStr;
                } else {
                    settingsStr += "," + setStr;
                }
                i++;
            }
        }
        return settingsStr;
    }


    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }

    /**
     * Returns an HTML escaped representation of the given plain text.
     */
    public static String escapeHtml(CharSequence text) {
        StringBuilder out = new StringBuilder();
        withinStyle(out, text, 0, text.length());
        return out.toString();
    }

    private static void withinStyle(StringBuilder out, CharSequence text,
                                    int start, int end) {
        for (int i = start; i < end; i++) {
            char c = text.charAt(i);

            if (c == '<') {
                out.append("&lt;");
            } else if (c == '>') {
                out.append("&gt;");
            } else if (c == '&') {
                out.append("&amp;");
            } else if (c >= 0xD800 && c <= 0xDFFF) {
                if (c < 0xDC00 && i + 1 < end) {
                    char d = text.charAt(i + 1);
                    if (d >= 0xDC00 && d <= 0xDFFF) {
                        i++;
                        int codepoint = 0x010000 | (int) c - 0xD800 << 10 | (int) d - 0xDC00;
                        out.append("&#").append(codepoint).append(";");
                    }
                }
            } else if (c > 0x7E || c < ' ') {
                out.append("&#").append((int) c).append(";");
            } else if (c == ' ') {
                while (i + 1 < end && text.charAt(i + 1) == ' ') {
                    out.append("&nbsp;");
                    i++;
                }

                out.append(' ');
            } else {
                out.append(c);
            }
        }
    }

    public static String escapeTags(String text) {
        String textEscaped = text.replace("</div><div>", "");
        textEscaped = text.replaceAll("<(.*?)\\>", "");
        return textEscaped;
    }

    public static CharSequence highlightText(String search, String originalText, int color, int defaultColor) {
        if (search == null || search.isEmpty()) {
            return originalText;
        }
        int start = originalText.toLowerCase().indexOf(search);
        if (start < 0) {
            return originalText;
        } else {
            Spannable highlighted = new SpannableString(originalText);
            while (start >= 0) {
                int spanStart = start;
                int spanEnd = start + search.length();
                highlighted.setSpan(new BackgroundColorSpan(color), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                start = originalText.indexOf(search, spanEnd);
            }
            return highlighted;
        }
    }

    public static CharSequence highlightText(String search, Spannable originalText, int color) {
        if (search == null || search.isEmpty()) {
            return originalText;
        }
        int start = originalText.toString().toLowerCase().indexOf(search);
        while (start >= 0) {
            int spanStart = start;
            int spanEnd = start + search.length();
            originalText.setSpan(new BackgroundColorSpan(color), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = originalText.toString().indexOf(search, spanEnd);
        }
        return originalText;
    }


    public static String unescape(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }

    public static String escape(String value) {
        try {
            //  Html.escapeHtml()
            return URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }


    public static CharSequence highlightText(String search, String originalText, int color) {
        if (search == null || search.isEmpty()) {
            return originalText;
        }
        int start = originalText.toLowerCase().indexOf(search.toLowerCase());
        if (start < 0) {
            return originalText;
        } else {
            Spannable highlighted = new SpannableString(originalText);
            while (start >= 0) {
                int spanStart = start;
                int spanEnd = start + search.length();
                highlighted.setSpan(new BackgroundColorSpan(color), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                start = originalText.indexOf(search, spanEnd);
            }
            return highlighted;
        }
    }

    public static CharSequence underscoreText(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        int spanStart = 0;
        int spanEnd = spanStart + text.length();
        Spannable textSpannable = new SpannableString(text);
        final UnderlineSpan uspan = new UnderlineSpan();
        textSpannable.setSpan(uspan, spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return textSpannable;
    }

    public static CharSequence colorTextInString(String search, String text,int color) {
        if (search == null || search.isEmpty()) {
            return text;
        }

        int start = text.toString().toLowerCase().indexOf(search.toLowerCase());
        Spannable highlighted = new SpannableString(text);
        while (start >= 0) {
            int spanStart = start;
            int spanEnd = start + search.length();
            highlighted.setSpan(new ForegroundColorSpan(color), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = text.toString().indexOf(search, spanEnd);
        }
        return highlighted;
    }

    public static CharSequence underscoreTextInString(String search, String text,int color) {
        if (search == null || search.isEmpty()) {
            return text;
        }

        int start = text.toString().toLowerCase().indexOf(search.toLowerCase());
        Spannable highlighted = new SpannableString(text);
        while (start >= 0) {
            int spanStart = start;
            int spanEnd = start + search.length();
            final UnderlineSpan uspan = new UnderlineSpan();
            highlighted.setSpan(uspan, spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            highlighted.setSpan(new ForegroundColorSpan(color), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = text.toString().indexOf(search, spanEnd);
        }
        return highlighted;
    }


    public static String formatPriceByCurrenyType(double money, int currencyType) {
        String formatedPrice = "";
        try {
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
            otherSymbols.setDecimalSeparator('.');
            otherSymbols.setGroupingSeparator(' ');
            DecimalFormat decim = new DecimalFormat("0.0", otherSymbols);
            switch (currencyType) {
                case 0:
                    decim = new DecimalFormat("0", otherSymbols);
                    break;
                case 1:
                    decim = new DecimalFormat("0.0", otherSymbols);
                    break;
                case 2:
                    decim = new DecimalFormat("0.00", otherSymbols);
                    break;
            }
            decim.setGroupingSize(3);
            decim.setGroupingUsed(true);
            formatedPrice = decim.format(money);
        } catch (Exception e) {

        }
        return formatedPrice;
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    /**
     * Modified from:
     * https://github.com/apache/cordova-plugin-globalization/blob/master/src/android/Globalization.java
     *
     * Returns a well-formed ITEF BCP 47 language tag representing this locale string
     * identifier for the client's current locale
     *
     * @return String: The BCP 47 language tag for the current locale
     */
    public static String toBcp47Language(Locale loc) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return loc.toLanguageTag();
        }

        // we will use a dash as per BCP 47
        final char SEP = '-';
        String language = loc.getLanguage();
        String region = loc.getCountry();
        String variant = loc.getVariant();

        // special case for Norwegian Nynorsk since "NY" cannot be a variant as per BCP 47
        // this goes before the string matching since "NY" wont pass the variant checks
        if (language.equals("no") && region.equals("NO") && variant.equals("NY")) {
            language = "nn";
            region = "NO";
            variant = "";
        }

        if (language.isEmpty() || !language.matches("\\p{Alpha}{2,8}")) {
            language = "und";       // Follow the Locale#toLanguageTag() implementation
            // which says to return "und" for Undetermined
        } else if (language.equals("iw")) {
            language = "he";        // correct deprecated "Hebrew"
        } else if (language.equals("in")) {
            language = "id";        // correct deprecated "Indonesian"
        } else if (language.equals("ji")) {
            language = "yi";        // correct deprecated "Yiddish"
        }

        // ensure valid country code, if not well formed, it's omitted
        if (!region.matches("\\p{Alpha}{2}|\\p{Digit}{3}")) {
            region = "";
        }

        // variant subtags that begin with a letter must be at least 5 characters long
        if (!variant.matches("\\p{Alnum}{5,8}|\\p{Digit}\\p{Alnum}{3}")) {
            variant = "";
        }

        StringBuilder bcp47Tag = new StringBuilder(language);
        if (!region.isEmpty()) {
            bcp47Tag.append(SEP).append(region);
        }
        if (!variant.isEmpty()) {
            bcp47Tag.append(SEP).append(variant);
        }

        return bcp47Tag.toString();
    }

    public static  String separatedText(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        SpannableString urlStyle = new SpannableString(url);
        int i = 0;
        int start = 0;
        int end = 0;
        boolean isStartText = true;
        for (char ch : url.toCharArray()) {
            if (isStartText) {
                start = i;
                isStartText = false;
            }
            i++;
            stringBuilder.append(ch);
            if (ch == ' ' || url.length() == i) {
                if (isLinkify(stringBuilder.toString())) {
                    end = i;
                    urlStyle.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),
                            start, end, 0);
                    return stringBuilder.toString();
                }
                isStartText = true;
                stringBuilder = new StringBuilder();
            }
        }
        return "";
    }

    public static boolean isLinkify(String url) {
        String textUrl = extractRoot(url);
        if (!textUrl.equals("")) {

            String[] cirCh = {
                    "А", "Б", "В", "Г", "Д", "Е", "Ё", "Ж", "З", "И", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф",
                    "Х", "Ц", "Ч", "Ш", "Щ", "Ъ", "Ы", "Ь", "Э", "Ю", "Я",
                    "а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф",
                    "x", "ц", "ч", "ш", "щ", "ъ", "ы", "ь", "э", "ю", "я"
            };
            for (String ch : cirCh) {
                if (textUrl.equals(ch)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static String extractRoot(String url) {
        if (url.length() > 4) {
            if (url.substring(0, 4).equals("www.") || url.substring(0, 4).equals("http")) {
                Matcher m = Patterns.WEB_URL.matcher(url);
                while (m.find()) {
                    String urlString = m.group();
                    return url;
                }
            }
        }
        return "";
    }

    public static boolean isExtractRoot(String url) {
        if (url.length() > 4) {
            if (url.substring(0, 4).equals("www.") || url.substring(0, 4).equals("http")) {
                Matcher m = Patterns.WEB_URL.matcher(url);
                while (m.find()) {
                    String urlString = m.group();
                    return true;
                }
            }
        }
        return false;
    }
}
