package cn.krisez.imchat.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;

public class MessageDbHelper extends SupportSQLiteOpenHelper.Callback {
    private static final int VERSION = 1;
    public static final String CREATE_MESSAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + DbConstant.MSG_TABLE
            + "(_id INTEGER,fromId TEXT,toId TEXT,type TEXT,content TEXT,time TEXT,fileUrl TEXT,address TEXT,read TEXT,name TEXT,avatar TEXT);";

    public MessageDbHelper() {
        super(VERSION);
    }

    @Override
    public void onCreate(SupportSQLiteDatabase db) {
        db.execSQL(CREATE_MESSAGE_TABLE);
    }

    @Override
    public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
