package com.josmartinez.handyplanner

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room.databaseBuilder
import com.josmartinez.handyplanner.database.TaskDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "crime-database"

class TaskRepository private constructor(context: Context) {

    private val database : TaskDatabase = databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val taskDao = database.taskDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getTasks(): LiveData<List<Task>> = taskDao.getTasks()

    fun getTask(id: UUID) : LiveData<Task?> = taskDao.getTask(id)

    fun updateTask(task: Task){
        executor.execute {
            taskDao.updateTask(task)
        }
    }

    fun addTask(task: Task){
        executor.execute {
            taskDao.addTask(task)
        }
    }

    companion object {
        private var INSTANCE: TaskRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TaskRepository(context)
            }
        }

        fun get(): TaskRepository {
            return INSTANCE ?:
            throw IllegalStateException("Crime repository must be initialized")
        }
    }

}