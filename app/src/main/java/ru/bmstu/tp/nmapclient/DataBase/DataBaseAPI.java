package ru.bmstu.tp.nmapclient.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DataBaseAPI extends SQLiteOpenHelper {

    public DataBaseAPI(Context context) {
        super(context, DataBaseSchemas.DATABASE_NAME, null, DataBaseSchemas.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseSchemas.SQL_CREATE_TABLE_SCANS);
        db.execSQL(DataBaseSchemas.SQL_CREATE_TABLE_TMP_SCAN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DataBaseSchemas.SQL_DELETE_SCANS);
        db.execSQL(DataBaseSchemas.SQL_DELETE_TMP_SCAN);
        onCreate(db);
    }
}
