package com.example.nfclastdemo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nfclastdemo.adapter.listViewAdapter;
import com.example.nfclastdemo.bean.Employee;
//import com.example.nfclastdemo.bean.iconInformation;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class EmployeeDetailActivity extends AppCompatActivity
{
    private SearchView searchView;
    private ListView listView;

    //private List<iconInformation> list;
    //private List<iconInformation> findList;
    private List<Employee> list;
    private List<Employee> findList;

    private List<String> nameList;
    private listViewAdapter adapter;
    private listViewAdapter findAdapter;
    //private Bitmap bitmap;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 123:
                    adapter = new listViewAdapter(EmployeeDetailActivity.this, list);
                    listView.setAdapter(adapter);
                    for(int i = 0; i < list.size(); i++) {
                        //iconInformation information = list.get(i);
                        //nameList.add(information.getName());
                        Employee employee = list.get(i);
                        nameList.add(employee.getName());
                    }
                    break;
            }
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("员工出勤");

        setContentView(R.layout.activity_employee_detail);
        searchView = (SearchView) findViewById(R.id.employee_search);
        listView = (ListView) findViewById(R.id.listView);

        //findList = new ArrayList<iconInformation>();
        findList = new ArrayList<Employee>();
        list = new ArrayList<Employee>();
        nameList = new ArrayList<String>();

        /**
         * 默认情况下是没提交搜索的按钮，所以用户必须在键盘上按下"enter"键来提交搜索.你可以同过setSubmitButtonEnabled(
         * true)来添加一个提交按钮（"submit" button)
         * 设置true后，右边会出现一个箭头按钮。如果用户没有输入，就不会触发提交（submit）事件
         */
        searchView.setSubmitButtonEnabled(false);
        searchView.setIconified(false);

        init();

        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },2000);*/

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //输入完成后，提交时触发的方法，一般情况是点击输入法中的搜索按钮才会触发，表示现在正式提交了
            public boolean onQueryTextSubmit(String query) {

                /*if(TextUtils.isEmpty(query)) {
                    Toast.makeText(MainActivity.this, "请输入查找内容！", Toast.LENGTH_SHORT).show();
                    listView.setAdapter(adapter);
                } else {
                    findList.clear();
                    for(int i = 0; i < list.size(); i++) {
                        iconInformation information = list.get(i);
                        if(information.getName().equals(query)) {
                            findList.add(information);
                            break;
                        }
                    }
                    if(findList.size() == 0) {
                        Toast.makeText(MainActivity.this, "查找的商品不在列表中", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, "查找成功", Toast.LENGTH_SHORT).show();
                        findAdapter = new listViewAdapter(MainActivity.this, findList);
                        listView.setAdapter(findAdapter);
                    }
                }*/
                return true;
            }

            //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)) {
                    listView.setAdapter(adapter);
                } else {
                    findList.clear();
                    for(int i = 0; i < list.size(); i++) {

                        /*iconInformation information = list.get(i);
                        if(information.getName().contains(newText)) {
                            findList.add(information);
                        }*/
                        Employee employee = list.get(i);
                        if(employee.getName().contains(newText)){
                            findList.add(employee);
                        }

                    }
                    findAdapter = new listViewAdapter(EmployeeDetailActivity.this, findList);
                    findAdapter.notifyDataSetChanged();
                    listView.setAdapter(findAdapter);
                }
                return true;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //iconInformation information;
                Employee employee;
                if(findList.size()==0){
                    //information = list.get(position);
                    employee = list.get(position);
                }else{
                    //information = findList.get(position);
                    employee = findList.get(position);
                }
                Toast.makeText(EmployeeDetailActivity.this,employee.getName(),Toast.LENGTH_LONG).show();



                Intent intent1 = new Intent(EmployeeDetailActivity.this,ShowDetailActivity.class);
                intent1.putExtra("data111",employee.getEmployeeId());
                intent1.putExtra("data222",employee.getName());
                intent1.putExtra("data333",employee.getSex());
                intent1.putExtra("data444",employee.getAge());
                intent1.putExtra("data555",employee.getPhone());
                intent1.putExtra("data666",employee.getDepartment());
                startActivity(intent1);

                findList.clear();
                searchView.setQuery("",false);

            }
        });

    }

    public void init(){
        final String sql = "select * from Employee";

        new BmobQuery<Employee>().doSQLQuery(sql, new SQLQueryListener<Employee>() {
            @Override
            public void done(BmobQueryResult<Employee> bmobQueryResult, BmobException e) {
                if(e==null){
                    //list11--请假表
                    List<Employee> list11 = (List<Employee>)bmobQueryResult.getResults();
                    if(list11!=null && list11.size()>0){
                        for(int i=0;i<list11.size();i++){
                            list.add(list11.get(i));
                        }
                        Message message = new Message();
                        message.what = 123;
                        message.obj = list;
                        handler.sendMessage(message);
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
}


