package com.example.nfclastdemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfclastdemo.Util.Utils;
import com.example.nfclastdemo.bean.Employee;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 信息录入
 */
public class Login extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    //private EditText editText6;
    private TextView textView;
    private boolean state;
    private String id;
    Utils utils = new Utils();

    private Spinner spinner1;
    private List<String> data1;
    private ArrayAdapter adapter1;
    private String login_department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("添加员工信息");

        spinner1 = (Spinner)findViewById(R.id.spin_login_department);
        data1 = new ArrayList<String>();
        data1.add("部门一");
        data1.add("部门二");
        data1.add("部门三");
        data1.add("部门四");
        adapter1=new ArrayAdapter(this,android.R.layout.simple_list_item_1,data1);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);

        editText1 = (EditText)findViewById(R.id.employeeId);
        editText2 = (EditText)findViewById(R.id.name);
        editText3 = (EditText)findViewById(R.id.sex);
        editText4 = (EditText)findViewById(R.id.age);
        editText5 = (EditText)findViewById(R.id.phone);
        //editText6 = (EditText)findViewById(R.id.department);

        textView = findViewById(R.id.register);
        textView.setOnClickListener(this);
        /*Button button1 = (Button)findViewById(R.id.register);
        button1.setOnClickListener(this);*/

        state = false;
        init();

    }

    public void toast(String Message){
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }

    public void init(){
        Thread thread = new Thread(new Runnable() {
            byte[] CardNo;
            @Override
            public void run()
            {
                while(!state)
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if( ManageActivity.rfidreader.ISO14443A.YW_RequestCard(ManageActivity.rfidreader.ISO14443A.REQUESTMODE_ACTIVE)<0)continue;
                    CardNo=ManageActivity.rfidreader.ISO14443A.YW_AntiCollideAndSelect(ManageActivity.rfidreader.ISO14443A.MULTIMODE_ONE);

                    if(CardNo==null){
                        continue;
                    }
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            id=utils.bytesToHexString(CardNo) + "";
                            editText1.setText(utils.bytesToHexString(CardNo) + "");
                            //Toast.makeText(Login.this, utils.bytesToHexString(CardNo) + "", Toast.LENGTH_SHORT).show();
                            Toast.makeText(Login.this, id, Toast.LENGTH_SHORT).show();
                        }
                    });
                    ManageActivity.rfidreader.ISO14443A.YW_CardHalt();
                    ManageActivity.rfidreader.ReaderHardware.YW_Buzzer(5, 5, 1);
                }

            }
        });
        thread.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                final String inputname = editText2.getText().toString();
                final String inputsex = editText3.getText().toString();
                final String inputage = editText4.getText().toString();
                final String inputphone = editText5.getText().toString();
                //final String department = editText6.getText().toString();

                upload(id,inputname,inputsex,inputage,inputphone,login_department);
             break;
             default:
                 break;
        }
    }


    public void upload(String id,String name,String sex,String age,String phone,String department){

        if(id.isEmpty()||name.isEmpty()||sex.isEmpty()||age.isEmpty()||phone.isEmpty()){
            toast("请将信息填写完整");
        }else{
            final Employee employee = new Employee();

            employee.setEmployeeId(id);
            employee.setName(name);
            employee.setSex(sex);
            employee.setAge(age);
            employee.setPhone(phone);
            employee.setDepartment(department);
            //employee.setIsmanage("0");

            final String sql = "select * from Employee where employeeId='"+id+"'";
            new BmobQuery<Employee>().doSQLQuery(sql, new SQLQueryListener<Employee>() {
                @Override
                public void done(BmobQueryResult<Employee> bmobQueryResult, BmobException e) {
                    if(e ==null){
                        List<Employee> list = (List<Employee>) bmobQueryResult.getResults();
                        if(list!=null && list.size()>0){
                            toast("此卡已注册" );
                        }else{
                            //Log.i("smile", "查询成功，无数据返回");
                            employee.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if(e==null){
                                        toast("添加数据成功，");
                                        editText1.setText("");
                                        editText2.setText("");
                                        editText3.setText("");
                                        editText4.setText("");
                                        editText5.setText("");
                                        //editText6.setText("");
                                    }else{
                                        toast("创建数据失败：" + e.getMessage());
                                    }
                                }
                            });
                        }
                    }else{
                        //Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                        //toast("查询失败" );
                        employee.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e==null){
                                    toast("添加数据成功，");
                                    editText1.setText("");
                                    editText2.setText("");
                                    editText3.setText("");
                                    editText4.setText("");
                                    editText5.setText("");
                                    //editText6.setText("");
                                }else{
                                    toast("创建数据失败：" + e.getMessage());
                                }
                            }
                        });
                    }
                }
            });
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String content = parent.getItemAtPosition(position).toString();

        switch(parent.getId()){
            case R.id.spin_login_department:
                login_department=content;
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        state=true;
    }
}
