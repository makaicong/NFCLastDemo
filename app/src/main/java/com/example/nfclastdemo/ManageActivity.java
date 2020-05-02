package com.example.nfclastdemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.nfclastdemo.Util.Utils;

import java.util.ArrayList;
import java.util.List;

import yoworfid.usbreader;

public class ManageActivity extends AppCompatActivity implements View.OnClickListener{

    public static usbreader rfidreader;
    Utils utils = new Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//因为不是所有的系统都可以设置颜色的，在4.4以下就不可以。。有的说4.1，所以在设置的时候要检查一下系统版本是否是4.1以上
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.color_blue));
        }*/
        setContentView(R.layout.activity_manage);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("管理");

        Intent intent = getIntent();
        String isBoss = intent.getStringExtra("isBoss");

        Button button1 = (Button)findViewById(R.id.add);
        Button button2 = (Button)findViewById(R.id.alter);
        Button button3 = (Button)findViewById(R.id.holiday);
        Button button4 = (Button)findViewById(R.id.official_business);
        Button button5 = (Button)findViewById(R.id.export);
        Button button6 = (Button)findViewById(R.id.manage_boss);
        Button button7 = (Button)findViewById(R.id.employee_detail);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);

        if(isBoss.equals("1")){
            button6.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add:
                init();

                if(!rfidreader.Initial(ManageActivity.this))
                {
                    utils.dialog("NFC读写器连接失败", ManageActivity.this);
                    return;
                }else{
                    rfidreader.ReaderHardware.YW_SearchCardMode(rfidreader.ReaderHardware.SEARCHCARDMODE_14443A);
                    Intent intent0 = new Intent(ManageActivity.this,Login.class);
                    startActivity(intent0);
                }


                break;
            case R.id.alter:
                //Intent intent1 = new Intent(ManageActivity.this,AlterActivity.class);
                Intent intent1 = new Intent(ManageActivity.this,EmployeeFindActivity.class);
                startActivity(intent1);
                break;
            case R.id.holiday:
                Intent intent2 = new Intent(ManageActivity.this,HolidayActivity.class);
                startActivity(intent2);
                break;
            case R.id.official_business:
                Intent intent3 = new Intent(ManageActivity.this,OfficialBusinessActivity.class);
                startActivity(intent3);
                break;
            case R.id.export:
                Intent intent4 = new Intent(ManageActivity.this,ExportActivity.class);
                startActivity(intent4);

                break;
            case R.id.manage_boss:
                Intent intent5 = new Intent(ManageActivity.this,ManagerSettingActivity.class);
                startActivity(intent5);
                break;
            case R.id.employee_detail:
                Intent intent6 = new Intent(ManageActivity.this,EmployeeDetailActivity.class);
                startActivity(intent6);
                break;
                default:
                    break;
        }
    }

    public void init(){
        rfidreader = new usbreader();
        rfidreader.TryUSB(this);

    }

    public void toast(String Message){
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }


}
