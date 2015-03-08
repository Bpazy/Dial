package bpazy.diag;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    Button button;
    TextView textView;
    EditText editText_Cookie;
    BroadcastReceiverUI receiverUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button_connect);
        editText_Cookie = (EditText) findViewById(R.id.edit_cookie);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "请稍候...", Toast.LENGTH_LONG).show();
                OtherFunctions.sendMessage("10001", "xykdmm");
                button.setClickable(false);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiverUI);
    }


    @Override
    protected void onResume() {
        super.onResume();
        init_BroadcastReceiverUI();
    }

    private void init_BroadcastReceiverUI() {
        IntentFilter filter = new IntentFilter("toMainActivity");
        receiverUI = new BroadcastReceiverUI();
        this.registerReceiver(receiverUI, filter);
    }

    public void turnToSetting(View v){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,SettingActivity.class);
        startActivity(intent);
    }

    public class BroadcastReceiverUI extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            button.setClickable(true);
            textView = (TextView) findViewById(R.id.textview);
            int returnCode = intent.getExtras().getInt("data");
            switch (returnCode) {
                case Crouter.SUCCESS:
                    textView.setText("执行成功");
                    break;
                default:
                    textView.setText("执行失败,错误代码: " + returnCode);
            }
        }
    }
}
