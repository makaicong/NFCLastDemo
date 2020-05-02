package com.example.nfclastdemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfclastdemo.bean.Employee;
import com.example.nfclastdemo.bean.Manager;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.example.nfclastdemo.EmployeeFindActivity.broadcastReceiver1;
import static com.example.nfclastdemo.adapter.FruitAdapter.broadcastReceiver;


public class AlterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    public static int RESULT_CODE=111;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private EditText editText6;
    private String ObjectId;
    //private String name;
    //private String phone;
    private String ObjectId1;//权限表
    private String password;
    private String isBoss;
    private String name;
    //private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private int position;

    //广播
    private LocalBroadcastManager localBroadcastManager;
    private LocalBroadcastManager localBroadcastManager1;
    private IntentFilter intentFilter;
    private IntentFilter intentFilter1;

    private Spinner spinner1;
    private List<String> data1;
    private ArrayAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("修改员工信息");

        //广播
        localBroadcastManager = localBroadcastManager.getInstance(this);
        localBroadcastManager1 = localBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter1=new IntentFilter();
        intentFilter1.addAction("com.example.temp");
        intentFilter.addAction("com.example.test");
        localBroadcastManager.registerReceiver(broadcastReceiver,intentFilter);
        localBroadcastManager1.registerReceiver(broadcastReceiver1,intentFilter1);

        spinner1 = (Spinner)findViewById(R.id.spin_alter_department);
        data1 = new ArrayList<String>();
        data1.add("");
        data1.add("部门一");
        data1.add("部门二");
        data1.add("部门三");
        data1.add("部门四");
        adapter1=new ArrayAdapter(this,android.R.layout.simple_list_item_1,data1);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);

        editText1 = (EditText)findViewById(R.id.rename);
        editText2 = (EditText)findViewById(R.id.resex);
        editText3 = (EditText)findViewById(R.id.reage);
        editText4 = (EditText)findViewById(R.id.rephone);
        editText5 = (EditText)findViewById(R.id.re_isManager);
        editText6 = (EditText)findViewById(R.id.re_department);

        //textView1 = findViewById(R.id.query_alter);
        textView2 = findViewById(R.id.alter_now);
        textView3 = findViewById(R.id.delete_now);

        //textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);

        Bundle info=getIntent().getExtras();
        editText1.setText(info.getString("data2"));
        editText2.setText(info.getString("data3"));
        editText3.setText(info.getString("data4"));
        editText4.setText(info.getString("data5"));
        editText6.setText(info.getString("data6"));
        position = info.getInt("data7");//定位
        //toast(position+"");
        ObjectId=info.getString("data1");

        query1();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.query_alter:
                query();
                query1();
                break;*/
            case R.id.alter_now:
                if(editText5.getText().toString().equals("是")){
                    modify2();//修改权限表
                }
                modify1();

                break;
            case R.id.delete_now:
                delete1();//删除用户表

                //delete2();//删除权限表
                if(editText5.getText().toString().equals("是")){
                    delete2();//删除权限表
                }

                break;
            default:
                break;
        }
    }

    public void delete2(){
        Manager manager = new Manager();
        manager.setObjectId(ObjectId1);
        Log.e("ss",ObjectId1);
        manager.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.e("ss","1111");
                }else{
                    toast("删除失败：" + e.getMessage());
                }
            }
        });
    }

    public void  query1(){
        String phone = editText4.getText().toString();

        final String sql = "select * from Manager where phone='"+phone+"'";

        Log.e("s",sql);

        new BmobQuery<Manager>().doSQLQuery(sql, new SQLQueryListener<Manager>() {
            @Override
            public void done(BmobQueryResult<Manager> bmobQueryResult, BmobException e) {
                if(e ==null){
                    List<Manager> list = (List<Manager>) bmobQueryResult.getResults();
                    if(list!=null && list.size()>0){
                        ObjectId1=list.get(0).getObjectId();
                        password=list.get(0).getPassword();
                        isBoss=list.get(0).getIsBoss();
                        name=list.get(0).getName();
                        editText5.setText("是");
                    }else{
                        //Log.i("smile", "查询成功，无数据返回");
                        editText5.setText("否");
                        //toast("号码出错" );
                    }
                }else{
                    //Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                    toast("查询失败" );
                }
            }
        });
    }

    public void delete1(){
        final Employee employee = new Employee();
        employee.setObjectId(ObjectId);
        employee.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    toast("删除成功:");
                    /*editText1.setText("");
                    editText2.setText("");
                    editText3.setText("");
                    editText4.setText("");
                    editText5.setText("");
                    editText6.setText("");*/

                    Intent intent1 = new Intent("com.example.temp");
                    intent1.putExtra("data111",position);
                    localBroadcastManager1.sendBroadcast(intent1);
                    Intent intent = new Intent(AlterActivity.this,EmployeeFindActivity.class);
                    startActivity(intent);

                    finish();

                }else{
                    toast("删除失败：" + e.getMessage());
                }
            }
        });

    }

    public void modify2(){
        Manager manager = new Manager();
        final String inputname = editText1.getText().toString();
        final String inputsex = editText2.getText().toString();
        final String inputage = editText3.getText().toString();
        final String inputphone = editText4.getText().toString();
        final String inputdepartment = editText6.getText().toString();
        if(inputname.isEmpty()||inputsex.isEmpty()||inputage.isEmpty()||inputphone.isEmpty()||inputdepartment.isEmpty()){
            toast("请将信息填写完整");
        }else{
            manager.setPhone(inputphone);
            manager.setPassword(password);
            manager.setIsBoss(isBoss);
            manager.setName(name);
            manager.update(ObjectId1, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){

                    }else {
                        toast("更新失败：" + e.getMessage());
                    }
                }
            });
        }

    }

    public void modify1(){
        final Employee employee = new Employee();
        final String inputname = editText1.getText().toString();
        final String inputsex = editText2.getText().toString();
        final String inputage = editText3.getText().toString();
        final String inputphone = editText4.getText().toString();
        final String inputdepartment = editText6.getText().toString();
        if(inputname.isEmpty()||inputsex.isEmpty()||inputage.isEmpty()||inputphone.isEmpty()||inputdepartment.isEmpty()){
            toast("请将信息填写完整");
        }else{
            employee.setName(inputname);
            employee.setSex(inputsex);
            employee.setAge(inputage);
            employee.setPhone(inputphone);
            employee.setDepartment(inputdepartment);
            employee.update(ObjectId, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        toast("更新成功:");

                        Intent intent = new Intent("com.example.test");
                        intent.putExtra("data11",inputname);
                        intent.putExtra("data22",inputsex);
                        intent.putExtra("data33",inputage);
                        intent.putExtra("data44",inputphone);
                        intent.putExtra("data55",inputdepartment);
                        intent.putExtra("data66",position);

                        localBroadcastManager.sendBroadcast(intent);

                        finish();
                        //editText1.setText("");
                        //editText2.setText("");
                        //editText3.setText("");
                        //editText4.setText("");
                        //editText5.setText("");
                        //editText6.setText("");
                        //toast("签到成功");

                    }else{
                        toast("更新失败：" + e.getMessage());
                    }
                }
            });
        }
    }

 /*   public void query(){
        final String inputphone = editText4.getText().toString();

        final String sql = "select * from Employee where phone='"+inputphone+"'";
        new BmobQuery<Employee>().doSQLQuery(sql, new SQLQueryListener<Employee>() {
            @Override
            public void done(BmobQueryResult<Employee> bmobQueryResult, BmobException e) {
                if(e ==null){
                    List<Employee> list = (List<Employee>) bmobQueryResult.getResults();
                    if(list!=null && list.size()>0){
                        ObjectId=list.get(0).getObjectId();

                        editText1.setText(list.get(0).getName());
                        editText2.setText(list.get(0).getSex());
                        editText3.setText(list.get(0).getAge());
                        toast(editText1.getText().toString());
                    }else{
                        //Log.i("smile", "查询成功，无数据返回");
                        toast("号码出错" );
                    }
                }else{
                    //Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                    toast("查询失败" );
                }
            }
        });
    }*/

    public void toast(String Message){
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        localBroadcastManager1.unregisterReceiver(broadcastReceiver1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String content = parent.getItemAtPosition(position).toString();

        switch(parent.getId()){
            case R.id.spin_alter_department:
                if(content!=""){
                    editText6.setText(content);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
