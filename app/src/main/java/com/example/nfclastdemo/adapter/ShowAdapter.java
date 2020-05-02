package com.example.nfclastdemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nfclastdemo.R;
import com.example.nfclastdemo.bean.Attendence;
import com.example.nfclastdemo.bean.Employee;

import java.util.List;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder>{

    private List<Attendence> mAttendence;
    private String[] isFull;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View fruitView;
        //ImageView fruitImage;
        TextView fruitName;
        TextView lateday;

        public ViewHolder(View view) {
            super(view);
            fruitView = view;
            //fruitImage = view.findViewById(R.id.fruit_image);
            fruitName = view.findViewById(R.id.show_item_date);
            lateday = view.findViewById(R.id.show_item_islate);
        }
    }

    @NonNull
    @Override
    public ShowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_item, parent, false);
        final ShowAdapter.ViewHolder holder = new ShowAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAdapter.ViewHolder holder, int position) {
        Attendence attendence = mAttendence.get(position);
        ///Employee employee = mEmployee.get(position);
        holder.fruitName.setText(attendence.getYear()+"-"+attendence.getMonth()+"-"+attendence.getDay());
        holder.lateday.setText(isFull[position]);
    }

    @Override
    public int getItemCount() {
        return mAttendence.size();
    }
    public ShowAdapter(List<Attendence> attendences,String[] flag) {
        mAttendence = attendences;
        isFull = flag;
    }
}
