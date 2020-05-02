package com.example.nfclastdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.nfclastdemo.Util.DialogThridUtils;
import com.example.nfclastdemo.adapter.FruitAdapter;
import com.example.nfclastdemo.bean.Employee;
import com.example.nfclastdemo.bean.Holidays;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class EmployeeFindActivity extends AppCompatActivity {

    public static BroadcastReceiver broadcastReceiver1;//广播
    RecyclerView recyclerView;

    /*private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    DialogThridUtils.closeDialog(mDialog);
                    break;
            }
        }
    };*/
    //private Dialog mDialog;
    private List<Employee> employeeFind = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_find);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("修改员工信息");

        broadcastReceiver1=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };

        //loading
        //mDialog = DialogThridUtils.showWaitDialog(EmployeeFindActivity.this, "加载中...", false, true);
        //mHandler.sendEmptyMessageDelayed(1, 2000);

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        initFruits();



        /*final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                FruitAdapter adapter = new FruitAdapter(employeeFind);
                recyclerView.setAdapter(adapter);
            }
        },2000);*/


    }

    private void initFruits() {


        final String sql = "select * from Employee";

        new BmobQuery<Employee>().doSQLQuery(sql, new SQLQueryListener<Employee>() {
            @Override
            public void done(BmobQueryResult<Employee> bmobQueryResult, BmobException e) {
                if(e==null){
                    //list11--请假表
                    List<Employee> list11 = (List<Employee>)bmobQueryResult.getResults();
                    if(list11!=null && list11.size()>0){
                        for(int i=0;i<list11.size();i++){
                            employeeFind.add(list11.get(i));

                            FruitAdapter adapter = new FruitAdapter(employeeFind);
                            recyclerView.setAdapter(adapter);
                        }
                    }else{

                    }
                }else{
                    toast("查询失败1111" );
                }
            }
        });
    }

    public void toast(String Message){
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

