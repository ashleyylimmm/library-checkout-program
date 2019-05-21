package com.deanza.mstrclib;



import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AsyncUpload {
    public interface Callback {
        void UploadDone(int which, String msg);
    }

    Callback caller = null;
    int which = 0;

    public AsyncUpload(Callback caller) {
        this.caller = caller;
    }

    public void Doit(int which, String query) {
        this.which = which;
        UploadTask task = new UploadTask();
        task.execute(query);
    }

    private class UploadTask extends AsyncTask<String, String, String> {
        String msg = "success";

        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmt = null;
            String query = strings[0];

            try {
                Class.forName(Cons.JDBC_DRIVER);
                conn = DriverManager.getConnection(Cons.FULL_URL, Cons.DATABASE_USER, Cons.DATABASE_PASSWD);
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                rs.close();
                stmt.close();
                conn.close();
            } catch (ClassNotFoundException e) {
                msg = "Driver Not Found";
                e.printStackTrace();
            } catch (SQLException e) {
                msg = e.toString();
                e.printStackTrace();
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException e) {
                    msg = e.toString();
                    e.printStackTrace();
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    msg = e.toString();
                    e.printStackTrace();
                }
            }

            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            caller.UploadDone(which, msg);
        }
    }
}