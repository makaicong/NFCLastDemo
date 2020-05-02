package com.example.nfclastdemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfclastdemo.Util.Utils;
import com.example.nfclastdemo.bean.Attendence;
import com.example.nfclastdemo.bean.Employee;

import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 签到
 */

public class Sign extends AppCompatActivity {

    private TextView textView;
    Utils utils = new Utils();
    private String id;
    private boolean state;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String time;
    private String date;
    private int hour1;
    private int minute1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("签到");

        textView = (TextView) findViewById(R.id.show);

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
                    if( MainActivity.rfidreader.ISO14443A.YW_RequestCard(MainActivity.rfidreader.ISO14443A.REQUESTMODE_ACTIVE)<0)continue;
                    CardNo=MainActivity.rfidreader.ISO14443A.YW_AntiCollideAndSelect(MainActivity.rfidreader.ISO14443A.MULTIMODE_ONE);

                    if(CardNo==null){
                        continue;
                    }
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            id=utils.bytesToHexString(CardNo) + "";
                            is_exist();
                            //textView.setText(utils.bytesToHexString(CardNo) + "");
                            //Toast.makeText(Login.this, utils.bytesToHexString(CardNo) + "", Toast.LENGTH_SHORT).show();
                            Toast.makeText(Sign.this, id, Toast.LENGTH_SHORT).show();
                        }
                    });
                    MainActivity.rfidreader.ISO14443A.YW_CardHalt();
                    MainActivity.rfidreader.ReaderHardware.YW_Buzzer(5, 5, 1);
                }

            }
        });
        thread.start();
    }

    public void is_exist(){
        final String sql = "select * from Employee where employeeId='"+id+"'";
        new BmobQuery<Employee>().doSQLQuery(sql, new SQLQueryListener<Employee>() {
            @Override
            public void done(BmobQueryResult<Employee> bmobQueryResult, BmobException e) {
                if(e ==null){
                    List<Employee> list = (List<Employee>) bmobQueryResult.getResults();
                    if(list!=null && list.size()>0){
                        signin();
                    }else{
                        //Log.i("smile", "查询成功，无数据返回");
                        toast("此卡未注册" );
                    }
                }else{
                    //Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                    toast("查询失败" );
                }
            }
        });
    }

    public String format(String data){
        if(data.length()==1){
            data="0"+data;
        }

        return data;
    }

    public void signin(){
        final Attendence attendence = new Attendence();

        Calendar calendar1 = Calendar.getInstance();
        int month1=calendar1.get(Calendar.MONTH)+1;
        year = ""+calendar1.get(Calendar.YEAR);
        //month = ""+calendar1.get(Calendar.MONTH);
        day = ""+calendar1.get(Calendar.DAY_OF_MONTH);
        hour = ""+calendar1.get(Calendar.HOUR_OF_DAY);
        minute = ""+calendar1.get(Calendar.MINUTE);
        hour1 = calendar1.get(Calendar.HOUR_OF_DAY);
        minute1 = calendar1.get(Calendar.MINUTE);

        month=format(""+month1);
        day=format(day);
        hour=format(hour);
        minute=format(minute);

        date=year+"-"+month+"-"+day;
        time=hour+"-"+minute;

        final String sql = "select * from Attendence where employeeId='"+id+"' and date='"+date+"'" ;
        //textView.setText(sql);
        new BmobQuery<Attendence>().doSQLQuery(sql, new SQLQueryListener<Attendence>() {
            @Override
            public void done(BmobQueryResult<Attendence> bmobQueryResult, BmobException e) {
                if(e ==null){
                    List<Attendence> list = (List<Attendence>) bmobQueryResult.getResults();
                    if(list!=null && list.size()>0){
                        //表存在，只要修改数据
                        //toast("创建过了");
                        String ObjectID=list.get(0).getObjectId();

                        if(hour1==11&&minute1<=30){
                            attendence.setMorning_out(time);
                            attendence.update(ObjectID, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        //toast("更新成功:"+p2.getUpdatedAt());
                                        toast("签到成功");
                                    }else{
                                        toast("更新失败：" + e.getMessage());
                                    }
                                }
                            });
                        }else if(hour1==14&&minute1<=30){
                            attendence.setAfternoon_in(time);
                            attendence.update(ObjectID, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        //toast("更新成功:"+p2.getUpdatedAt());
                                        toast("签到成功");
                                    }else{
                                        toast("更新失败：" + e.getMessage());
                                    }
                                }
                            });
                        }else if(hour1==18&&minute1<=30){
                            attendence.setAfternoon_out(time);
                            attendence.update(ObjectID, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        //toast("更新成功:"+p2.getUpdatedAt());
                                        toast("签到成功");
                                    }else{
                                        toast("更新失败：" + e.getMessage());
                                    }
                                }
                            });
                        }else{
                            toast("现在不是打卡时间");
                        }

                    }else{
                        //Log.i("smile", "查询成功，无数据返回");
                        attendence.setEmployeeId(id);
                        //attendence.setDate(date);
                        //attendence.setName(name);
                        //attendence.setPhone(phone);
                        attendence.setMonth(month);
                        attendence.setYear(year);
                        attendence.setDay(day);

                        if(hour1==14&&minute1<=60){
                            attendence.setAfternoon_in("");
                            attendence.setAfternoon_out("");
                            attendence.setMorning_in(time);
                            attendence.setMorning_out("");

                            attendence.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if(e==null){
                                        //toast("添加数据成功，");
                                        toast("打卡成功");
                                    }else{
                                        toast("创建数据失败：" + e.getMessage());
                                    }
                                }
                            });
                        }else if(hour1==11&&minute1<=30){
                            attendence.setAfternoon_in("");
                            attendence.setAfternoon_out("");
                            attendence.setMorning_in("");
                            attendence.setMorning_out(time);

                            attendence.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if(e==null){
                                        //toast("添加数据成功，");
                                        toast("打卡成功");
                                    }else{
                                        toast("创建数据失败：" + e.getMessage());
                                    }
                                }
                            });
                        }else if(hour1==14&&minute1<=30){
                            attendence.setAfternoon_in(time);
                            attendence.setAfternoon_out("");
                            attendence.setMorning_in("");
                            attendence.setMorning_out("");

                            attendence.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if(e==null){
                                        //toast("添加数据成功，");
                                        toast("打卡成功");
                                    }else{
                                        toast("创建数据失败：" + e.getMessage());
                                    }
                                }
                            });
                        }else if(hour1==18&&minute1<=30){
                            attendence.setAfternoon_in("");
                            attendence.setAfternoon_out(time);
                            attendence.setMorning_in("");
                            attendence.setMorning_out("");

                            attendence.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if(e==null){
                                        //toast("添加数据成功，");
                                        toast("打卡成功");
                                    }else{
                                        toast("创建数据失败：" + e.getMessage());
                                    }
                                }
                            });
                        }else{
                            toast("现在不是打卡时间");

                        }

                    }
                }else{
                    //Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                    //表需要存在
                    toast("查询失败" );
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        state=true;
    }
}
