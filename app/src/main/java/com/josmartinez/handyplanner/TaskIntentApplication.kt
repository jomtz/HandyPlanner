package com.josmartinez.handyplanner

import android.app.Application

class TaskIntentApplication :Application(){

    override fun onCreate(){
        super.onCreate()
        TaskRepository.initialize(this)
    }
}