package com.entrepreneurs.entrepreneurs.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.entrepreneurs.entrepreneurs.R;
import com.entrepreneurs.entrepreneurs.entities.Group;
import com.entrepreneurs.entrepreneurs.entities.Member;
import com.entrepreneurs.entrepreneurs.entities.PaymentInformation;
import com.entrepreneurs.entrepreneurs.repository.Session;
import com.entrepreneurs.entrepreneurs.util.Constants;
import com.entrepreneurs.entrepreneurs.util.ProgressDialogFactory;
import com.entrepreneurs.entrepreneurs.util.ToastFactory;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class ManualPayment extends AppCompatActivity {
    private Spinner memberSpinner;
    private String selectedMember;
    private String[] members = new String[]{
            "-- Select --",
            Constants.MEMBER_1,
            Constants.MEMBER_2,
            Constants.MEMBER_3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_payment);
        // set the title of the activity
        this.setTitle("Manual Payment");
        // populate the spinner with data
        Spinner spinner = findViewById(R.id.names_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.support_simple_spinner_dropdown_item, members);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        memberSpinner = spinner;
        memberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ManualPayment.this.selectedMember
                        = ManualPayment.this.members[adapterView.getSelectedItemPosition()];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ManualPayment.this.selectedMember
                        = ManualPayment.this.members[0];
            }
        });
    }

    public void addPayment(View view) {
        String member = this.selectedMember;
        String amount = ((EditText) this.findViewById(R.id.amount_editText)).getText().toString().trim();
        String date = ((EditText) this.findViewById(R.id.date_editText)).getText().toString().trim();
        String time = ((EditText) this.findViewById(R.id.time_editText)).getText().toString().trim();
        String transactionID = ((EditText) this.findViewById(R.id.transactionID_editText)).getText().toString().trim();

        ToastFactory toastFactory = new ToastFactory(this);

        if(member.startsWith("--") || amount.isEmpty() || date.isEmpty() || time.isEmpty() || transactionID.isEmpty()){
            toastFactory.getToast(ToastFactory.ToastType.ERROR, "All Fields are required!").show();
        }else{
            Pattern datePattern = Pattern.compile("[\\d]+/[\\d]+/[\\d]+");
            Pattern timePattern = Pattern.compile("[\\d]+:[\\d]+:[\\d]+");

            if(!datePattern.matcher(date).matches()){
                // wrong date format
                toastFactory.getToast(ToastFactory.ToastType.ERROR, "Wrong Date Format").show();
            }else if(!timePattern.matcher(time).matches()){
                toastFactory.getToast(ToastFactory.ToastType.ERROR, "Wrong Time Format").show();
            }else{

                String[] dateComponents = date.split("/");
                if(Integer.parseInt(dateComponents[0]) > 31 || Integer.parseInt(dateComponents[0]) < 1){
                    toastFactory.getToast(ToastFactory.ToastType.ERROR, "Invalid value for Day").show();
                }else if(Integer.parseInt(dateComponents[1]) < 1 || Integer.parseInt(dateComponents[1]) > 12){
                    toastFactory.getToast(ToastFactory.ToastType.ERROR, "Invalid value for Month").show();
                }else if(Integer.parseInt(dateComponents[2]) < new Date().getYear()){
                    toastFactory.getToast(ToastFactory.ToastType.ERROR, "Invalid value for Year").show();
                }else{
                    String[] timeComponents = time.split(":");

                    if(Integer.parseInt(timeComponents[0]) > 23 || Integer.parseInt(timeComponents[0]) < 0){
                        toastFactory.getToast(ToastFactory.ToastType.ERROR, "Invalid value for Hours").show();
                    }else if(Integer.parseInt(timeComponents[1]) < 0 || Integer.parseInt(timeComponents[1]) > 59){
                        toastFactory.getToast(ToastFactory.ToastType.ERROR, "Invalid value for Minutes").show();
                    }else if(Integer.parseInt(timeComponents[2]) < 0 || Integer.parseInt(timeComponents[2]) > 100){
                        toastFactory.getToast(ToastFactory.ToastType.ERROR, "Invalid value for Seconds").show();
                    }else{
                        // check if there is another such payment.
                        if(!this.paymentExists(transactionID)){
                            PaymentInformation paymentInformation = new PaymentInformation(transactionID, amount, date + "  " + time);
                            Group group = (Group) Session.getSession().getAttribute(Constants.GROUP_KEY);
                            group.getMemberByName(member).getPayments().add(paymentInformation);
                            this.updateFirebase(group);
                            this.clearFields();
                        }else{
                            toastFactory.getToast(ToastFactory.ToastType.ERROR, "A payment with this ID exists!").show();
                        }
                    }
                }
            }
        }
    }

    private void updateFirebase(Group group){
        ToastFactory toastFactory = new ToastFactory(this);
        ProgressDialog progressDialog = ProgressDialogFactory.getProgressDialog(this, "Upload Payment", "Saving Payment...please wait");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog.show();
        databaseReference.setValue(group, (databaseError, databaseReference1) -> {
            if(null == databaseError){
                // there was no error during the update
                progressDialog.dismiss();
                toastFactory.getToast(ToastFactory.ToastType.INFO, ":-) Save Successful");
            }else{
                // there was an error during the update
                progressDialog.dismiss();
                toastFactory.getToast(ToastFactory.ToastType.ERROR, ":-( Save Failed "
                        + databaseError.getMessage()).show();
            }
        });
    }

    private void clearFields(){
        ((EditText) this.findViewById(R.id.amount_editText)).setText("");
        ((EditText) this.findViewById(R.id.date_editText)).setText("");
        ((EditText) this.findViewById(R.id.time_editText)).setText("");
        ((EditText) this.findViewById(R.id.transactionID_editText)).setText("");
        this.selectedMember = this.members[0];
    }

    private boolean paymentExists(String paymentID){
        Group group = (Group) Session.getSession().getAttribute(Constants.GROUP_KEY);
        for(Member member : group.getMembers()){
            for(PaymentInformation paymentInformation : member.getPayments()){
                if(paymentInformation.getTransactionID().equals(paymentID)){
                    return true;
                }
            }
        }
        return false;
    }
}
