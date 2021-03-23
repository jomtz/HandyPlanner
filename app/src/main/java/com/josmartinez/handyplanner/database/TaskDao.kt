package com.josmartinez.handyplanner.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.josmartinez.handyplanner.Task
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE id=(:id)")
    fun getTask(id: UUID): LiveData<Task?>
}