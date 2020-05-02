package com.example.nfclastdemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfclastdemo.bean.Employee;
import com.example.nfclastdemo.bean.Manager;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;

public class ManagerSettingActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private TextView textView1;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("设置管理员");

        editText1 = (EditText)findViewById(R.id.manager_setting_phone);
        editText2 = (EditText)findViewById(R.id.manager_setting_name);
        editText3 = (EditText)findViewById(R.id.manager_setting_password);

        textView1 = findViewById(R.id.manager_setting_check);
        textView2 = findViewById(R.id.manager_setting_register);
        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        /*Button button1 = (Button)findViewById(R.id.manager_setting_register);
        Button button2 = (Button)findViewById(R.id.manager_setting_check);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.manager_setting_check:

                check();

                break;
            case R.id.manager_setting_register:

                register();

                break;
                default:
                    break;
        }
    }

    public void register(){
        String phone = editText1.getText().toString();
        String name = editText2.getText().toString();
        String password = editText3.getText().toString();

        Manager manager = new Manager();

        manager.setIsBoss("0");
        manager.setName(name);
        manager.setPassword(password);
        manager.setPhone(phone);
        manager.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    //toast("添加数据成功，");
                    editText1.setText("");
                    editText2.setText("");
                    editText3.setText("");
                    toast("管理员设置成功");
                }else{
                    toast("创建数据失败：" + e.getMessage());
                }
            }
        });

    }

    public void check(){
        String phone = editText1.getText().toString();
        final String sql = "select * from Employee where phone='"+phone+"'";

        new BmobQuery<Employee>().doSQLQuery(sql, new SQLQueryListener<Employee>() {
            @Override
            public void done(BmobQueryResult<Employee> bmobQueryResult, BmobException e) {
                if(e==null){
                    List<Employee> list = (List<Employee>) bmobQueryResult.getResults();
                    if(list!=null && list.size()>0){
                        editText2.setText(list.get(0).getName());
                    }else{
                        //Log.i("smile", "查询成功，无数据返回");
                        toast("号码出错" );
                    }
                }else{
                    toast("查询失败" );
                }
            }
        });

    }

    public void toast(String Message){
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }
}
