package com.socialsirius.messenger.repository.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.socialsirius.messenger.repository.models.LocalMessage;


import java.sql.SQLException;



public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "travelpass.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 6;

    // any time you make changes to your database objects, you may have to increase the database version

    // the DAO object we use to access the SimpleData table

    private Dao<LocalMessage, String> messagesDaoDao = null;




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public ConnectionSource getConnectionSource() {
        return super.getConnectionSource();
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to createOrUpdateUser
     * the tables that will store your data.
     */

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, LocalMessage.class);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, LocalMessage.class, true);

        } catch (Exception e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
        }

        onCreate(db, connectionSource);
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        try {
            TableUtils.dropTable(connectionSource, LocalMessage.class, true);



        } catch (Exception e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
        }

        onCreate(db, connectionSource);
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<LocalMessage, String> getMessagesDao() throws SQLException {
        if (messagesDaoDao == null) {
            messagesDaoDao = getDao(LocalMessage.class);
        }
        return messagesDaoDao;
    }



    public void clearTableAfterLogout() {
        clearMessages();

    }

    public void clearMessages() {
        try{
            TableUtils.clearTable(connectionSource, LocalMessage.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        messagesDaoDao = null;

    }

}