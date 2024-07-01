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
    Context context;
    ArrayList<ContactItem> contactItems;
    OnItemClickListener clickListener;
    OnItemLongClickListener longClickListener;  // 길게 누르기 리스너 추가

    public ContactItem getItem(int position) {
        return contactItems.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, View view, int position);
    }

    public interface OnItemLongClickListener {  // 길게 누르기 리스너 인터페이스 추가
        void onItemLongClick(ViewHolder holder, View view, int position);
    }

    public ContactAdapter(Context context, ArrayList<ContactItem> contactItems) {
        this.context = context;
        this.contactItems = contactItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(itemView, clickListener, longClickListener);  // 생성자에 리스너 전달
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactItem item = contactItems.get(position);

        holder.name.setText(item.getName() + " 교수님");
        holder.department.setText(item.getDepartment());
        holder.phoneNumber.setText(item.getPhoneNumber());
        holder.email.setText(item.getEmail());
    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {  // 길게 누르기 리스너 설정 메서드
        this.longClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, department, phoneNumber, email;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener clickListener, final OnItemLongClickListener longClickListener) {  // 생성자에서 리스너 전달받음
            super(itemView);

            name = itemView.findViewById(R.id.name);
            department = itemView.findViewById(R.id.department);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            email = itemView.findViewById(R.id.email);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (clickListener != null && position != RecyclerView.NO_POSITION) {
                        clickListener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {  // 길게 누르기 리스너 설정
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if (longClickListener != null && position != RecyclerView.NO_POSITION) {
                        longClickListener.onItemLongClick(ViewHolder.this, view, position);
                        return true;  // 이벤트 소비
                    }
                    return false;
                }
            });
        }
    }
}