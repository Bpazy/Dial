package bpazy.dial;

import android.os.BaseBundle;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Map;

/***
 * 通过通知读取短信内容
 */
public class SmsNotificationService extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Class cls = sbn.getNotification().getClass();
        try {
            // Android4.3极其以下版本获取Notification.extras
            Field fieldExtras = cls.getDeclaredField("extras");
            fieldExtras.setAccessible(true);
            Bundle extras = (Bundle) fieldExtras.get(sbn.getNotification());
            Log.v("extras", extras.toString());

            // 获取Bundle内容
            Field mMap = BaseBundle.class.getDeclaredField("mMap");
            mMap.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, Object> o = (Map) mMap.get(extras);
            Log.v("Debug", o.toString());
            if (o.toString().contains("密码")) {
                String body = o.get("android.text").toString();
                String password = UtilsHelpers.getPassword(body);
                UtilsHelpers.uploadPassword(password, getApplicationContext());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.i("Debug", "Remove: " + sbn.toString());
    }
}
