package com.example.livedatademo.room.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.livedatademo.room.dao.UserSessionDao;
import com.example.livedatademo.room.entity.UserSessionEntity;


@Database(entities = {UserSessionEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "room.flex.db";
    private static volatile AppDatabase mInstance;

    public abstract UserSessionDao userSessionDao();

    /**
     * This method should be called in the Application so the context parameter can
     * be set to application context since this object will live during the entire
     * application life cycle
     * <p>Utilizes singleton pattern by restricting the instantiation of this class
     * and ensuring that only one instance of the class exists in the java virtual machine</p>
     *
     * @param context Interface to global information about an application environment
     * @return Instance of class
     */
    public static AppDatabase getDatabase(@NonNull Context context) {
        if (mInstance == null) {
            // all synchronized blocks synchronized on the same object can only have
            // one thread executing inside them at a time. All other threads attempting
            // to enter the synchronized block are blocked until the thread inside the
            // synchronized block exits the block
            synchronized (AppDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return mInstance;
    }

    /**
     * Method is used to destroy database instance
     */
    public static void destroy() {
        mInstance = null;
    }
}
