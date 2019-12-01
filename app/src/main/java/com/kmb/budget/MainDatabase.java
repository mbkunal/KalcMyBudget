package com.kmb.budget;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = {TransactionModal.class,CategoryModal.class},version = 1)
@TypeConverters({Converters.class})
public abstract class MainDatabase extends RoomDatabase {

    private static MainDatabase INSTANCE;

    public abstract TransactionDAO transactionDAO();
    public abstract CategoryDAO categoryDAO();

    public static MainDatabase getMainDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), MainDatabase.class, "main-database").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
