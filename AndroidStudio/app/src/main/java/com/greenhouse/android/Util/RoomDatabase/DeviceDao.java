package com.greenhouse.android.Util.RoomDatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.greenhouse.android.Wrappers.Device;

import java.util.List;

import retrofit2.http.GET;

@Dao
public interface DeviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Device device);

//    @Update
//    void update(Device device);

//    @Delete
//    void delete(Device device);

    @Query("DELETE FROM device_table")
    void deleteAll();

    @Query("SELECT * FROM device_table")
    LiveData<List<Device>> getAllLocal();

}
