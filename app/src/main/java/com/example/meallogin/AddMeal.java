package com.example.meallogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddMeal extends AppCompatActivity {
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbref= db.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        Cook cook = (Cook) getIntent().getSerializableExtra("Cook");

        MaterialButton add = (MaterialButton) findViewById(R.id.addButton);
        add.setOnClickListener(v -> {
            String mealName = ((EditText) findViewById(R.id.toAddTextInput)).getText().toString();
            if (!(cook.getMenu().getOffered().contains(cook.getMenu().findMealByNameInMeallist(mealName)))) {
                cook.getMenu().addtoOffered(cook.getMenu().findMealByNameInMeallist(mealName));
                dbref.child("Cooks").orderByChild("username").equalTo(cook.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for(DataSnapshot d:snapshot.getChildren()){
                                Cook c = d.getValue(Cook.class);
                                if(c.getUsername().equals(cook.getUsername())){
                                    //Find the cook in the db to update their menu
                                    String id = d.getKey(); //Cook-unique id
                                    dbref.child("Cooks").child(id).child("menu").setValue(cook.getMenu());
                                    Toast.makeText(getApplicationContext(), "The meal is now offered!", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"here",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "This meal is already offered", Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(this, EditMenu.class);
            intent.putExtra("Cook",cook);
            startActivity(intent);
        });
    }


}