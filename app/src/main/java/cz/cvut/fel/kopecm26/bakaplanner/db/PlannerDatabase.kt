package cz.cvut.fel.kopecm26.bakaplanner.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.cvut.fel.kopecm26.bakaplanner.db.dao.PeriodDao
import cz.cvut.fel.kopecm26.bakaplanner.db.dao.ShiftDao
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

@Database(entities = [Shift::class, SchedulingPeriod::class], version = 13, exportSchema = false)
abstract class PlannerDatabase : RoomDatabase() {
    abstract fun getShiftDao(): ShiftDao
    abstract fun getPeriodDao(): PeriodDao

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
