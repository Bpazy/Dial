package bpazy.dial;

import android.telephony.SmsManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsHelpers {
    public static void sendMessage(String num, String content) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(num, null, content, null, null);
    }

    public static String getPassword(String sb) {
        Pattern pattern = Pattern.compile("(?<=密码)[0-9]{6}");
        Matcher matcher = pattern.matcher(sb);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}