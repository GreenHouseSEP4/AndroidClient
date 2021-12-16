package com.greenhouse.android.Util.RoomDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.greenhouse.android.Wrappers.APIResponse.LoggedUser;
import com.greenhouse.android.Wrappers.Device;

import java.util.List;


@Dao
public interface UserCache {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(LoggedUser user);

    @Query("DELETE FROM user_table")
    void deleteAll();

    @Query("SELECT * FROM user_table")
    LiveData<List<LoggedUser>> getAllLocal();
}
