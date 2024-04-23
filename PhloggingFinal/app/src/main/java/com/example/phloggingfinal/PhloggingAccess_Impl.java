package com.example.phloggingfinal;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PhloggingAccess_Impl implements PhloggingAccess {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter<PhloggingEntity> __deletionAdapterOfPhloggingEntity;
    private final EntityInsertionAdapter<PhloggingEntity> __insertionAdapterOfPhloggingEntity;
    private final EntityDeletionOrUpdateAdapter<PhloggingEntity> __updateAdapterOfPhloggingEntity;
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public PhloggingAccess_Impl(RoomDatabase __db2) {
        this.__db = __db2;
        this.__insertionAdapterOfPhloggingEntity = new EntityInsertionAdapter<PhloggingEntity>(__db2) {
            public String createQuery() {
                return "INSERT OR ABORT INTO `Phlogs` (`id`,`phlogsDB_title`,`phlogsDB_text`,`phlogsDB_uri`,`phlogsDB_timestamp`,`phlogsDB_latitude`,`phlogsDB_longitude`,`phlogsDB_text_location`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement stmt, PhloggingEntity value) {
                stmt.bindLong(1, (long) value.getId());
                if (value.getTitle() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getTitle());
                }
                if (value.getText() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getText());
                }
                if (value.getUri() == null) {
                    stmt.bindNull(4);
                } else {
                    stmt.bindString(4, value.getUri());
                }
                if (value.getTimestamp() == null) {
                    stmt.bindNull(5);
                } else {
                    stmt.bindString(5, value.getTimestamp());
                }
                stmt.bindDouble(6, (double) value.getLatitude());
                stmt.bindDouble(7, (double) value.getLongitude());
                if (value.getTextLocation() == null) {
                    stmt.bindNull(8);
                } else {
                    stmt.bindString(8, value.getTextLocation());
                }
            }
        };
        this.__deletionAdapterOfPhloggingEntity = new EntityDeletionOrUpdateAdapter<PhloggingEntity>(__db2) {
            public String createQuery() {
                return "DELETE FROM `Phlogs` WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement stmt, PhloggingEntity value) {
                stmt.bindLong(1, (long) value.getId());
            }
        };
        this.__updateAdapterOfPhloggingEntity = new EntityDeletionOrUpdateAdapter<PhloggingEntity>(__db2) {
            public String createQuery() {
                return "UPDATE OR ABORT `Phlogs` SET `id` = ?,`phlogsDB_title` = ?,`phlogsDB_text` = ?,`phlogsDB_uri` = ?,`phlogsDB_timestamp` = ?,`phlogsDB_latitude` = ?,`phlogsDB_longitude` = ?,`phlogsDB_text_location` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement stmt, PhloggingEntity value) {
                stmt.bindLong(1, (long) value.getId());
                if (value.getTitle() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getTitle());
                }
                if (value.getText() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getText());
                }
                if (value.getUri() == null) {
                    stmt.bindNull(4);
                } else {
                    stmt.bindString(4, value.getUri());
                }
                if (value.getTimestamp() == null) {
                    stmt.bindNull(5);
                } else {
                    stmt.bindString(5, value.getTimestamp());
                }
                stmt.bindDouble(6, (double) value.getLatitude());
                stmt.bindDouble(7, (double) value.getLongitude());
                if (value.getTextLocation() == null) {
                    stmt.bindNull(8);
                } else {
                    stmt.bindString(8, value.getTextLocation());
                }
                stmt.bindLong(9, (long) value.getId());
            }
        };
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public void addPhlog(PhloggingEntity newPhlog) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfPhloggingEntity.insert(newPhlog);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public void deletePhlog(PhloggingEntity oldPlog) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfPhloggingEntity.handle(oldPlog);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public void updatePhlog(PhloggingEntity newPhlog) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfPhloggingEntity.handle(newPhlog);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public List<PhloggingEntity> fetchAllPhlogs() {
        String _tmpTitle;
        int _cursorIndexOfId;
        String _tmpText;
        String _tmpText2;
        String _tmpUri;
        String _tmpTextLocation;
        RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM Phlogs ORDER BY id ASC", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor _cursor = DBUtil.query(this.__db, _statement, false, (CancellationSignal) null);
        try {
            int _cursorIndexOfId2 = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "phlogsDB_title");
            int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "phlogsDB_text");
            int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "phlogsDB_uri");
            int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "phlogsDB_timestamp");
            int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "phlogsDB_latitude");
            int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "phlogsDB_longitude");
            int _cursorIndexOfTextLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "phlogsDB_text_location");
            List<PhloggingEntity> _result = new ArrayList<>(_cursor.getCount());
            while (_cursor.moveToNext()) {
                PhloggingEntity _item = new PhloggingEntity();
                _item.setId(_cursor.getInt(_cursorIndexOfId2));
                if (_cursor.isNull(_cursorIndexOfTitle)) {
                    _tmpTitle = null;
                } else {
                    _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
                }
                _item.setTitle(_tmpTitle);
                if (_cursor.isNull(_cursorIndexOfText)) {
                    _cursorIndexOfId = _cursorIndexOfId2;
                    _tmpText = null;
                } else {
                    _cursorIndexOfId = _cursorIndexOfId2;
                    _tmpText = _cursor.getString(_cursorIndexOfText);
                }
                _item.setText(_tmpText);
                if (_cursor.isNull(_cursorIndexOfUri)) {
                    String str = _tmpText;
                    _tmpText2 = null;
                } else {
                    String str2 = _tmpText;
                    _tmpText2 = _cursor.getString(_cursorIndexOfUri);
                }
                _item.setUri(_tmpText2);
                if (_cursor.isNull(_cursorIndexOfTimestamp)) {
                    String str3 = _tmpText2;
                    _tmpUri = null;
                } else {
                    String str4 = _tmpText2;
                    _tmpUri = _cursor.getString(_cursorIndexOfTimestamp);
                }
                _item.setTimestamp(_tmpUri);
                String str5 = _tmpUri;
                float _tmpLatitude = _cursor.getFloat(_cursorIndexOfLatitude);
                _item.setLatitude(_tmpLatitude);
                float f = _tmpLatitude;
                float _tmpLongitude = _cursor.getFloat(_cursorIndexOfLongitude);
                _item.setLongitude(_tmpLongitude);
                if (_cursor.isNull(_cursorIndexOfTextLocation)) {
                    float f2 = _tmpLongitude;
                    _tmpTextLocation = null;
                } else {
                    float f3 = _tmpLongitude;
                    _tmpTextLocation = _cursor.getString(_cursorIndexOfTextLocation);
                }
                _item.setTextLocation(_tmpTextLocation);
                _result.add(_item);
                _cursorIndexOfId2 = _cursorIndexOfId;
            }
            return _result;
        } finally {
            _cursor.close();
            _statement.release();
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public PhloggingEntity getPhlogByTimestamp(String this_timestamp) {
        PhloggingEntity _result;
        String _tmpTitle;
        String _tmpTitle2;
        String _tmpText;
        String _tmpUri;
        String _tmpTextLocation;
        String str = this_timestamp;
        RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire("SELECT * FROM Phlogs WHERE phlogsDB_timestamp=?", 1);
        if (str == null) {
            _statement.bindNull(1);
        } else {
            _statement.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor _cursor = DBUtil.query(this.__db, _statement, false, (CancellationSignal) null);
        try {
            int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "phlogsDB_title");
            int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "phlogsDB_text");
            int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "phlogsDB_uri");
            int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "phlogsDB_timestamp");
            int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "phlogsDB_latitude");
            int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "phlogsDB_longitude");
            int _cursorIndexOfTextLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "phlogsDB_text_location");
            if (_cursor.moveToFirst()) {
                _result = new PhloggingEntity();
                _result.setId(_cursor.getInt(_cursorIndexOfId));
                if (_cursor.isNull(_cursorIndexOfTitle)) {
                    int i = _cursorIndexOfId;
                    _tmpTitle = null;
                } else {
                    int i2 = _cursorIndexOfId;
                    _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
                }
                _result.setTitle(_tmpTitle);
                if (_cursor.isNull(_cursorIndexOfText)) {
                    String str2 = _tmpTitle;
                    _tmpTitle2 = null;
                } else {
                    String str3 = _tmpTitle;
                    _tmpTitle2 = _cursor.getString(_cursorIndexOfText);
                }
                _result.setText(_tmpTitle2);
                if (_cursor.isNull(_cursorIndexOfUri)) {
                    String str4 = _tmpTitle2;
                    _tmpText = null;
                } else {
                    String str5 = _tmpTitle2;
                    _tmpText = _cursor.getString(_cursorIndexOfUri);
                }
                _result.setUri(_tmpText);
                if (_cursor.isNull(_cursorIndexOfTimestamp)) {
                    String str6 = _tmpText;
                    _tmpUri = null;
                } else {
                    String str7 = _tmpText;
                    _tmpUri = _cursor.getString(_cursorIndexOfTimestamp);
                }
                _result.setTimestamp(_tmpUri);
                String str8 = _tmpUri;
                float _tmpLatitude = _cursor.getFloat(_cursorIndexOfLatitude);
                _result.setLatitude(_tmpLatitude);
                float f = _tmpLatitude;
                float _tmpLongitude = _cursor.getFloat(_cursorIndexOfLongitude);
                _result.setLongitude(_tmpLongitude);
                if (_cursor.isNull(_cursorIndexOfTextLocation)) {
                    float f2 = _tmpLongitude;
                    _tmpTextLocation = null;
                } else {
                    float f3 = _tmpLongitude;
                    _tmpTextLocation = _cursor.getString(_cursorIndexOfTextLocation);
                }
                _result.setTextLocation(_tmpTextLocation);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _cursor.close();
            _statement.release();
        }
    }

    public static List<Class<?>> getRequiredConverters() {
        return Collections.emptyList();
    }
}
