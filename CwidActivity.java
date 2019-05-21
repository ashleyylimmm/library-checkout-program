package com.deanza.mstrclib;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CwidActivity extends AppCompatActivity implements AsyncUpload.Callback {
    String bookID;
    String cwid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cwid);

        GetParams();
        SetUp();
    }

    private void GetParams() {
        Intent intent = getIntent();
        bookID = intent.getStringExtra("pBookID");
    }

    private void SetUp() {
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Checking out " + bookID);
    }

    public void ButEnterClick(View view) {
        EditText et = findViewById(R.id.etCWID);
        cwid = et.getText().toString();

        CheckOut();
    }

    private void CheckOut() {
        String timeOut = Utils.GetDt13();
        String timeIn = "";
        String term = Utils.GetTerm(Utils.GetDt6());

        AsyncUpload asyncUpload = new AsyncUpload(this);
        String cmdstr =
                String.format("REPLACE INTO libcheckout(ItemID, CWID, TimeOut, TimeIn, Term) " +
                                "VALUES('%s', '%s', '%s', '%s', '%s')",
                                bookID, cwid, timeOut, timeIn, term);
        asyncUpload.Doit(0, cmdstr);
    }

    void ShowMessage(String msg) {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setMessage(msg);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (DialogInterface.OnClickListener) null);
        alert.show();
    }

    public void ButCancelClick(View view) {
        finish();
    }

    @Override
    public void UploadDone(int which, String msg) {
        finish();
    }
}
