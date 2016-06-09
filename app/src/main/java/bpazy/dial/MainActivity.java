package bpazy.dial;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;
    private WifiManager wifiManager;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        initView();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiManager.setWifiEnabled(true);
                textView.setText("");
                TextView tv = new TextView(MainActivity.this);
                tv.setText("请稍候...");
                toast.setView(tv);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                UtilsHelpers.sendMessage("10001", "xykdmm");
                button.setClickable(false);
            }
        });
    }

    private void initView() {
        button = (Button) findViewById(R.id.button_connect);
        textView = (TextView) findViewById(R.id.text_view);
        toast = new Toast(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void turnToSetting(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateWidget(EventClass event) {
        button.setClickable(true);
        if (event.execResult) {
            textView.setText(getString(R.string.success));
        } else {
            textView.setText(getString(R.string.failure));
        }
    }
}
