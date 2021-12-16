package com.greenhouse.android.Util.RoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.greenhouse.android.Wrappers.APIResponse.LoggedUser;
import com.greenhouse.android.Wrappers.Device;


@Database(entities = {Device.class}, version = 5)
public abstract class DeviceDb extends RoomDatabase {
    private static DeviceDb instance;
    public abstract DeviceCache getDeviceDao();

    public static synchronized DeviceDb getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DeviceDb.class, "greenhouse_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
