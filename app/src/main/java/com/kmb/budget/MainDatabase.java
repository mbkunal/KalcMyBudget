package com.kmb.budget;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {TransactionModal.class,CategoryModal.class,BudgetTransactionModal.class},version = 2)
@TypeConverters({Converters.class})
public abstract class MainDatabase extends RoomDatabase {

    private static MainDatabase INSTANCE;

    public abstract TransactionDAO transactionDAO();
    public abstract CategoryDAO categoryDAO();
    public abstract BudgetTransactionDAO budgetTransactionDAO();
    public static MainDatabase getMainDatabase(Context context) {

        Migration MIGRATION_1_2 = new Migration(1, 2) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("CREATE TABLE `budgetTransactions` (`_id` INTEGER NOT NULL, "
                        + "`toId` INTEGER NOT NULL,"
                        + "`fromId` INTEGER NOT NULL,"
                        + "`amount` INTEGER NOT NULL,"
                        + "`comment` TEXT,"
                        + "`transactionDate` INTEGER,"
                        + "`createDate` INTEGER,"
                        + " PRIMARY KEY(`_id`))");
            }
        };

        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), MainDatabase.class, "main-database").addMigrations(MIGRATION_1_2).build();



        }
        return INSTANCE;


    }



    public static void destroyInstance() {
        INSTANCE = null;
    }
}
