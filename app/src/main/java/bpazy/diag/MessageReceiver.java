package bpazy.diag;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MessageReceiver extends BroadcastReceiver {

    public MessageReceiver() {
        super();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final MyHandler handler = new MyHandler(context);
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] message = new SmsMessage[pdus.length];
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < message.length; i++) {
                message[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                sb.append(message[i].getDisplayMessageBody());
            }
            final String password = OtherFunctions.getPassword(sb);
            if (password != null) {
                abortBroadcast();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Crouter crouter = new Crouter();
                        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginData", Activity.MODE_PRIVATE);
                        String Cookie = sharedPreferences.getString("Cookie", "Authorization=Basic%20YWRtaW46OTUwNjAx; ChgPwdSubTag=");
                        String userName = sharedPreferences.getString("userName", "17751752291@njxy");
                        int result = crouter.connect(password, Cookie, userName);
                        if (result == Crouter.SUCCESS) {
                            handler.sendEmptyMessage(111);
                        } else {
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putInt("result", result);
                            msg.setData(bundle);
                            msg.what = 112;
                            handler.sendMessage(msg);
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

    static class MyHandler extends Handler {
        Context context;

        public MyHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 111:
                    Toast.makeText(context, "执行成功", Toast.LENGTH_LONG).show();
                    WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                    if(wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED)
                        wifiManager.setWifiEnabled(false);
                    break;
                case 112:
                    Bundle bundle = msg.getData();
                    int result = bundle.getInt("result");
                    Toast.makeText(context, "执行失败,错误代码: " + result, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
