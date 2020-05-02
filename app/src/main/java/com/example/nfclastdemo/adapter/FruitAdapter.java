package com.example.nfclastdemo.adapter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nfclastdemo.AlterActivity;
import com.example.nfclastdemo.EmployeeFindActivity;
import com.example.nfclastdemo.R;
import com.example.nfclastdemo.bean.Employee;

import java.util.List;


public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {
    private List<Employee> mEmployee;

    public static BroadcastReceiver broadcastReceiver;//广播
    //public static BroadcastReceiver broadcastReceiver1;//广播

    static class ViewHolder extends RecyclerView.ViewHolder {
        View fruitView;
        //ImageView fruitImage;
        TextView fruitName;

        public ViewHolder(View view) {
            super(view);
            fruitView = view;
            //fruitImage = view.findViewById(R.id.fruit_image);
            fruitName = view.findViewById(R.id.fruit_name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_item_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle info=intent.getExtras();
                int position = info.getInt("data66");

                mEmployee.get(position).setName(info.getString("data11"));
                mEmployee.get(position).setSex(info.getString("data22"));
                mEmployee.get(position).setAge(info.getString("data33"));
                mEmployee.get(position).setPhone(info.getString("data44"));
                mEmployee.get(position).setDepartment(info.getString("data55"));

                Log.e("11",mEmployee.get(position).getName()+" "+position);

                //holder.fruitName.setText(mEmployee.get(position).getName());
                notifyItemChanged(position);

            }
        };

        /*broadcastReceiver1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("sss","111");
                if (EmployeeFindActivity.class.isInstance(context)) {
                    Log.e("sss","222");
                    // 转化为activity，然后finish就行了
                    EmployeeFindActivity activity = (EmployeeFindActivity) context;
                    activity.finish();
                }
                *//*Log.e("sss","111");
                Bundle info=intent.getExtras();
                int position = info.getInt("data111");
                Log.e("ddd",""+position);
                mEmployee.remove(position);
                notifyItemRemoved(position);
                Log.e("sss","222");
                notifyItemRangeChanged(position,getItemCount()-position);*//*
            }
        };*/

        holder.fruitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Employee employee = mEmployee.get(position);

                //Log.e("22",mEmployee.get(position).getName());
                //Toast.makeText(view.getContext(), "你点击了View"+ employee.getName()+"    "+position, Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent(view.getContext(), AlterActivity.class);
                intent1.putExtra("data1",employee.getObjectId());
                intent1.putExtra("data2",employee.getName());
                intent1.putExtra("data3",employee.getSex());
                intent1.putExtra("data4",employee.getAge());
                intent1.putExtra("data5",employee.getPhone());
                intent1.putExtra("data6",employee.getDepartment());


                intent1.putExtra("data7",position);
                view.getContext().startActivity(intent1);

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee employee = mEmployee.get(position);
        holder.fruitName.setText(employee.getName());
    }

    @Override
    public int getItemCount() {
        return mEmployee.size();
    }

    public FruitAdapter(List<Employee> employees) {
        mEmployee = employees;
    }

}
