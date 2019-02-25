package com.example.livedatademo.room.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.livedatademo.room.dao.UserSessionDao;
import com.example.livedatademo.room.database.AppDatabase;
import com.example.livedatademo.room.entity.UserSessionEntity;
import com.example.livedatademo.room.enums.Enum;

import java.util.List;

public class UserSessionRepository {
    private static final String TAG = UserSessionRepository.class.getSimpleName();

    private volatile UserSessionRepository mInstance;

    private Context mContext;
    private UserSessionDao mUserSessionDao;
    private LiveData<List<UserSessionEntity>> alUserSessionEntity;

    /**
     * Constructor
     */
    public UserSessionRepository(@NonNull Context context) {
        mContext = context;
        AppDatabase db = AppDatabase.getDatabase(context);
        // instantiate DAO (data access objects)
        mUserSessionDao = db.userSessionDao();
        // instantiate live data objects
        alUserSessionEntity = mUserSessionDao.getAllLiveList();
    }

    /**
     * Method is used to retrieve instance of Room utilities
     * <p>Utilizes singleton pattern by restricting the instantiation of this class
     * and ensuring that only one instance of the class exists in the java virtual machine</p>
     *
     * @return Instance of class
     */
    public UserSessionRepository getInstance() {
        if (mInstance == null) {
            // all synchronized blocks synchronized on the same object can only have
            // one thread executing inside them at a time. All other threads attempting
            // to enter the synchronized block are blocked until the thread inside the
            // synchronized block exits the block
            synchronized (AppDatabase.class) {
                if (mInstance == null) {
                    mInstance = new UserSessionRepository(mContext);
                }
            }
        }
        return mInstance;
    }

    /**
     * Retrieve data from Room database tables
     * <p>Cityway live data list {@link UserSessionDao}</p>
     *
     * @return LiveData is a data holder class that can be observed within a given lifecycle
     */
    public LiveData<List<UserSessionEntity>> getInfoUserSessionLiveList() {
        return alUserSessionEntity;
    }

    /**
     * Method is used to perform operations for UserSessionEntity {@link UserSessionEntity}
     *
     * @param operation         The CRUD operation to perform, e.g. INSERT, UPDATE, DELETE, ect
     * @param userSessionEntity User session entity
     */
    public void performDatabaseOperation(@NonNull Enum.DatabaseOperation operation,
                                         @Nullable UserSessionEntity userSessionEntity) {
        new DatabaseAsyncTask(operation, mUserSessionDao, userSessionEntity).execute();
    }

    /**
     * AsyncTask enables proper and easy use of the UI thread
     * <p>Database CRUD operations e.g. INSERT, UPDATE< DELETE, ect are performed on a seperate thread</p>
     */
    private static class DatabaseAsyncTask extends AsyncTask<Void, Void, Void> {

        private final Enum.DatabaseOperation mAsyncTaskOperation;
        private final UserSessionDao mAsyncTaskUserSessionDao;
        private final UserSessionEntity mAsyncTaskUserSessionEntity;

        /**
         * Constructor
         *
         * @param operation         The CRUD operation to perform, e.g. INSERT, UPDATE, DELETE, ect
         * @param userSessionDao    User session DAO (data access object)
         * @param userSessionEntity User session entity
         */
        DatabaseAsyncTask(@NonNull Enum.DatabaseOperation operation, @NonNull UserSessionDao userSessionDao,
                          @Nullable UserSessionEntity userSessionEntity) {

            mAsyncTaskOperation = operation;
            mAsyncTaskUserSessionDao = userSessionDao;
            mAsyncTaskUserSessionEntity = userSessionEntity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (mAsyncTaskOperation.equals(Enum.DatabaseOperation.INSERT)) {
                if (mAsyncTaskUserSessionEntity != null) {
                    // insert data
                    mAsyncTaskUserSessionDao.insert(mAsyncTaskUserSessionEntity);
                }
            } else if (mAsyncTaskOperation.equals(Enum.DatabaseOperation.UPDATE)) {
                if (mAsyncTaskUserSessionEntity != null) {
                    // update data
                    mAsyncTaskUserSessionDao.update(mAsyncTaskUserSessionEntity);
                }
            } else if (mAsyncTaskOperation.equals(Enum.DatabaseOperation.DELETE)) {
                if (mAsyncTaskUserSessionEntity != null) {
                    // delete specific data
                    mAsyncTaskUserSessionDao.delete(mAsyncTaskUserSessionEntity);
                } else {
                    // delete all data
                    mAsyncTaskUserSessionDao.deleteAll();
                }
            }
            return null;
        }
    }
}
