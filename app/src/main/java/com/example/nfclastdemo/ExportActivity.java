package com.example.nfclastdemo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.util.StateSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfclastdemo.Util.DialogThridUtils;
import com.example.nfclastdemo.Util.ExcelUtil;
import com.example.nfclastdemo.bean.Attendence;
import com.example.nfclastdemo.bean.Employee;
import com.example.nfclastdemo.bean.Holidays;
import com.example.nfclastdemo.bean.OfficialBusiness;
import com.example.nfclastdemo.bean.Statistics;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;

public class ExportActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private TextView textView;
    private TextView textView1;
    private Dialog mDialog;
    private AlertDialog alertDialog;
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private int REQUEST_PERMISSION_CODE = 1000;

    private String filePath1 = "/sdcard/AndroidExcelDemo";
    private String fileUrl;

    //private List<String> name = new ArrayList<>();
    private List<String> employeeId = new ArrayList<>();
    private List<String> date = new ArrayList<>();
    private List<String> is_complete = new ArrayList<>();
    //private List<String> phone = new ArrayList<>();
    private String data[];//remark
    private String name[];
    private String department[];
    private String manage_month;
    private String manage_year;
    private String targetDepartment;

    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private List<String> data1;
    private List<String> data2;
    private List<String> data3;
    private ArrayAdapter adapter1;
    private ArrayAdapter adapter2;
    private ArrayAdapter adapter3;

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

    private void requestPermission() {

        if (Build.VERSION.SDK_INT > 23) {
            if (ContextCompat.checkSelfPermission(ExportActivity.this,
                    permissions[0])
                    == PackageManager.PERMISSION_GRANTED) {
                //授予权限
                Log.i("requestPermission:", "用户之前已经授予了权限！");
            } else {
                //未获得权限
                Log.i("requestPermission:", "未获得权限，现在申请！");
                requestPermissions(permissions
                        , REQUEST_PERMISSION_CODE);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("具体出勤情况");

        requestPermission();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        textView = findViewById(R.id.exceladdress);
        textView1 = findViewById(R.id.expoernow);

        textView1.setOnClickListener(this);

        spinner1 = (Spinner)findViewById(R.id.spin_year);
        spinner2 = (Spinner)findViewById(R.id.spin_month);
        spinner3 = (Spinner)findViewById(R.id.spin_department);
        data1 = new ArrayList<String>();
        data2 = new ArrayList<String>();
        data3 = new ArrayList<String>();

        for(int i=2020;i<2100;i++){
            data1.add(""+i);
        }
        data2.add("");
        for(int i=1;i<13;i++){
            String temp = ""+i;
            if(temp.length()==1){
                temp = "0"+temp;
            }
            data2.add(temp);
        }

        data3.add("");
        data3.add("部门一");
        data3.add("部门二");
        data3.add("部门三");
        data3.add("部门四");

        //2、未下来列表定义一个数组适配器
        adapter1=new ArrayAdapter(this,android.R.layout.simple_list_item_1,data1);
        adapter2=new ArrayAdapter(this,android.R.layout.simple_list_item_1,data2);
        adapter3=new ArrayAdapter(this,android.R.layout.simple_list_item_1,data3);
        //3、为适配器设置下拉菜单的样式
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //4、将适配器配置到下拉列表上
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);
        //5、给下拉菜单设置监听事件
        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        spinner3.setOnItemSelectedListener(this);

        //exportExcel(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("onPermissionsResult:", "权限" + permissions[0] + "申请成功");
                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {
                Log.i("onPermissionsResult:", "用户拒绝了权限申请");
                AlertDialog.Builder builder = new AlertDialog.Builder(ExportActivity.this);
                builder.setTitle("permission")
                        .setMessage("点击允许才可以使用我们的app哦")
                        .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (alertDialog != null && alertDialog.isShowing()) {
                                    alertDialog.dismiss();
                                }
                                ActivityCompat.requestPermissions(ExportActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }
                        });
                alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }
    }

    private void exportExcel(final Context context) {

        //loading
        mDialog = DialogThridUtils.showWaitDialog(ExportActivity.this, "加载中...", false, true);
        mHandler.sendEmptyMessageDelayed(1, 2000);

        File file = new File(filePath1);
        if (!file.exists()) {
            file.mkdirs();
        }

        final String[] title = {"编号","姓名", "日期", "是否全勤","备注"};
        final String sheetName = "demoSheetName";

        //2222222
        final List<Statistics> statistics = new ArrayList<>();

        //333333333
        //final String sql = "select * from Attendence";
        String sql;
        Log.e("fff",manage_year+"-"+manage_month);
        if(manage_month.isEmpty()){
            sql = "select * from Attendence where year ='"+manage_year+"' ";
        }else{
            sql = "select * from Attendence where month ='"+manage_month+"' and year='"+manage_year+"'";
        }

        new BmobQuery<Attendence>().doSQLQuery(sql, new SQLQueryListener<Attendence>() {
            @Override
            public void done(BmobQueryResult<Attendence> bmobQueryResult, BmobException e) {
                if(e ==null){
                    //list--出勤表
                    final List<Attendence> list = (List<Attendence>) bmobQueryResult.getResults();

                    if(list!=null && list.size()>0){
                        //toast("此卡已注册" );
                        for(int i=0;i<list.size();i++){

                            Log.e("666",""+list.size() );

                            String days = list.get(i).getYear()+"-"+list.get(i).getMonth()+"-"+list.get(i).getDay();
                            employeeId.add(list.get(i).getEmployeeId());
                            //name.add(list.get(i).getName());
                            date.add(days);
                            //date.add(list.get(i).getDate());
                            //phone.add(list.get(i).getPhone());

                            if(list.get(i).getMorning_in().isEmpty()||list.get(i).getMorning_out().isEmpty()||
                                    list.get(i).getAfternoon_in().isEmpty()||list.get(i).getAfternoon_out().isEmpty()){
                                is_complete.add("否");
                            }else{
                                //statistics1.setIs_complete("是");
                                is_complete.add("是");
                            }
                            Log.e("777",""+is_complete.size() );
                        }
                    }else{
                        //Log.i("smile", "查询成功，无数据返回");
                    }
                    //Log.e("eeeee",employeeId.size()+"");
                }else{
                    //Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                    toast("查询失败5555551" );
                }
            }

        });

        /*if(is_complete.size()==0){
            toast("该日期无打卡数据");
            return;
        }*/

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /*Log.e("222",employeeId.get(0));
                Log.e("333",is_complete.get(0));
                Log.e("111",date.get(0));*/

                Log.e("222",employeeId.size()+"");
                Log.e("333",is_complete.size()+"");
                Log.e("111",date.size()+"");

         //       Log.e("222",name.get(0)+"-"+employeeId.get(0)+"-"+is_complete.get(0));
                //Log.e("333",name.get(1)+"-"+employeeId.get(1)+"-"+is_complete.get(1));
                //Log.e("111",name.get(2)+"-"+employeeId.get(2)+"-"+is_complete.get(2));

                data = new String[is_complete.size()];
                name = new String[is_complete.size()];
                department = new String[is_complete.size()];

                for(int i=0;i<is_complete.size();i++){
                    name[i]="1";
                    department[i]="1";
                    final int j=i;
                    findName(j);
                }

                //final List<String> flag = new ArrayList<>();
                for(int i=0;i<is_complete.size();i++) {
                    final int j = i;

                    if (is_complete.get(j).equals("否")) {
                        data[j] = "无请假无出差";//非全勤且无请假出差

                        checkRemark1(j);
                        checkRemark2(j);

                    } else {
                        //remark.add("-");
                        data[j] = "无";//全勤
                    }
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Log.e("6666",name[0]+" "+name[1]+" "+name[2]+" "+name[3]+" "+name[4]+" "+data.length+"");

                        for(int i=0;i<is_complete.size();i++){

                            if(targetDepartment.equals("")){
                                Statistics statistics1 = new Statistics(employeeId.get(i),name[i],date.get(i),is_complete.get(i),data[i]);
                                statistics.add(statistics1);
                            }else{
                                if(department[i].equals(targetDepartment)){
                                    Statistics statistics1 = new Statistics(employeeId.get(i),name[i],date.get(i),is_complete.get(i),data[i]);
                                    statistics.add(statistics1);
                                }
                            }


                        }

                        String excelFileName = "/deo.xls";
                        String filePath = "/sdcard/AndroidExcelDemo";
                        filePath = filePath + excelFileName;

                        ExcelUtil.initExcel(filePath, sheetName, title);

                        //ExcelUtil.writeObjListToExcel(demoBeanList, filePath, context);//1111
                        ExcelUtil.writeObjListToExcel(statistics, filePath, context);//2222

                        //name.clear();
                        employeeId.clear();
                        is_complete.clear();
                        date.clear();
                        data=null;
                        name=null;
                        department=null;

                        textView.setText("excel已导出至：" + filePath);

                        /*if(flag.size()==0){
                            String excelFileName = "/deo.xls";
                            String filePath = "/sdcard/AndroidExcelDemo";
                            filePath = filePath + excelFileName;

                            ExcelUtil.initExcel(filePath, sheetName, title);

                            //ExcelUtil.writeObjListToExcel(demoBeanList, filePath, context);//1111
                            ExcelUtil.writeObjListToExcel(statistics, filePath, context);//2222

                            //name.clear();
                            employeeId.clear();
                            is_complete.clear();
                            date.clear();
                            data=null;
                            name=null;
                            department=null;

                            textView.setText("excel已导出至：" + filePath);
                            flag.add("1");
                        }*/

                    }
                },1000);

            }
        },1000);
    }

    public void findName(final int t){
        final String sql = "select * from Employee where employeeId='"+employeeId.get(t)+"'";

        new BmobQuery<Employee>().doSQLQuery(sql, new SQLQueryListener<Employee>() {
            @Override
            public void done(BmobQueryResult<Employee> bmobQueryResult, BmobException e) {
                if(e==null){
                    List<Employee> list11 = (List<Employee>)bmobQueryResult.getResults();
                    if(list11!=null && list11.size()>0){
                        name[t]=list11.get(0).getName();
                        department[t]=list11.get(0).getDepartment();
                    }else {

                    }
                }else{
                    toast("查询失败22" );
                }
            }
        });

    }

    public void checkRemark2(final int t){
        //查请假表
        //判断假期时间待处理++++++++++++++++
        //final String sqlll = "select * from Holidays where phone='"+phone.get(t)+"'";
        final String sqlll = "select * from Holidays where employeeId='"+employeeId.get(t)+"'";

        new BmobQuery<Holidays>().doSQLQuery(sqlll, new SQLQueryListener<Holidays>() {
            @Override
            public void done(BmobQueryResult<Holidays> bmobQueryResult, BmobException e) {
                if(e==null){
                    //list11--请假表
                    List<Holidays> list11 = (List<Holidays>)bmobQueryResult.getResults();
                    if(list11!=null && list11.size()>0){
                        //statistics1.setRemark("出差");

                        for(int i=0;i<list11.size();i++){
                            String startdate = list11.get(i).getBegin();//出差起始日期
                            String enddate = list11.get(i).getEnd();//出差结束日期
                            int a = date.get(t).compareTo(startdate);
                            int b = date.get(t).compareTo(enddate);

                            if(a>=0&&b<=0){
                                //remark.add("请假");
                                data[t]="请假";
                                //break;
                            }
                        }
                        //remark.add("请假");
                    }else{
                        //statistics1.setRemark("");
                        //remark.add("");
                    }
                }else{
                    toast("查询失败1111" );
                }
            }
        });
    }

    public void checkRemark1(final int t){
        //查出差表
        //判断假期时间待处理++++++++++++++++
        //final String sqlll = "select * from OfficialBusiness where phone='"+phone.get(t)+"'";
        final String sqlll = "select * from OfficialBusiness where employeeId='"+employeeId.get(t)+"'";

        new BmobQuery<OfficialBusiness>().doSQLQuery(sqlll, new SQLQueryListener<OfficialBusiness>() {
            @Override
            public void done(BmobQueryResult<OfficialBusiness> bmobQueryResult, BmobException e) {
                if(e==null){
                    //list11--出差表
                    List<OfficialBusiness> list11 = (List<OfficialBusiness>)bmobQueryResult.getResults();
                    if(list11!=null && list11.size()>0){
                        //statistics1.setRemark("出差");
                        for(int i=0;i<list11.size();i++){
                            String startdate = list11.get(i).getBegin();//出差起始日期
                            String enddate = list11.get(i).getEnd();//出差结束日期
                            int a = date.get(t).compareTo(startdate);
                            int b = date.get(t).compareTo(enddate);

                            //Log.e("qqq",phone.get(t)+"-"+date.get(i)+"-"+startdate+"-"+enddate+"-"+a+"-"+b);

                            if(a>=0&&b<=0){
                                //remark.add("出差");
                                data[t]="出差";
                               // break;
                            }
                        }
                        //remark.add("出差");
                    }else{
                        //statistics1.setRemark("");
                        //remark.add("");
                    }
                }else{
                    toast("查询失败1111" );
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.expoernow:

                Calendar calendar = Calendar.getInstance();
                String temp_year = ""+calendar.get(Calendar.YEAR);
                int temp_mon=calendar.get(Calendar.MONTH)+1;
                String temp_month = ""+temp_mon;
                //String temp_month = ""+calendar.get(Calendar.MONTH)+1;

                if(temp_month.length()==1){
                    temp_month = "0"+temp_month;
                }
                String temp_date = temp_year+"-"+temp_month;

                Log.e("++", temp_date);
                Log.e("++", manage_year+"-"+manage_month);

                int a = temp_date.compareTo(manage_year+"-"+manage_month);
                Log.e("++", ""+a);

                if(a<0){
                    toast("请选择正确的时间");
                }else{
                    exportExcel(this);
                }


                break;

                default:
                    break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                //etUrl.setText(uri.getPath().toString());
                //fileUrl = etUrl.getText().toString();
                fileUrl = uri.getPath().toString();
                Log.e("wwwwww", fileUrl);
            }
        }
    }

    public void toast(String Message){
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String content = parent.getItemAtPosition(position).toString();

        switch(parent.getId()){
            case R.id.spin_year:
                manage_year = content;
                break;
            case R.id.spin_month:
                manage_month = content;
                break;
            case R.id.spin_department:
                targetDepartment = content;
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
