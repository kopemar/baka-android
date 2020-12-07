package cz.cvut.fel.kopecm26.bakaplanner.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.cvut.fel.kopecm26.bakaplanner.datasource.dao.ShiftDao
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

@Database(entities = [Shift::class], version = 1, exportSchema = false)
abstract class PlannerDatabase: RoomDatabase() {
    abstract fun getShiftDao(): ShiftDao

    companion object {
        fun buildDatabase(context: Context) = Room.databaseBuilder(context, PlannerDatabase::class.java, PlannerDatabase::class.java.name)
            .fallbackToDestructiveMigration()
            .build()

    }
}


