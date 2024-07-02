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

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends Fragment {
    RecyclerView recyclerView; // 연락처 목록 뷰
    LinearLayoutManager layoutManager;
    ContactAdapter adapter; // 연락처 관리 어댑터
    ArrayList<ContactItem> contactItems = new ArrayList<>(); // 기본 목록
    ArrayList<ContactItem> filteredList = new ArrayList<>(); // 검색했을 때 목록
    FloatingActionButton fab;
    EditText editSearch; // 검색바
    private ActivityResultLauncher<Intent> addContactLauncher; // 입력창에서 정보를 받아오기 위한 런처

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ContactAdapter(getActivity().getApplicationContext(), contactItems);
        recyclerView.setAdapter(adapter); // 기본 뷰 세팅

        editSearch = (EditText) rootView.findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 비어 있음
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ///
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editSearch.getText().toString();
                searchFilter(searchText);
            }
        });

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
            @Override
            public void onItemLongClick(ContactAdapter.ViewHolder holder, View view, int position) {
                ContactItem item = adapter.getItem(position);
                showDeleteConfirmationDialog(item);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initContactData();
    }

    @Override
    public void onResume() {
        super.onResume();
        // UI 갱신을 위해 데이터를 다시 로드
        refreshContactList();
    }

    private void refreshContactList() {
        // 기존의 리스트를 비우고 데이터베이스에서 다시 로드
        contactItems.clear();
        new GetContact(getActivity()).start();
    }


    public void searchFilter(String searchText) {
        filteredList.clear();

        for (ContactItem item : contactItems) {
            if (item.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList); // 어댑터에 필터링된 리스트 전달
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
                .setMessage(item.getName() + " 교수님을 삭제하시겠습니까?")
                .setPositiveButton("삭제", (dialogInterface, i) -> new DeleteContact(getActivity(), item.getIdx()).start())
                .setNegativeButton("취소", null)
                .show();
    }

    class AddContact extends Thread { // DB에 넣고 리사이클러 뷰 갱신
        String TAG = "AddContact";
        private Context context;
        ContactItem item;

        public AddContact(Context context, ContactItem item) {
            this.context = context;
            this.item = item;
        }

        @Override
        public void run() {
            Log.d(TAG, "실행 시작");
            ContactEntity contact = new ContactEntity(item.getName(), item.getDepartment(), item.getPhoneNumber(), item.getEmail());
            long newIdx = ContactDatabase.getInstance(context).getContactDao().insert(contact); // 새로 만든 entity contact를 넣기
            item.setIdx((int) newIdx); // 새로 추가된 연락처의 idx 설정
            Log.d(TAG, "DB에 데이터추가 완료");

            // UI 갱신
            getActivity().runOnUiThread(() -> {
                adapter.addItem(item); // 어댑터에 추가
                Log.d(TAG, "기본 목록에 데이터 추가 및 UI 갱신");
            });

            /* 기존의 경우 스레드에서 뷰 갱신
            new Runnable() {
                @Override
                public void run() {
                    contactItems.add(item);
                    Log.d(TAG, "기본 목록에 데이터 추가");
                    adapter.notifyItemInserted(contactItems.size() - 1);
                    Log.d(TAG, "notifyItemInserted");
                }
            });*/
        }
    }

    class GetContact extends Thread {
        String TAG = "GetContact";
        private Context context;

        public GetContact(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            Log.d(TAG, "기본 목록에 데이터 추가");

            // 데이터베이스의 현재 데이터 수를 확인
            int contactCount = ContactDatabase.getInstance(context).getContactDao().getContactCount();
            if (contactCount == 0) {
                // 데이터가 없을 때만 JSON 파싱 및 삽입
                parserJsonAndInsert(context);

                // JSON 데이터 삽입 후 새로 갱신된 데이터를 로드
                List<ContactEntity> updatedEntities = ContactDatabase.getInstance(context).getContactDao().getAllContact();
                for (ContactEntity entity : updatedEntities) {
                    ContactItem item = new ContactItem(entity.getIdx(), entity.getName(), entity.getDepartment(), entity.getPhone(), entity.getEmail());
                    contactItems.add(item);
                }
                getActivity().runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                });
            } else {
                // 데이터가 이미 있을 경우 기존 데이터 로드
                List<ContactEntity> entities = ContactDatabase.getInstance(context).getContactDao().getAllContact();
                for (ContactEntity entity : entities) {
                    ContactItem item = new ContactItem(entity.getIdx(), entity.getName(), entity.getDepartment(), entity.getPhone(), entity.getEmail());
                    contactItems.add(item);
                }
                getActivity().runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                });
            }
        }
    }

    class DeleteContact extends Thread {
        String TAG = "DeleteContact";
        private Context context;
        private int idx; // 삭제할 연락처의 인덱스

        public DeleteContact(Context context, int idx) {
            this.context = context;
            this.idx = idx;
        }

        @Override
        public void run() {
            Log.d(TAG, "실행 시작");
            ContactDatabase.getInstance(context).getContactDao().delete(idx);
            Log.d(TAG, "DB 삭제");

            // UI 갱신
            getActivity().runOnUiThread(() -> {
                for (int i = 0; i < contactItems.size(); i++) {
                    if (contactItems.get(i).getIdx() == idx) {
                        contactItems.remove(i);
                        adapter.removeItem(i); // 어댑터에 삭제 알림/어댑터가 관리
                        break;
                    }
                }
            });

            /*
            스레드에서 모든 걸 처리하던 기존 코드...
            new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "UI 갱신");
                    for (int i = 0; i < contactItems.size(); i++) {
                        if (contactItems.get(i).getIdx() == idx) {
                            Log.d(TAG, "삭제할 인덱스의 아이템 찾음");
                            contactItems.remove(i);
                            Log.d(TAG, "리사이클러 뷰에서 삭제");
                            adapter.notifyItemRemoved(i);
                            Log.d(TAG, "notifyItemRemoved");

                            break;
                        }
                    }
                }
            });*/
        }
    }

//    class UpdateContact extends Thread {
//        private Context context;
//        private ContactEntity contact; // 업데이트할 연락처 객체
//
//        public UpdateContact(Context context, ContactEntity contact) {
//            this.context = context;
//            this.contact = contact;
//        }
//
//        @Override
//        public void run() {
//            ContactDatabase.getInstance(context).getContactDao().update(contact);
//        }
//    }

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