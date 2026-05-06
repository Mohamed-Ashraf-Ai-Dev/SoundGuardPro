package com.mohamedashraf.soundguardpro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

    private final ActivityResultLauncher<Intent> contactPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri contactUri = result.getData().getData();
                    String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                    try (Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null)) {
                        if (cursor != null && cursor.moveToFirst()) {
                            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            String number = cursor.getString(numberIndex);
                            addContact(number);
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Error picking contact", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

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

        findViewById(R.id.btn_pick_contact).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            contactPickerLauncher.launch(intent);
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
