package com.mohamedashraf.soundguardpro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Set<String> contactSet;
    private ContactsAdapter adapter;
    private TextInputEditText etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        sharedPreferences = getSharedPreferences("SoundGuardPrefs", MODE_PRIVATE);
        contactSet = new HashSet<>(sharedPreferences.getStringSet("white_list", new HashSet<String>()));

        setupUI();
    }

    private void setupUI() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar_contacts);
        toolbar.setNavigationOnClickListener(v -> finish());

        etPhone = findViewById(R.id.et_phone);
        TextInputLayout inputLayout = findViewById(R.id.input_layout_phone);
        inputLayout.setEndIconOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            if (!phone.isEmpty()) {
                addContact(phone);
                etPhone.setText("");
            }
        });

        RecyclerView rv = findViewById(R.id.rv_contacts);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactsAdapter(new ArrayList<>(contactSet));
        rv.setAdapter(adapter);
    }

    private void addContact(String phone) {
        if (contactSet.add(phone)) {
            sharedPreferences.edit().putStringSet("white_list", contactSet).apply();
            adapter.updateList(new ArrayList<>(contactSet));
            Toast.makeText(this, "Added: " + phone, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Already exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeContact(String phone) {
        if (contactSet.remove(phone)) {
            sharedPreferences.edit().putStringSet("white_list", contactSet).apply();
            adapter.updateList(new ArrayList<>(contactSet));
            Toast.makeText(this, "Removed: " + phone, Toast.LENGTH_SHORT).show();
        }
    }

    private class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
        private List<String> list;

        ContactsAdapter(List<String> list) {
            this.list = list;
        }

        void updateList(List<String> newList) {
            this.list = newList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String phone = list.get(position);
            holder.tvPhone.setText(phone);
            holder.btnDelete.setOnClickListener(v -> removeContact(phone));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvPhone;
            ImageButton btnDelete;

            ViewHolder(View itemView) {
                super(itemView);
                tvPhone = itemView.findViewById(R.id.tv_phone_number);
                btnDelete = itemView.findViewById(R.id.btn_delete_contact);
            }
        }
    }
}
