package com.example.nfclastdemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfclastdemo.bean.Manager;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class IsManageActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editText1;
    private EditText editText2;
    private TextView textView;
    //private Button button1;
    //private Button button2;
    //private Context mContext;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_manage);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("登录");

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        checkBox = findViewById(R.id.remember_pass);
        boolean isRemember = preferences.getBoolean("remember_password",false);

        editText1 = (EditText)findViewById(R.id.phonenumber);
        editText2 = (EditText)findViewById(R.id.password);
        //button1 = (Button)findViewById(R.id.verify);
        //button2 = (Button)findViewById(R.id.send);
        //button1.setOnClickListener(this);
        //button2.setOnClickListener(this);
        textView = (TextView)findViewById(R.id.verify);
        textView.setOnClickListener(this);

        if(isRemember){
            String account = preferences.getString("account","");
            String password = preferences.getString("password","");
            editText1.setText(account);
            editText2.setText(password);
            checkBox.setChecked(true);
        }

       // mContext=this;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.verify:

                check();
                //finish();
                break;

            /*case R.id.send:
                //Permissionx();
                //sendInfo();
                break;*/
            default:
                break;
        }
    }

    public void check(){
        final String phonenumber = editText1.getText().toString();
        final String passwordnum = editText2.getText().toString();

        final String sql = "select * from Manager where phone='"+phonenumber+"' and password ='"+passwordnum+"'";
        Log.e("aa",sql);

        new BmobQuery<Manager>().doSQLQuery(sql, new SQLQueryListener<Manager>() {
            @Override
            public void done(BmobQueryResult<Manager> bmobQueryResult, BmobException e) {
                if(e ==null){
                    List<Manager> list = (List<Manager>) bmobQueryResult.getResults();
                    if(list!=null && list.size()>0){
                        //toast("此卡已注册" );
                        toast(list.get(0).getName()+"你好");

                        editor = preferences.edit();
                        if(checkBox.isChecked()){
                            editor.putBoolean("remember_password",true);
                            editor.putString("account",phonenumber);
                            editor.putString("password",passwordnum);
                        }else{
                            editor.clear();
                        }
                        editor.apply();

                        Intent intent1 = new Intent(IsManageActivity.this,ManageActivity.class);
                        intent1.putExtra("isBoss",list.get(0).getIsBoss());
                        startActivity(intent1);

                        finish();
                    }else{
                        //Log.i("smile", "查询成功，无数据返回");
                        toast("你不是管理员");
                    }
                }else{
                    Log.e("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                    toast("查询失败" );

                }
            }
        });
    }


    public void toast(String Message){
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }

}
