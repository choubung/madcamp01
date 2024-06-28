package com.example.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    // contactAdapter 안에 뷰홀더라고 하는 것을 정의
    Context context;
    ArrayList<ContactItem> contactItems;

    public ContactAdapter(Context context, ArrayList<ContactItem> contactItems){
        this.context = context;
        this.contactItems = contactItems;
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
        holder.name.setText(contactItems.get(position).getName());
        holder.department.setText(contactItems.get(position).getDepartment());
        holder.phoneNumber.setText(contactItems.get(position).getPhoneNumber());
        holder.email.setText(contactItems.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        //뷰홀더는 각각의 아이템을 위한 뷰를 담고 있을 수 있음
        TextView name, department, phoneNumber, email;
        //ImageView profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            department = itemView.findViewById(R.id.department);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            email = itemView.findViewById(R.id.email);
            //profile = itemView.findViewById(R.id.profile);
        }
    }
}
