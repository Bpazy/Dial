package bpazy.dial;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class SettingActivity extends AppCompatActivity {

    private EditText editText_Cookie, editText_userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        editText_Cookie = (EditText) findViewById(R.id.edit_cookie);
        editText_userName = (EditText) findViewById(R.id.edit_userName);
        loadLoginData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void turnToMainActivity(View view) {
        finish();
    }

    public void saveLoginData(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", Activity
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String Cookie = editText_Cookie.getText().toString();
        editor.putString("Cookie", Cookie);
        editor.putString("userName", Cookie);
        editor.apply();
        Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
    }

    private void loadLoginData() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", Activity
                .MODE_PRIVATE);
        String Cookie = sharedPreferences.getString("Cookie",
                "Authorization=Basic%20YWRtaW46OTUwNjAx; ChgPwdSubTag=");
        String userName = sharedPreferences.getString("userName", "17751752291@njxy");
        editText_Cookie.setText(Cookie);
        editText_userName.setText(userName);
    }

    public void startNotifyActivity(View v) {
        Toast.makeText(this, R.string.toast_warning, Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setAction("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);
    }
}
