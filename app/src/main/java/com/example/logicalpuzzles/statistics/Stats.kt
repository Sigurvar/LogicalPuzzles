package com.example.logicalpuzzles.statistics

interface Stats {
    val name: String
    val unlockCriteria: Int
    fun getNumberOfLevels(): Int
    fun getNumberOfCompletedLevels(): Int
}