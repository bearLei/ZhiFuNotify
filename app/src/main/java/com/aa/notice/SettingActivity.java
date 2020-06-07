package com.aa.notice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends BaseActivity {

    private TextView save;
    private EditText editHost,editSocketHost,editSocketPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        save = findViewById(R.id.save);
        editHost = findViewById(R.id.edit_host);
        editSocketPort = findViewById(R.id.edit_socket_port);
        editSocketHost = findViewById(R.id.edit_socket);
        initIp();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.getInstance().put("baseHost",editHost.getText().toString().trim());
                SPUtils.getInstance().put("baseSocket",editSocketHost.getText().toString());
                SPUtils.getInstance().put("baseSocketPort",editSocketPort.getText().toString());
                Toast.makeText(SettingActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initIp(){
        editHost.setText( SPUtils.getInstance().getString("baseHost"));
        editSocketHost.setText(SPUtils.getInstance().getString("baseSocket"));
        editSocketPort.setText(SPUtils.getInstance().getString("baseSocketPort"));
    }
}
