package cz.cvut.fel.kopecm26.bakaplanner.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.cvut.fel.kopecm26.bakaplanner.db.dao.ContractDao
import cz.cvut.fel.kopecm26.bakaplanner.db.dao.ShiftDao
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Contract
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

@Database(entities = [Shift::class, Contract::class], version = 9, exportSchema = false)
abstract class PlannerDatabase : RoomDatabase() {
    abstract fun getShiftDao(): ShiftDao
    abstract fun getContractDao(): ContractDao

    companion object {
        fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            PlannerDatabase::class.java,
            PlannerDatabase::class.java.name
        )
            .fallbackToDestructiveMigration()
            .build()

    }
}


