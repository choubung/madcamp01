package com.example.myapp;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ContactEntity.class}, version = 1)
public abstract class ContactDatabase extends RoomDatabase {
    public abstract ContactDao getContactDao();

    private static volatile ContactDatabase INSTANCE;

    public static ContactDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ContactDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ContactDatabase.class, "contact_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
