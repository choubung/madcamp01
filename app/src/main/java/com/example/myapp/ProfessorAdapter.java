//package com.example.myapp;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Filterable;
//import android.widget.TextView;
//
//import java.util.List;
//import java.util.logging.Filter;
//import java.util.logging.LogRecord;
//
//public class ProfessorAdapter extends BaseAdapter implements  {
//    private Context context;
//    private List<ContactEntity> originalProfessors;
//    private List<ContactEntity> filteredProfessors;
//    private LayoutInflater inflater;
//
//    public ProfessorAdapter(Context context, List<ContactEntity> professors) {
//        this.context = context;
//        this.originalProfessors = professors;
//        this.filteredProfessors = professors;
//        this.inflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        return filteredProfessors.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return filteredProfessors.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        if (view == null) {
//            view = inflater.inflate(R.layout.item_professor, viewGroup, false);
//        }
//
//        TextView nameTextView = view.findViewById(R.id.nameTextView);
//        TextView departmentTextView = view.findViewById(R.id.departmentTextView);
//
//        ContactEntity professor = filteredProfessors.get(i);
//        nameTextView.setText(professor.getName());
//        departmentTextView.setText(professor.getDepartment());
//
//        return view;
//    }
//
//
//
//    public void updateData(List<ContactEntity> newProfessors) {
//        this.originalProfessors = newProfessors;
//        this.filteredProfessors = newProfessors;
//        notifyDataSetChanged();
//    }
//}
