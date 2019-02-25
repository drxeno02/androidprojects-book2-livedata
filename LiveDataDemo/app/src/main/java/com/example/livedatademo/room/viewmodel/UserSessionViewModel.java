package com.example.livedatademo.room.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.livedatademo.room.dao.UserSessionDao;
import com.example.livedatademo.room.entity.UserSessionEntity;
import com.example.livedatademo.room.enums.Enum;
import com.example.livedatademo.room.repository.UserSessionRepository;

import java.util.List;
import java.util.Random;

public class UserSessionViewModel extends AndroidViewModel {

    private UserSessionRepository mUserSessionRepository;
    private LiveData<List<UserSessionEntity>> alUserSessionEntity;

    /**
     * Constructor
     *
     * @param application Base class for those who need to maintain global application state
     */
    public UserSessionViewModel(@NonNull Application application) {
        super(application);
        mUserSessionRepository = new UserSessionRepository(application);
        alUserSessionEntity = mUserSessionRepository.getInfoUserSessionLiveList();
    }

    /**
     * Retrieve data from Room database tables
     * <p>Cityway live data list {@link UserSessionDao}</p>
     *
     * @return LiveData is a data holder class that can be observed within a given lifecycle
     */
    public LiveData<List<UserSessionEntity>> getInfo() {
        return alUserSessionEntity;
    }

    /**
     * Method is used to perform INSERT operation
     *
     * @param userSessionEntity User session entity
     */
    public void insert(@NonNull UserSessionEntity userSessionEntity) {
        // set random UID
        // UID can be set while you are seeing all the other database values, but if you do not
        // want to think about it, you can set it as part of the insertion method
        Random rand = new Random();
        userSessionEntity.setUid(rand.nextInt(99999));
        mUserSessionRepository.getInstance().performDatabaseOperation(Enum.DatabaseOperation.INSERT, userSessionEntity);
    }

    /**
     * Method is used to perform UPDATE operation
     *
     * @param userSessionEntity User session entity
     */
    public void update(@NonNull UserSessionEntity userSessionEntity) {
        mUserSessionRepository.getInstance().performDatabaseOperation(Enum.DatabaseOperation.UPDATE, userSessionEntity);
    }

    /**
     * Method is usd to perform DELETE operation
     */
    public void delete(@NonNull UserSessionEntity userSessionEntity) {
        mUserSessionRepository.getInstance().performDatabaseOperation(Enum.DatabaseOperation.DELETE, userSessionEntity);
    }

    /**
     * Method is used to perform DELETE operation for all user session entries
     */
    public void deleteAll() {
        mUserSessionRepository.getInstance().performDatabaseOperation(Enum.DatabaseOperation.DELETE, null);
    }
}
