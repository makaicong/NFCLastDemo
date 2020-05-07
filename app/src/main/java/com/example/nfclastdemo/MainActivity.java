package com.example.nfclastdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nfclastdemo.Util.Utils;

import cn.bmob.v3.Bmob;
import yoworfid.usbreader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static usbreader rfidreader;
    Utils utils = new Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);*/

        //init();
        Bmob.initialize(this,"63aaf005c4526b638ae09aef38d16b56");


        ImageView imageView1 = findViewById(R.id.signin);
        ImageView imageView2 = findViewById(R.id.manage);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);

    }

    public void init(){
        rfidreader = new usbreader();
        rfidreader.TryUSB(this);

    }

    public void toast(String Message){
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //case R.id.login:
                /*if(!rfidreader.Initial(MainActivity.this))
                {
                    utils.dialog("NFC读写器连接失败", MainActivity.this);
                    return;
                }else{
                    rfidreader.ReaderHardware.YW_SearchCardMode(rfidreader.ReaderHardware.SEARCHCARDMODE_14443A);
                    Intent intent1 = new Intent(MainActivity.this,Login.class);
                    startActivity(intent1);
                }*/
               // break;
            case R.id.signin:
                init();
                if(!rfidreader.Initial(MainActivity.this))
                {
                    utils.dialog("NFC读写器连接失败", MainActivity.this);
                    return;
                }else{
                    rfidreader.ReaderHardware.YW_SearchCardMode(rfidreader.ReaderHardware.SEARCHCARDMODE_14443A);
                    Intent intent2 = new Intent(MainActivity.this,Sign.class);
                    startActivity(intent2);

                }
                break;
            case R.id.manage:
                Intent intent3 = new Intent(MainActivity.this,IsManageActivity.class);
                startActivity(intent3);
                break;
            default:
                    break;
        }
    }
}
