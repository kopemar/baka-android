package cz.cvut.fel.kopecm26.bakaplanner.datasource.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entity: T)

    @Update
    suspend fun update(vararg entity: T)

    @Delete
    suspend fun delete(vararg entity: T)

}