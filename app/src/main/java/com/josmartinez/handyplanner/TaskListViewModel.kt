package com.josmartinez.handyplanner

import androidx.lifecycle.ViewModel

class TaskListViewModel : ViewModel(){

    private val taskRepository = TaskRepository.get()
    val tasks = taskRepository.getTasks()
}