package com.example.meallogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ComplaintAdapter extends ArrayAdapter<Complaint> {
    //    Context context;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbref = db.getReference();
//

    public ComplaintAdapter(Context context, ArrayList<Complaint> complaintArrayList) {
        super(context, R.layout.recycler_complaint_item2, R.id.chefName, complaintArrayList);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Complaint complaint = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recycler_complaint_item2, parent, false);
        }

        //set name of cook that received the complaint
        TextView name = convertView.findViewById(R.id.chefName);
        name.setText(complaint.getCook().getUsername());

        //display the description of the complaint
        TextView description = convertView.findViewById(R.id.complaintDescription);
        description.setText(complaint.getDescription());

        MaterialButton dismiss = convertView.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(v -> {
            //Dismisses the complaints without punishing cooks
            db.getReference("Complaints").orderByChild("actioned").equalTo(false).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        int i = 0;
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            if (i != position) {
                                i++;//find specific complaint in the listview
                            } else {
                                Complaint c = ds.getValue(Complaint.class);
                                c.setActioned(true);//set it to actioned
                                String id = ds.getKey();//find its specific key
                                db.getReference("Complaints").child(id).setValue(c); //overwrite old object
                                remove(complaint);//remove from listview
                                notifyDataSetChanged();//update listview
                                break;
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
        EditText date = convertView.findViewById(R.id.date);
        MaterialButton suspend = convertView.findViewById(R.id.suspend);
        suspend.setOnClickListener(v -> {
            if (isValid(date.getText().toString())) {
                //repeat process in dismiss except set suspension date and modify cook reference path
                //to account for the cook being suspended.
                db.getReference("Complaints").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            int i = 0;
                            for(DataSnapshot ds: snapshot.getChildren()){
                                if(i!=position){
                                    i++;
                                }else{
                                    Complaint c = ds.getValue(Complaint.class);
                                    c.setActioned(true);
                                    c.setSuspensionDate(date.getText().toString());
                                    c.getCook().setStatus(true);
                                    c.getCook().setSuspensionDate(date.getText().toString());
                                    String id = ds.getKey();
                                    db.getReference("Complaints").child(id).setValue(c);

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                dbref = db.getReference("Cooks");
                dbref.orderByChild("username").equalTo(complaint.getCook().getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Cook newcook;
                            for(DataSnapshot snap: dataSnapshot.getChildren()){
                                Cook oldcook = snap.getValue(Cook.class);
                                newcook = oldcook;
                                newcook.setStatus(true);
                                newcook.setSuspensionDate(date.getText().toString());
                                String addy = snap.getKey();
                                dbref.child(addy).setValue(newcook);
                                remove(complaint);
                                notifyDataSetChanged();
                                //updating cook in the db+updating listview
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                date.setText("");
                Toast.makeText(getContext(), "The date entered is invalid", Toast.LENGTH_LONG).show();
            }
        });
        MaterialButton permaBan = convertView.findViewById(R.id.permaBan);
        permaBan.setOnClickListener(v -> {
            db.getReference("Complaints").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        int i = 0;
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if(i!=position){
                                i++;
                            }else{
                                Complaint c = ds.getValue(Complaint.class);
                                c.setActioned(true);
                                c.setSuspensionDate("permanent");
                                c.getCook().setStatus(true);
                                c.getCook().setSuspensionDate("permanent");
                                String id = ds.getKey();
                                db.getReference("Complaints").child(id).setValue(c);

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            dbref = db.getReference("Cooks");
            dbref.orderByChild("username").equalTo(complaint.getCook().getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Cook newcook;
                        for(DataSnapshot snap: dataSnapshot.getChildren()){
                            Cook oldcook = snap.getValue(Cook.class);
                            newcook = oldcook;
                            newcook.setStatus(true);
                            newcook.setSuspensionDate("permanent");
                            String addy = snap.getKey();
                            dbref.child(addy).setValue(newcook);
                            remove(complaint);
                            notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
        return super.getView(position, convertView, parent);
    }

    public static boolean isValid(String date) {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setLenient(false);
        try {
            df.parse(date.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}
