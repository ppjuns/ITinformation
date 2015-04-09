package com.rabbit.application.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Rabbit on 2015/3/25.
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper {


    private static final String DATAVASE_NAME = "Infomation.db";

    private static final int DATABASE_VERSION = 1;

    private Dao<Infoitem, Integer> infoItemDao = null;

    private RuntimeExceptionDao<Infoitem, Integer> infoItemRuntimeDao = null;


    private DataBaseHelper(Context context) {
        super(context, DATAVASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Infoitem.class);

            infoItemDao = getInfoItemDao();
            infoItemRuntimeDao = getInfoItemRuntimeDao();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {


        try {
            TableUtils.dropTable(connectionSource, Infoitem.class, true);

            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public Dao<Infoitem, Integer> getInfoItemDao() throws SQLException

    {
        if (infoItemDao == null) {

            infoItemDao = getDao(Infoitem.class);
        }
        return infoItemDao;

    }

    public RuntimeExceptionDao<Infoitem, Integer> getInfoItemRuntimeDao() {
        if (infoItemDao == null) {

            infoItemRuntimeDao = getRuntimeExceptionDao(Infoitem.class);

        }

        return infoItemRuntimeDao;
    }

}
