package com.deanza.mstrclib;

public class Cons {
    static final String DATABASE_URL = "192.232.218.105:3306";
    static final String DATABASE_NAME = "compling_pantrywics";
    static final String FULL_URL = "jdbc:mysql://" + DATABASE_URL + "/" + DATABASE_NAME;
    static final String DATABASE_USER = "compling_wics";
    static final String DATABASE_PASSWD = "womancoder";

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    static final int Do_upload_checkout = 1;

    static final int Do_download_checkout = 101;
    static final int Do_download_inventory = 102;
}
