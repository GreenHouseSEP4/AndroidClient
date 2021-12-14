package com.greenhouse.android.Util.RoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.greenhouse.android.Wrappers.Device;


@Database(entities = {Device.class}, version = 2)
public abstract class GreenHouseDatabase extends RoomDatabase {
    private static GreenHouseDatabase instance;
    public abstract DeviceDao getDeviceDao();

    public static synchronized GreenHouseDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GreenHouseDatabase.class, "greenhouse_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
