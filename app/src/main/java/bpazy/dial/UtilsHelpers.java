package bpazy.dial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
                SharedPreferences sharedPreferences = context.getSharedPreferences("LoginData", Activity.MODE_PRIVATE);
                String Cookie = sharedPreferences.getString("Cookie", "Authorization=Basic%20YWRtaW46OTUwNjAx; ChgPwdSubTag=");
                String userName = sharedPreferences.getString("userName", "17751752291@njxy");
                int result = CRouter.connect(password, Cookie, userName);
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

    public static class CRouter {
        public CRouter() {
        }

        public final static int SUCCESS = 0;
        public final static int INIT_URL_ERROR = 100;
        public final static int IO_EXCEPTION = 101;
        public final static int UNKNOWN_ERROR = 102;

        /***
         * @param password Router's password
         * @param Cookie   Login router's cookies
         * @param userName Router's username
         * @return SUCCESS = 0 | INIT_URL_ERROR = 100 | IO_EXCEPTION = 101 | UNKNOWN_ERROR = 102;
         */
        public static int connect(String password, String Cookie, String userName) {
            try {
                URL url = new URL("http://192.168.1.1/userRpm/PPPoECfgRpm.htm?wan=0&wantype=2&acc=" + userName + "&psw=" + password + "&confirm=" + password + "&specialDial=100&SecType=0&sta_ip=0.0.0.0&sta_mask=0.0.0.0&linktype=2&Save=%B1%A3+%B4%E6");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setRequestProperty("Accept", "text/html, application/xhtml+xml, */*");
                connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
                connection.setRequestProperty("Accept-Language", "zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Cookie", Cookie);
                connection.setRequestProperty("Host", "192.168.1.1");
                connection.setRequestProperty("Referer", "http://192.168.1.1/userRpm/PPPoECfgRpm.htm");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko");
                connection.connect();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(), "gbk"));
                String result = null, line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                if (result != null && result.matches("(.+)自动断线等待时间(.+)")) {
                    return SUCCESS;
                }
            } catch (MalformedURLException e) {
                return INIT_URL_ERROR;
            } catch (IOException e) {
                return IO_EXCEPTION;
            }
            return UNKNOWN_ERROR;
        }
    }

}