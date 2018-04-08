package com.entrepreneurs.entrepreneurs.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.entrepreneurs.entrepreneurs.R;
import com.entrepreneurs.entrepreneurs.entities.Group;
import com.entrepreneurs.entrepreneurs.entities.Member;
import com.entrepreneurs.entrepreneurs.repository.Session;
import com.entrepreneurs.entrepreneurs.util.Constants;
import com.entrepreneurs.entrepreneurs.util.ProgressDialogFactory;
import com.entrepreneurs.entrepreneurs.util.ToastFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Entrepreneurs extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private ValueEventListener valueEventListener;
    private int attempts = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entrepreneurs);

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        String storedPIN = sharedPreferences.getString(Constants.CURRENT_MEMBER_PIN, "");
        EditText editText = this.findViewById(R.id.activateEditText);
        editText.setText(storedPIN);
        CheckBox checkBoxRememberPIN = findViewById(R.id.rememberPIN);

        if(storedPIN.trim().isEmpty()){
            checkBoxRememberPIN.setChecked(false);
        }else{
            checkBoxRememberPIN.setChecked(true);
        }
        this.valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get the group instance
                Group group = dataSnapshot.getValue(Group.class);
                // store the group reference in the session
                Session.getSession().addAttribute(Constants.GROUP_KEY, group);
                // grab the PIN entered
                String PIN = ((EditText) Entrepreneurs.this.findViewById(R.id.activateEditText))
                        .getText().toString().trim();
                // dismiss the progress dialog
                Entrepreneurs.this.progressDialog.dismiss();
                // perform authentication
                Entrepreneurs.this.signIn(PIN, group);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                ToastFactory toastFactory = new ToastFactory(Entrepreneurs.this);
                toastFactory.getToast(ToastFactory.ToastType.ERROR, "Error : " + databaseError.getMessage());
            }
        };
    }



    // activate the user
    public void activateUser(View view) {

        // create a toast factory, should be reused.
        ToastFactory toastFactory = new ToastFactory(this);

        // grab the code from edit text
        String activationCode = ((EditText) this.findViewById(R.id.activateEditText))
                .getText().toString().trim();

        if (activationCode.isEmpty()) {
            // code cannot be empty!
            Toast toast = toastFactory.getToast(ToastFactory.ToastType.ERROR,
                    this.getResources().getString(R.string.code_is_empty));
            toast.show();
        } else {
            // initialize firebase database ref.
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            try{
                firebaseDatabase.setPersistenceEnabled(true);
            }catch(Exception e){
                Log.d(this.getLocalClassName(), e.getLocalizedMessage());
            }
            this.databaseReference = firebaseDatabase.getReference();
            this.databaseReference.addValueEventListener(this.valueEventListener);
            // show progress dialog
            this.progressDialog = ProgressDialogFactory
                    .getProgressDialog(this, "Loading Data", "Please Wait...");
            progressDialog.show();
        }
    }



    // perform actual sign in
    private void signIn(String PIN, Group group){
        ToastFactory toastFactory = new ToastFactory(this);
        Member member = this.signInHelper(PIN, group);
        if (null == member) {
            // invalid PIN!

            // decrement the counter
            this.attempts--;
            // update the remaining attempts
            ((TextView)findViewById(R.id.attemptsRemainingTextView))
                    .setText(String.valueOf(this.attempts));
            // show that the user entered an invalid PIN
            Toast toast = toastFactory.getToast(ToastFactory.ToastType.ERROR,
                    this.getResources().getString(R.string.invalid_code));
            toast.show();

            // disable sign in button if the remaining attempts is 0
            if(this.attempts < 1){
                Button button = findViewById(R.id.activateButton);
                button.setEnabled(false);
                button.setText("Restart Application");
            }

        } else {
            // code is okay
            Toast toast = toastFactory.getToast(ToastFactory.ToastType.INFO,
                    this.getResources().getString(R.string.welcome));
            toast.show();

            // put the current member in session
            Session.getSession().addAttribute(Constants.CURRENT_MEMBER_KEY, member);


            // store the current member in shared preferences
            SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            String memberPIN = "";
            if(((CheckBox)this.findViewById(R.id.rememberPIN)).isChecked()){
                memberPIN = member.getPin();
            }
            sharedPreferencesEditor.putString(Constants.CURRENT_MEMBER_PIN, memberPIN);
            sharedPreferencesEditor.apply();

            // move to the dashboard
            Intent intent = new Intent(this, Dashboard.class);
            this.startActivity(intent);
        }
    }

    // return a member instance if there is a user with such a PIN, else, return null
    private Member signInHelper(String PIN, Group group){
        Member member = null;
        List<Member> memberList = group.getMembers();
        for (Member item : memberList) {
            if(item.getPin().equals(PIN.trim())){
                member = item;
                break;
            }
        }
        return member;
    }

    @Override
    public void onBackPressed(){
        // find a way to quit the app
        this.finishAffinity();
    }
}
