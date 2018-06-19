package com.example.liaohuan.mylauncher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        findViewById(R.id.c_input).requestFocusFromTouch();
        findViewById(R.id.c_input).requestFocus();
    }

    public void onClick(View v){
        setViewState(v);
        switch (v.getId()){
            case R.id.c_input:
//                startActivity(new Intent(this, TvInputActivity.class));
                break;
            case R.id.c_video:
                AppUtil.startMedia(this, AppUtil.OPTION_STATE_VIDEO);
                break;
            case R.id.c_wifi:
                AppUtil.startApp(this, "com.android.settings");
                break;
            case R.id.c_music:
                AppUtil.startMedia(this, AppUtil.OPTION_STATE_SONG);
                break;
            case R.id.c_aiqiyi:
                Toast.makeText(this, "5", Toast.LENGTH_SHORT).show();
                break;
            case R.id.c_app:
//                startActivity(new Intent(this, AppActivity.class));
                break;
            case R.id.c_file:
                AppUtil.startFileApp(this);
                break;
            case R.id.c_setting:
                AppUtil.startApp(this, "com.android.settings");
                break;
        }
    }

    private void setViewState(View v){
//        findViewById(R.id.c_input).setSelected(false);
//        findViewById(R.id.c_video).setSelected(false);
//        findViewById(R.id.c_wifi).setSelected(false);
//        findViewById(R.id.c_music).setSelected(false);
//        findViewById(R.id.c_aiqiyi).setSelected(false);
//        findViewById(R.id.c_app).setSelected(false);
//        findViewById(R.id.c_file).setSelected(false);
//        findViewById(R.id.c_setting).setSelected(false);
        v.requestFocusFromTouch();
        v.requestFocus();
//        v.setSelected(true);
    }

}
