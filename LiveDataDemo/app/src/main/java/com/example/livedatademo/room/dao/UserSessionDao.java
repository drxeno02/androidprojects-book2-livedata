package com.example.livedatademo.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.livedatademo.room.entity.UserSessionEntity;

import java.util.List;

@Dao
public interface UserSessionDao {

    @Query("SELECT * FROM user_session_table")
    LiveData<List<UserSessionEntity>> getAllLiveList();

    @Query("SELECT * FROM user_session_table")
    List<UserSessionEntity> getAll();

    @Query("DELETE FROM user_session_table")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserSessionEntity userSessionEntities);

    @Delete
    void delete(UserSessionEntity userSessionEntity);

    /**
     * Example of updating UserSessionEntity with no specified position
     * <p>This method will always update the first position</p>
     *
     * @param userSessionEntity User entity that contains user information e.g. name, email, ect
     */
    @Update
    void update(UserSessionEntity userSessionEntity);

}
