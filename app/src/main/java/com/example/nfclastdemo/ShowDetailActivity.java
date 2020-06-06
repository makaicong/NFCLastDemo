package com.example.nfclastdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfclastdemo.Util.DialogThridUtils;
import com.example.nfclastdemo.adapter.FruitAdapter;
import com.example.nfclastdemo.adapter.ShowAdapter;
import com.example.nfclastdemo.bean.Attendence;
import com.example.nfclastdemo.bean.Employee;
import com.example.nfclastdemo.bean.Holidays;
import com.example.nfclastdemo.bean.OfficialBusiness;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.PriorityQueue;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class ShowDetailActivity extends AppCompatActivity {

    private TextView textView;//姓名
    private TextView textView1;//迟到次数
    private TextView textView2;//请假次数
    private TextView textView3;//出差次数
    private String employeeId;
    private int count1=0;//迟到次数
    private int count2=0;//请假次数
    private int count3=0;//出差次数
    String year;
    String month;
    RecyclerView recyclerView;
    private List<Attendence> employeeShow = new ArrayList<>();
    private String flag[];
    private Dialog mDialog;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//因为不是所有的系统都可以设置颜色的，在4.4以下就不可以。。有的说4.1，所以在设置的时候要检查一下系统版本是否是4.1以上
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.color_blue));
        }
        setContentView(R.layout.activity_show_detail);

        textView=findViewById(R.id.detail_name);

        textView1=findViewById(R.id.late_days);
        textView2=findViewById(R.id.holiday_days);
        textView3=findViewById(R.id.office_days);

        Bundle info=getIntent().getExtras();
        textView.setText(info.getString("data222"));
        employeeId=info.getString("data111");

        //loading
        mDialog = DialogThridUtils.showWaitDialog(ShowDetailActivity.this, "加载中...", false, true);
        mHandler.sendEmptyMessageDelayed(1, 2000);


        Calendar calendar1 = Calendar.getInstance();
        year = ""+calendar1.get(Calendar.YEAR);
        String month1=""+(calendar1.get(Calendar.MONTH)+1);
        if(month1.length()==1){
            month="0"+month1;
        }else{
            month=month1;
        }

        recyclerView = findViewById(R.id.recycler_view_show);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        searchHoliday();
        searchOfficeday();
        searchDelayday();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("4444", flag.length+"");
                Log.e("4444", "1111");

                for(int i=0;i<flag.length;i++){
                    final int j=i;
                    if(flag[i].equals("迟到")){
                        checkRemark1(j);
                        checkRemark2(j);
                    }
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView1.setText(""+(count1-count2-count3));
                        textView2.setText(""+count2);
                        textView3.setText(""+count3);

                        ShowAdapter showAdapter = new ShowAdapter(employeeShow,flag);
                        recyclerView.setAdapter(showAdapter);
                    }
                },1000);

            }
        },1000);
    }

    public void checkRemark2(final int t){
        //查请假表
        //判断假期时间待处理++++++++++++++++
        //final String sqlll = "select * from Holidays where phone='"+phone.get(t)+"'";
        final String sqlll = "select * from Holidays where employeeId='"+employeeShow.get(t).getEmployeeId()+"'";

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
                            String date = employeeShow.get(t).getYear()+"-"+employeeShow.get(t).getMonth()+"-"+employeeShow.get(t).getDay();
                            int a = date.compareTo(startdate);
                            int b = date.compareTo(enddate);

                            if(a>=0&&b<=0){
                                //remark.add("请假");
                                flag[t]="请假";
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
        final String sqlll = "select * from OfficialBusiness where employeeId='"+employeeShow.get(t).getEmployeeId()+"'";

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
                            String date = employeeShow.get(t).getYear()+"-"+employeeShow.get(t).getMonth()+"-"+employeeShow.get(t).getDay();
                            int a = date.compareTo(startdate);
                            int b = date.compareTo(enddate);

                            //Log.e("qqq",phone.get(t)+"-"+date.get(i)+"-"+startdate+"-"+enddate+"-"+a+"-"+b);

                            if(a>=0&&b<=0){
                                //remark.add("出差");
                                flag[t]="出差";
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

    public void searchDelayday(){
        final String sql = "select * from Attendence where employeeId='"+employeeId+"'and year='"+year+"'and month='"+month+"' order by day desc";

        new BmobQuery<Attendence>().doSQLQuery(sql, new SQLQueryListener<Attendence>() {
            @Override
            public void done(BmobQueryResult<Attendence> bmobQueryResult, BmobException e) {
                if(e==null){
                    List<Attendence> list11 = (List<Attendence>)bmobQueryResult.getResults();
                    Log.e("1555",list11.size()+"");

                    if(list11!=null && list11.size()>0){
                        flag=  new String[list11.size()];

                        for(int i=0;i<list11.size();i++){
                            employeeShow.add(list11.get(i));
                            Log.e("15",list11.get(i).getAfternoon_in()+" "+list11.get(i).getMorning_in());

                            //flag[i]="-";//满勤
                            flag[i]="满勤";//满勤
                            if(list11.get(i).getMorning_in().isEmpty()||list11.get(i).getMorning_out().isEmpty()||
                            list11.get(i).getAfternoon_in().isEmpty()||list11.get(i).getAfternoon_out().isEmpty()){
                                count1++;
                                flag[i]="迟到";//缺勤
                                Log.e("152",list11.get(i).getAfternoon_in()+" "+list11.get(i).getMorning_in());
                            }

                            Log.e("12345", count1+"");
                        }
                    }else{
                        flag=  new String[0];
                    }
                }else{
                    toast("查询失败22" );
                }

            }
        });
    }

    public void searchOfficeday(){
        final String sql = "select * from OfficialBusiness where employeeId='"+employeeId+"'";
        new BmobQuery<OfficialBusiness>().doSQLQuery(sql, new SQLQueryListener<OfficialBusiness>() {
            @Override
            public void done(BmobQueryResult<OfficialBusiness> bmobQueryResult, BmobException e) {
                if(e==null){
                    List<OfficialBusiness> list11 = (List<OfficialBusiness>)bmobQueryResult.getResults();
                    if(list11!=null && list11.size()>0){

                        Log.e("111",list11.size()+"");

                        for(int i=0;i<list11.size();i++){
                            String begin = list11.get(i).getBegin();
                            String end = list11.get(i).getEnd();

                            String temp = year+"-"+month;
                            Log.e("222",temp);

                            if(begin.contains(temp)){
                                if(end.contains(temp)){
                                    try {
                                        count3 = count3+daysBetween(begin,end);
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                }else{
                                    String a="";
                                    if(month.equals("02")){
                                        int yearNum=Integer.parseInt(year);
                                        if((yearNum%4==0&&yearNum%100!=0)||(yearNum%400==0)){//闰年
                                            a = year+"-"+month+"-29";
                                        }else{
                                            a = year+"-"+month+"-28";
                                        }
                                    }else if(month.equals("01")||month.equals("03")||month.equals("05")||
                                            month.equals("07")||month.equals("08")||month.equals("10")||month.equals("12")){
                                        a = year+"-"+month+"-31";
                                    }else{
                                        a = year+"-"+month+"-30";
                                    }
                                    try {
                                        count3 = count3+daysBetween(begin,a);
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }else{
                                if(end.contains(temp)){
                                    String a = year+"-"+month+"-01";
                                    try {
                                        count3 = count3+daysBetween(a,end);
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                            if(count3==1){
                            }else{
                                if(count3!=0){
                                    count3++;
                                }
                            }


                            toast(""+count2);

                        }
                    }else {

                    }
                }else{
                    toast("查询失败22" );
                }
            }
        });
    }

    public void searchHoliday(){
        final String sql = "select * from Holidays where employeeId='"+employeeId+"'";

        new BmobQuery<Holidays>().doSQLQuery(sql, new SQLQueryListener<Holidays>() {
            @Override
            public void done(BmobQueryResult<Holidays> bmobQueryResult, BmobException e) {
                if(e==null){
                    List<Holidays> list11 = (List<Holidays>)bmobQueryResult.getResults();
                    if(list11!=null && list11.size()>0){

                        Log.e("111",list11.size()+"");

                        for(int i=0;i<list11.size();i++){
                            String begin = list11.get(i).getBegin();
                            String end = list11.get(i).getEnd();

                            String temp = year+"-"+month;
                            Log.e("222",temp);

                            if(begin.contains(temp)){
                                if(end.contains(temp)){
                                    try {
                                        count2 = count2+daysBetween(begin,end);
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                }else{
                                    String a="";
                                    if(month.equals("02")){
                                        int yearNum=Integer.parseInt(year);
                                        if((yearNum%4==0&&yearNum%100!=0)||(yearNum%400==0)){//闰年
                                            a = year+"-"+month+"-29";
                                        }else{
                                            a = year+"-"+month+"-28";
                                        }
                                    }else if(month.equals("01")||month.equals("03")||month.equals("05")||
                                            month.equals("07")||month.equals("08")||month.equals("10")||month.equals("12")){
                                        a = year+"-"+month+"-31";
                                    }else{
                                        a = year+"-"+month+"-30";
                                    }
                                    try {
                                        count2 = count2+daysBetween(begin,a);
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }else{
                                if(end.contains(temp)){
                                    String a = year+"-"+month+"-01";
                                    try {
                                        count2 = count2+daysBetween(a,end);
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                            if(count2==1){
                            }else{
                                if(count2!=0){
                                    count2++;
                                }
                            }


                            toast(""+count2);

                        }
                    }else {

                    }
                }else{
                    toast("查询失败22" );
                }
            }
        });
    }

    public int daysBetween(String smdate,String bdate)throws ParseException {
        if(smdate.equals(bdate)){
            return 1;
        }else{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long between_days=(time2-time1)/(1000*3600*24);

            return Integer.parseInt(String.valueOf(between_days));
        }

    }

    public void toast(String Message){
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }
}
