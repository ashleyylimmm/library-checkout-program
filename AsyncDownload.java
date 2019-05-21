package com.deanza.mstrclib;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class AsyncDownload {
    public interface Callback {
        void DownloadDone(int which, String msg, String[][] data);
    }

    Callback caller = null;
    int which = 0;

    public AsyncDownload(Callback caller ) {
        this.caller = caller;
    }

    public void Doit(int which, String query) {
        this.which = which;
        DownloadTask task = new DownloadTask();
        task.execute(query);
    }

    private class DownloadTask extends AsyncTask<String, String, String> {
        String msg = "success";
        String query;
        String[][] data;
        int columnCount;
        int rowCount;

        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement stmt = null;
            query = strings[0];

            try {
                Class.forName(Cons.JDBC_DRIVER);
                conn = DriverManager.getConnection(Cons.FULL_URL, Cons.DATABASE_USER, Cons.DATABASE_PASSWD);
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                ResultSetMetaData metaData = rs.getMetaData();
                columnCount = metaData.getColumnCount();
                rowCount = rs.last() ? rs.getRow() : 0;
                rs.beforeFirst();

                data = new String[rowCount][columnCount];
                while(rs.next()) {
                    //for(int i = 0; i < rowCount; i++) {
                    int row = rs.getRow() - 1;
                    for(int j = 0; j < columnCount; j++) {
                        data[row][j] = rs.getString(j+1);
                    }
                    //}
                }

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
            caller.DownloadDone(which, msg, data);
        }
    }
}
