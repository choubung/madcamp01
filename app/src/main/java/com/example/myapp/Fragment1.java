package com.example.myapp;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ContactAdapter adapter;
    ArrayList<ContactItem> contactItems = new ArrayList<>();
    FloatingActionButton fab;
    Boolean parserFlag = true;
    private ActivityResultLauncher<Intent> addContactLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ContactAdapter(getActivity().getApplicationContext(), contactItems);
        recyclerView.setAdapter(adapter);

        initContactData();

        adapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            // 클릭시 상세 페이지 보여줌
            @Override
            public void onItemClick(ContactAdapter.ViewHolder holder, View view, int position) {
                ContactItem item = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), ContactInfoActivity.class);
                intent.putExtra("contact", item);
                startActivity(intent);
            }
        });

        adapter.setOnItemLongClickListener(new ContactAdapter.OnItemLongClickListener() {
            // TODO 삭제 처리
            @Override
            public void onItemLongClick(ContactAdapter.ViewHolder holder, View view, int position) {
                ContactItem item = adapter.getItem(position);
                showDeleteConfirmationDialog(item);
//                ContactItem item = adapter.getItem(position);
//
//                int idx = item.getIdx(); // ContactItem에 설정된 idx 사용
//
//                // 데이터베이스에서 삭제
//                new DeleteContact(getActivity().getApplicationContext(), idx).start();
//
//                // 리스트에서 아이템 제거 및 UI 업데이트
//                contactItems.remove(position);
//                adapter.notifyItemRemoved(position);
            }
        });

        addContactLauncher = registerForActivityResult(
                // 입력창에서 넘어온 정보를 DB에 넣음
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String name = result.getData().getStringExtra("name");
                        String department = result.getData().getStringExtra("department");
                        String number = result.getData().getStringExtra("number");
                        String email = result.getData().getStringExtra("email");

                        ContactItem newItem = new ContactItem(name, department, number, email);
//                        contactItems.add(newItem);
//                        adapter.notifyItemInserted(contactItems.size() - 1);  // 변경된 위치가 마지막 인덱스이므로 -1을 해준다.
                        new AddContact(getActivity(), newItem).start();
                    }
                }
        );

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddContactActivity.class);
                addContactLauncher.launch(intent);
            }
        });

        return rootView;
    }

    // 데이터 초기화 및 가져오기
    private void initContactData() {
        if (contactItems.isEmpty()) { // 데이터가 비어있을 때만 초기화
            new GetContact(getActivity()).start();
        }
    }

    // 삭제 확인 다이얼로그 표시
    private void showDeleteConfirmationDialog(ContactItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("삭제 확인")
                .setMessage(item.getName() + "을(를) 삭제하시겠습니까?")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new DeleteContact(getActivity(), item.getIdx()).start();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    class AddContact extends Thread {
        // DB에 넣고 리사이클러 뷰 갱신
        private Context context;
        ContactItem item;

        public AddContact(Context context, ContactItem item) {
            this.context = context;
            this.item = item;
        }

        @Override
        public void run() {
            ContactEntity contact = new ContactEntity(item.getName(), item.getDepartment(), item.getPhoneNumber(), item.getEmail());
            long newIdx = ContactDatabase.getInstance(context).getContactDao().insert(contact);
            item.setIdx((int) newIdx); // 새로 추가된 연락처의 idx 설정

            // UI 갱신
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    contactItems.add(item);
                    adapter.notifyItemInserted(contactItems.size() - 1);
                }
            });
        }
    }

    class UpdateContact extends Thread {
        private Context context;
        private ContactEntity contact; // 업데이트할 연락처 객체

        public UpdateContact(Context context, ContactEntity contact) {
            this.context = context;
            this.contact = contact;
        }

        @Override
        public void run() {
            ContactDatabase.getInstance(context).getContactDao().update(contact);
        }
    }

    class GetContact extends Thread {
        private Context context;

        public GetContact(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            List<ContactEntity> entities = ContactDatabase.getInstance(context).getContactDao().getAllContact();
            if (entities.size() == 0) {
                // 최초 실행 시 JSON 파일에서 데이터베이스에 데이터 추가
                parserJsonAndInsert(context);
            } else {
                // 이미 데이터베이스에 데이터가 있으면 그 데이터로 UI 갱신
                for (ContactEntity entity : entities) {
                    ContactItem item = new ContactItem(entity.getIdx(), entity.getName(), entity.getDepartment(), entity.getPhone(), entity.getEmail());
                    contactItems.add(item);
                }
                // UI 갱신
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    class DeleteContact extends Thread {
        private Context context;
        private int idx; // 삭제할 연락처의 인덱스

        public DeleteContact(Context context, int idx) {
            this.context = context;
            this.idx = idx;
        }

        @Override
        public void run() {
            ContactDatabase.getInstance(context).getContactDao().delete(idx);

            // UI 갱신
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < contactItems.size(); i++) {
                        if (contactItems.get(i).getIdx() == idx) {
                            contactItems.remove(i);
                            adapter.notifyItemRemoved(i);
                            break;
                        }
                    }
                }
            });
        }
    }

    private void parserJsonAndInsert(Context context) {
        InputStream inputStream = getResources().openRawResource(R.raw.contactlist);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        StringBuffer stringBuffer = new StringBuffer();
        String line;

        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            JSONArray jsonArray = new JSONArray(jsonObject.getString("contact_list"));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                String name = jsonObject1.getString("name");
                String department = jsonObject1.getString("department");
                String phoneNumber = jsonObject1.getString("phoneNumber");
                String email = jsonObject1.getString("email");

                ContactItem item = new ContactItem(name, department, phoneNumber, email);
                new AddContact(context, item).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}