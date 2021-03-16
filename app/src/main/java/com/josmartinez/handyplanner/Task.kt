package com.josmartinez.handyplanner

import java.util.*

data class Task(
        val id: UUID = UUID.randomUUID(),
        var title: String ="",
        var date: Date = Date(),
        var isCompleted: Boolean = false
)
