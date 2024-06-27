package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    // contactAdapter 안에 뷰홀더라고 하는 것을 정의
    Context context;
    ArrayList<ContactItem> items = new ArrayList<ContactItem>();

    public ContactAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.contact_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 뷰홀더가 바인딩 될 시점
        ContactItem item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(ContactItem item){
        items.add(item);
    }

    //public void addItems(ArrayList<ContactItem> items){
    //    this.items = items;
    //}

    public ContactItem getItem(int position){
        return items.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        //뷰홀더는 각각의 아이템을 위한 뷰를 담고 있을 수 있음
        TextView name, department, phoneNumber, email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            department = (TextView) itemView.findViewById(R.id.department);
            phoneNumber = (TextView) itemView.findViewById(R.id.phoneNumber);
            email = (TextView) itemView.findViewById(R.id.email);
        }

        public void setItem(ContactItem item){
            name.setText(item.getName());
            department.setText(item.getDepartment());
            phoneNumber.setText(item.getPhoneNumber());
            email.setText(item.getEmail());
        }
    }
}
