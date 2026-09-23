package com.example.assignment1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Comparator;
import java.util.Objects;

public class Q8 extends Activity {
    Dialog createUserDialog;
    private DbHandler db;
    private UsersListAdapter users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q8);


        db = new DbHandler(this);
        users = new UsersListAdapter(this, db);
        db.getAllUsers().forEach((user) -> {
            users.add(new User(Objects.requireNonNull(user.get(DbHandler.KEY_NAME)), Objects.requireNonNull(user.get(DbHandler.KEY_LOC)), Objects.requireNonNull(user.get(DbHandler.KEY_DESG)), Long.parseLong(Objects.requireNonNull(user.get(DbHandler.KEY_ID)))));
        });
        createUserDialog = new Dialog(this);
        createUserDialog.setContentView(R.layout.activity_q8_create_dialog);

        Button createUserButton = findViewById(R.id.q8_create_user);
        createUserButton.setOnClickListener(v -> createUserDialog.show());

        ListView lv = findViewById(R.id.q8_list_view);
        lv.setAdapter(users);

    }


    public void handleSaveClick(View view) {
        TextView name = createUserDialog.findViewById(R.id.q8_insert_name);
        TextView location = createUserDialog.findViewById(R.id.q8_insert_location);
        TextView designation = createUserDialog.findViewById(R.id.q8_insert_designation);
        if (!DbHandler.valuesValid(name, location, designation)) return;

        String nameText = name.getText().toString();
        String locationText = location.getText().toString();
        String designationText = designation.getText().toString();

        long id = db.insert(nameText, locationText, designationText);
        users.add(new User(nameText, locationText, designationText, id));

        name.setText(null);
        location.setText(null);
        designation.setText(null);
        if (createUserDialog.isShowing()) createUserDialog.cancel();
    }
}

class UsersListAdapter extends ArrayAdapter<User> {

    private final Context c;
    private final DbHandler db;

    public UsersListAdapter(@NonNull Context context, DbHandler db) {
        super(context, R.layout.q8_list_item);
        this.c = context;
        this.db = db;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.q8_list_item, parent, false);
        }


        TextView id = convertView.findViewById(R.id.q8_save_id);
        TextView name = convertView.findViewById(R.id.q8_save_name);
        TextView loc = convertView.findViewById(R.id.q8_save_location);
        TextView desg = convertView.findViewById(R.id.q8_save_designation);
        Button edit = convertView.findViewById(R.id.q8_save_edit);
        Button delete = convertView.findViewById(R.id.q8_save_delete);
        assert user != null;
        id.setText(String.valueOf(user.id));
        name.setText(user.name);
        loc.setText(user.location);
        desg.setText(user.designation);

        edit.setOnClickListener(view -> {
            // verify data
            if (edit.getText() == c.getText(R.string.edit)) {
                name.setEnabled(true);
                loc.setEnabled(true);
                desg.setEnabled(true);
                edit.setText(c.getText(R.string.save));
                return;
            }

            // save updates to database
            if (!DbHandler.valuesValid(name, loc, desg)) return;
            user.name = name.getText().toString();
            user.location = loc.getText().toString();
            user.designation = desg.getText().toString();
            db.update(user.id, user.name, user.location, user.designation);

            // update the UI
            notifyDataSetChanged();
            name.setEnabled(false);
            loc.setEnabled(false);
            desg.setEnabled(false);
            edit.setText(c.getText(R.string.edit));
        });

        delete.setOnClickListener(v -> {
            db.delete(user.id);
            remove(user);
        });
        return convertView;
    }

    @Override
    public void sort(@NonNull Comparator<? super User> comparator) {
        super.sort((user, t1) -> {
            if (user == null || t1 == null) return 0;
            if (user.id == t1.id) return 0;
            return user.id >= t1.id ? 1 : -1;
        });
    }
}

class User {
    final long id;
    String name;
    String location;
    String designation;

    User(@NonNull String name, @NonNull String location, @NonNull String designation, long id) {
        this.name = name;
        this.location = location;
        this.designation = designation;
        this.id = id;
    }
}