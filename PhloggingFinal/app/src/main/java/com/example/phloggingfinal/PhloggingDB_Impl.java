package com.example.phloggingfinal;

import android.annotation.SuppressLint;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class PhloggingDB_Impl extends PhloggingDB {
    private volatile PhloggingAccess _phloggingAccess;

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
        return configuration.sqliteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration.builder(configuration.context).name(configuration.name).callback(new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
            public void createAllTables(SupportSQLiteDatabase _db) {
                _db.execSQL("CREATE TABLE IF NOT EXISTS `Phlogs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `phlogsDB_title` TEXT, `phlogsDB_text` TEXT, `phlogsDB_uri` TEXT, `phlogsDB_timestamp` TEXT, `phlogsDB_latitude` REAL NOT NULL, `phlogsDB_longitude` REAL NOT NULL, `phlogsDB_text_location` TEXT)");
                _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_Phlogs_phlogsDB_timestamp` ON `Phlogs` (`phlogsDB_timestamp`)");
                _db.execSQL(RoomMasterTable.CREATE_QUERY);
                _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'f73552d8142e2bf79891518931aea300')");
            }

            public void dropAllTables(SupportSQLiteDatabase _db) {
                _db.execSQL("DROP TABLE IF EXISTS `Phlogs`");
                if (PhloggingDB_Impl.this.mCallbacks != null) {
                    int _size = PhloggingDB_Impl.this.mCallbacks.size();
                    for (int _i = 0; _i < _size; _i++) {
                        ((RoomDatabase.Callback) PhloggingDB_Impl.this.mCallbacks.get(_i)).onDestructiveMigration(_db);
                    }
                }
            }

            /* access modifiers changed from: protected */
            public void onCreate(SupportSQLiteDatabase _db) {
                if (PhloggingDB_Impl.this.mCallbacks != null) {
                    int _size = PhloggingDB_Impl.this.mCallbacks.size();
                    for (int _i = 0; _i < _size; _i++) {
                        ((RoomDatabase.Callback) PhloggingDB_Impl.this.mCallbacks.get(_i)).onCreate(_db);
                    }
                }
            }

            public void onOpen(SupportSQLiteDatabase _db) {
                SupportSQLiteDatabase unused = PhloggingDB_Impl.this.mDatabase = _db;
                PhloggingDB_Impl.this.internalInitInvalidationTracker(_db);
                if (PhloggingDB_Impl.this.mCallbacks != null) {
                    int _size = PhloggingDB_Impl.this.mCallbacks.size();
                    for (int _i = 0; _i < _size; _i++) {
                        ((RoomDatabase.Callback) PhloggingDB_Impl.this.mCallbacks.get(_i)).onOpen(_db);
                    }
                }
            }

            public void onPreMigrate(SupportSQLiteDatabase _db) {
                DBUtil.dropFtsSyncTriggers(_db);
            }

            public void onPostMigrate(SupportSQLiteDatabase _db) {
            }

            /* access modifiers changed from: protected */
            public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
                HashMap<String, TableInfo.Column> _columnsPhlogs = new HashMap<>(8);
                _columnsPhlogs.put("phlogsDB_title", new TableInfo.Column("phlogsDB_title", "TEXT", false, 0, (String) null, TableInfo.CREATED_FROM_ENTITY));
                _columnsPhlogs.put("phlogsDB_text", new TableInfo.Column("phlogsDB_text", "TEXT", false, 0, (String) null, TableInfo.CREATED_FROM_ENTITY));
                _columnsPhlogs.put("phlogsDB_uri", new TableInfo.Column("phlogsDB_uri", "TEXT", false, 0, (String) null, TableInfo.CREATED_FROM_ENTITY));
                _columnsPhlogs.put("phlogsDB_timestamp", new TableInfo.Column("phlogsDB_timestamp", "TEXT", false, 0, (String) null, TableInfo.CREATED_FROM_ENTITY));
                _columnsPhlogs.put("phlogsDB_latitude", new TableInfo.Column("phlogsDB_latitude", "REAL", true, 0, (String) null, TableInfo.CREATED_FROM_ENTITY));
                _columnsPhlogs.put("phlogsDB_longitude", new TableInfo.Column("phlogsDB_longitude", "REAL", true, 0, (String) null, TableInfo.CREATED_FROM_ENTITY));
                _columnsPhlogs.put("phlogsDB_text_location", new TableInfo.Column("phlogsDB_text_location", "TEXT", false, 0, (String) null, TableInfo.CREATED_FROM_ENTITY));
                HashSet<TableInfo.ForeignKey> _foreignKeysPhlogs = new HashSet<>(0);
                HashSet<TableInfo.Index> _indicesPhlogs = new HashSet<>(1);
                _indicesPhlogs.add(new TableInfo.Index("index_Phlogs_phlogsDB_timestamp", true, Arrays.asList(new String[]{"phlogsDB_timestamp"}), Arrays.asList(new String[]{"ASC"})));
                TableInfo _infoPhlogs = new TableInfo("Phlogs", _columnsPhlogs, _foreignKeysPhlogs, _indicesPhlogs);
                TableInfo _existingPhlogs = TableInfo.read(_db, "Phlogs");
                if (_infoPhlogs.equals(_existingPhlogs)) {
                    return new RoomOpenHelper.ValidationResult(true, (String) null);
                }
                return new RoomOpenHelper.ValidationResult(false, "Phlogs(edu.miami.cs.tem.phlogging.PhloggingEntity).\n Expected:\n" + _infoPhlogs + "\n Found:\n" + _existingPhlogs);
            }
        }, "f73552d8142e2bf79891518931aea300", "94d37d8a078d027885077dea878e4850")).build());
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, new HashMap<>(0), new HashMap<>(0), "Phlogs");
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public void clearAllTables() {
        super.assertNotMainThread();
        SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
        try {
            super.beginTransaction();
            _db.execSQL("DELETE FROM `Phlogs`");
            super.setTransactionSuccessful();
        } finally {
            super.endTransaction();
            _db.query("PRAGMA wal_checkpoint(FULL)").close();
            if (!_db.inTransaction()) {
                _db.execSQL("VACUUM");
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
        HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<>();
        _typeConvertersMap.put(PhloggingAccess.class, PhloggingAccess_Impl.getRequiredConverters());
        return _typeConvertersMap;
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
        return new HashSet<>();
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public List<Migration> getAutoMigrations(Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> map) {
        return Arrays.asList(new Migration[0]);
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public PhloggingAccess daoAccess() {
        PhloggingAccess phloggingAccess;
        if (this._phloggingAccess != null) {
            return this._phloggingAccess;
        }
        synchronized (this) {
            if (this._phloggingAccess == null) {
                this._phloggingAccess = new PhloggingAccess_Impl(this);
            }
            phloggingAccess = this._phloggingAccess;
        }
        return phloggingAccess;
    }
}
