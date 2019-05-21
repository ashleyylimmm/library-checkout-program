package com.deanza.mstrclib;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements AsyncDownload.Callback, AsyncUpload.Callback {
    final int Do_Download_Inventory = 0;
    final int Do_Download_checkout = 1;

    String bookID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ButEnterClick(View view) {
        EditText etBookID = findViewById(R.id.etCWID);
        bookID = etBookID.getText().toString();

        DownloadInventory(bookID);
    }

    private void DownloadInventory(String bookID) {
        String cmdstr = String.format("SELECT * FROM libinventory WHERE ItemID = '%s'", bookID);
        AsyncDownload asyncDownload = new AsyncDownload(this);
        asyncDownload.Doit(0, cmdstr);
    }

    private void GotInvntory(String[][] data) {
        if(data.length == 0) {   // got some data,
            ShowMessage("Invalid Book ID " + bookID);
            return;
        }

        Download_checkoutRecord(bookID);
    }

    void Download_checkoutRecord(String bookID) {
        String cmdstr = String.format("SELECT * FROM libcheckout WHERE ItemID = '%s' AND TimeIn = ''", bookID);
        AsyncDownload asyncDownload = new AsyncDownload(this);
        asyncDownload.Doit(1, cmdstr);
    }

    private void GotCheckoutRecord(String[][] data) {
        if(data.length > 0) {
            CheckIn(data);
        }
        else {
            OpenCwidActivity(bookID);
        }
    }

    void CheckIn(String[][] data) {
        String bookID = data[0][0];
        String cwid = data[0][1];
        String timeOut = data[0][2];
        String timeIn = Utils.GetDt13();
        String term = Utils.GetTerm(Utils.GetDt6());

        AsyncUpload asyncUpload = new AsyncUpload(this);
        String cmdstr =
                String.format("REPLACE INTO libcheckout(ItemID, CWID, TimeOut, TimeIn, Term) " +
                                "VALUES('%s', '%s', '%s', '%s', '%s')",
                        bookID, cwid, timeOut, timeIn, term);
        asyncUpload.Doit(0, cmdstr);
    }

    private void OpenCwidActivity(String bookID) {
        Intent intent = new Intent(this, CwidActivity.class);
        intent.putExtra("pBookID", bookID);
        startActivity(intent);
    }

    void ShowMessage(String msg) {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setMessage(msg);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (DialogInterface.OnClickListener) null);
        alert.show();
    }

    @Override
    public void DownloadDone(int which, String msg, String[][] data) {
        switch(which) {
            case 0:
                GotInvntory(data);
                break;

            case 1:
                GotCheckoutRecord(data);
                break;
        }
    }

    @Override
    public void UploadDone(int which, String msg) {
        ShowMessage(bookID + " checked in");
    }
}
