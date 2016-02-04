package bpazy.dial;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MessageReceiver extends BroadcastReceiver {

    public MessageReceiver() {
        super();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            if (pdus == null) {
                return;
            }
            SmsMessage[] message = new SmsMessage[pdus.length];
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < message.length; i++) {
                message[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                sb.append(message[i].getDisplayMessageBody());
            }
            final String password = UtilsHelpers.getPassword(sb.toString());
            if (password != null) {
                // 是否打断短信传播
                // abortBroadcast();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
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
    }
}
