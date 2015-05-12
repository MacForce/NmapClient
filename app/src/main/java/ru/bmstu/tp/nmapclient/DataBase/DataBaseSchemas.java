package ru.bmstu.tp.nmapclient.DataBase;

import android.provider.BaseColumns;

public final class DataBaseSchemas {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NmapClient.db";

    public DataBaseSchemas() {}

    public static final String SQL_CREATE_TABLE_SCANS =
            "CREATE TABLE " + Scans.TABLE_NAME + " (" +
                    Scans._ID + " INTEGER PRIMARY KEY)";

    public static final String SQL_DELETE_SCANS =
            "DROP TABLE IF EXISTS " + Scans.TABLE_NAME;

    public static abstract class Scans implements BaseColumns {
        public static final String TABLE_NAME = "scans";
    }

    public static final String SQL_CREATE_TABLE_TMP_SCAN =
            "CREATE TABLE " + TmpScan.TABLE_NAME + " (" +
                    TmpScan._ID + " INTEGER PRIMARY KEY)";

    public static final String SQL_DELETE_TMP_SCAN =
            "DROP TABLE IF EXISTS " + TmpScan.TABLE_NAME;

    public static abstract class TmpScan implements BaseColumns {
        public static final String TABLE_NAME = "tmp_scan";
    }
}
