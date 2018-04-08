package com.entrepreneurs.entrepreneurs.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

public class UpdatePin extends AppCompatActivity {
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pin);
    }

    public void updatePIN(View view) {
        String oldPIN = ((TextView)findViewById(R.id.old_pin)).getText().toString();
        String newPIN = ((TextView)findViewById(R.id.new_pin)).getText().toString();
        String confirmNewPIN = ((TextView)findViewById(R.id.confirm_new_pin)).getText().toString();
        ToastFactory toastFactory = new ToastFactory(this);
        Member member = (Member) Session.getSession().getAttribute(Constants.CURRENT_MEMBER_KEY);

        if(oldPIN.trim().isEmpty() || newPIN.trim().isEmpty() || confirmNewPIN.trim().isEmpty()){
            toastFactory.getToast(ToastFactory.ToastType.ERROR, "All Fields Are Required").show();
        }else if(!confirmNewPIN.equals(newPIN)){
            toastFactory.getToast(ToastFactory.ToastType.ERROR, "PINs must match!").show();
        }else if(!member.getPin().equals(oldPIN)){
            toastFactory.getToast(ToastFactory.ToastType.ERROR, "Incorrect Password Supplied").show();
        }else{
            // is OK, time to update the PIN.
           // member.setPin(newPIN);
            //toastFactory.getToast(ToastFactory.ToastType.INFO, "Password Successfully Changed.").show();

            // get which person is making the changes
            Group group = (Group) Session.getSession().getAttribute(Constants.GROUP_KEY);
            int memberPos = group.getMembers().indexOf(member);
           /* switch (member.getName()){
                case Constants.MEMBER_1:
                    memberPos = 1;
                    break;
                case Constants.MEMBER_2:
                    memberPos = 2;
                    break;
            }*/

            // update firebase
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference().child("members").child(String.valueOf(memberPos)).child("pin");
            databaseReference.setValue(newPIN, new DatabaseReference.CompletionListener() {
                private ProgressDialog progressDialog = ProgressDialogFactory.getProgressDialog(UpdatePin.this, "Updating PIN","Please Wait");
                {
                    progressDialog.show();
                }
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Log.d("Update Pin", databaseReference.getKey());
                    progressDialog.dismiss();
                }
            });
        }
    }
}
