package com.example.pc.aoptest.aop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.lib.aop.annotation.NetCheck;
import com.example.pc.aoptest.R;


public class AopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gotoDown();
    }


    @NetCheck
    public void gotoDown() {
        MyLog.e("gotoDown  网络检查通过");
    }
}
