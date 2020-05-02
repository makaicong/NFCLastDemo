package com.example.nfclastdemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfclastdemo.Util.DialogThridUtils;
import com.example.nfclastdemo.bean.Employee;
import com.example.nfclastdemo.bean.Holidays;
import com.example.nfclastdemo.bean.OfficialBusiness;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;

public class OfficialBusinessActivity extends AppCompatActivity implements View.OnClickListener{

    private Dialog mDialog;
    private EditText editText1;
    //private EditText editText2;
    //private EditText editText3;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private List<String> employId = new ArrayList<>();
    private Calendar calendar= Calendar.getInstance();
    private int style=5;
    private String inputdate;
    private String inputdays;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    DialogThridUtils.closeDialog(mDialog);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_business);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("出差");

        editText1 = (EditText)findViewById(R.id.official_phone);
        //editText2 = (EditText)findViewById(R.id.official_begindate);
        //editText3 = (EditText)findViewById(R.id.official_enddate);
        textView1 = findViewById(R.id.official_begindate);
        textView2 = findViewById(R.id.official_enddate);
        textView3 = findViewById(R.id.official_now);

        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);
        //Button button1 = (Button)findViewById(R.id.official_now);
        //button1.setOnClickListener(this);
    }

    public void toast(String Message){
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.official_begindate:

                new DatePickerDialog(OfficialBusinessActivity.this, style, new DatePickerDialog.OnDateSetListener() {
                    // 绑定监听器(How the parent is notified that the date is set.)
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 此处得到选择的时间，可以进行你想要的操作
                        int mon=monthOfYear+1;
                        String month1=""+mon;
                        String day1=""+dayOfMonth;

                        month1=format(month1);
                        day1=format(day1);

                        inputdate=""+year+"-"+month1+"-"+day1;
                        textView1.setText(inputdate);
                        //tv.setText("您选择了：" + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                        //Toast.makeText(OfficialBusinessActivity.this,""+year+'-'+mon+"-"+dayOfMonth,Toast.LENGTH_LONG).show();
                        //Log.e("11",""+year+'-'+mon+"-"+dayOfMonth);
                    }
                }
                        // 设置初始日期
                        , calendar.get(Calendar.YEAR)
                        , calendar.get(Calendar.MONTH)
                        , calendar.get(Calendar.DAY_OF_MONTH)).show();

                break;
            case R.id.official_enddate:

                new DatePickerDialog(OfficialBusinessActivity.this, style, new DatePickerDialog.OnDateSetListener() {
                    // 绑定监听器(How the parent is notified that the date is set.)
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 此处得到选择的时间，可以进行你想要的操作
                        int mon=monthOfYear+1;
                        String month1=""+mon;
                        String day1=""+dayOfMonth;

                        month1=format(month1);
                        day1=format(day1);

                        inputdays=""+year+"-"+month1+"-"+day1;
                        textView2.setText(inputdays);
                        //tv.setText("您选择了：" + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                        //Toast.makeText(OfficialBusinessActivity.this,""+year+'-'+mon+"-"+dayOfMonth,Toast.LENGTH_LONG).show();
                        //Log.e("11",""+year+'-'+mon+"-"+dayOfMonth);
                    }
                }
                        // 设置初始日期
                        , calendar.get(Calendar.YEAR)
                        , calendar.get(Calendar.MONTH)
                        , calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.official_now:

                //loading
                mDialog = DialogThridUtils.showWaitDialog(OfficialBusinessActivity.this, "加载中...", false, true);
                mHandler.sendEmptyMessageDelayed(1, 1000);

                final String inputphone = editText1.getText().toString();
                //final String inputdate = editText2.getText().toString();
                //final String inputdays = editText3.getText().toString();

                final String sql = "select * from Employee where phone='"+inputphone+"'";

                new BmobQuery<Employee>().doSQLQuery(sql, new SQLQueryListener<Employee>() {
                    @Override
                    public void done(BmobQueryResult<Employee> bmobQueryResult, BmobException e) {
                        if(e ==null){
                            List<Employee> list = (List<Employee>) bmobQueryResult.getResults();
                            if(list!=null && list.size()>0){
                                employId.add(list.get(0).getEmployeeId());
                            }else{
                                //Log.i("smile", "查询成功，无数据返回");
                                toast("号码不正确");
                            }
                        }else{
                            //Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                            toast("查询失败" );
                        }
                    }
                });

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(employId.size()==1){
                            OfficialBusiness officialBusiness = new OfficialBusiness();
                            //officialBusiness.setPhone(inputphone);
                            officialBusiness.setBegin(inputdate);
                            officialBusiness.setEnd(inputdays);
                            officialBusiness.setEmployeeId(employId.get(0));

                            officialBusiness.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if(e==null){
                                        toast("添加数据成功，");
                                        editText1.setText("");
                                        textView1.setText("");
                                        textView2.setText("");
                                        employId.clear();
                                        //editText2.setText("");
                                        //editText3.setText("");
                                    }else{
                                        toast("创建数据失败：" + e.getMessage());
                                    }
                                }
                            });
                        }
                    }
                },1000);

                break;
            default:
                break;
        }
    }

    public String format(String data){
        if(data.length()==1){
            data="0"+data;
        }

        return data;
    }
}
