package bpazy.diag;

import android.telephony.SmsManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtherFunctions {
    public static void sendMessage(String num, String content) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(num, null, content, null, null);
    }

    public static String getPassword(StringBuilder sb) {
        Pattern pattern = Pattern.compile("(?<=密码)[0-9]{6}");
        Matcher matcher = pattern.matcher(sb.toString());
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}