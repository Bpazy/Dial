package bpazy.dial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.telephony.SmsManager;
import android.widget.Toast;

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

    public static void uploadPassword(final String password, final Context context) {
        // 是否打断短信传播
        // abortBroadcast();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                CRouter crouter = new CRouter();
                SharedPreferences sharedPreferences = context.getSharedPreferences("LoginData", Activity.MODE_PRIVATE);
                String Cookie = sharedPreferences.getString("Cookie", "Authorization=Basic%20YWRtaW46OTUwNjAx; ChgPwdSubTag=");
                String userName = sharedPreferences.getString("userName", "17751752291@njxy");
                int result = crouter.connect(password, Cookie, userName);
                if (result == CRouter.SUCCESS) {
                    Toast.makeText(context, "执行成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "执行失败,错误代码: " + result, Toast.LENGTH_LONG).show();
                }
                Intent intentToUI = new Intent();
                intentToUI.setAction("toMainActivity");
                intentToUI.putExtra("data", result);
                context.sendBroadcast(intentToUI);
            }
        }).start();
    }
}