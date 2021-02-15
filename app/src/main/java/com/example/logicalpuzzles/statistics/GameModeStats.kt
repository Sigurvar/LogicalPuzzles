package com.example.logicalpuzzles.statistics

import java.util.*

data class GameModeStats(override val name: String, override val unlockCriteria: Int) : Stats {
    private val subGameModeStats = ArrayList<SubGameModeStats>()

    override fun getNumberOfLevels(): Int = subGameModeStats.stream().mapToInt { obj: SubGameModeStats -> obj.getNumberOfLevels() }.sum()
    override fun getNumberOfCompletedLevels(): Int =subGameModeStats.stream().mapToInt { obj: SubGameModeStats -> obj.getNumberOfCompletedLevels() }.sum()

    fun addSubGameMode(name: String, total: Int, completed: Int, unlockCriteria: Int, hintInterval: Int) =
        subGameModeStats.add(SubGameModeStats(
                name = name,
                totalNumberOfLevels = total,
                completedLevels = completed,
                unlockCriteria = unlockCriteria,
                hintInterval = hintInterval))

    fun getSubGameModeStats(subGameMode: String): SubGameModeStats =
            subGameModeStats.stream().filter { s: SubGameModeStats -> s.name == subGameMode }.findFirst().get()

    fun getAllSubGameModeStats(): List<SubGameModeStats> = subGameModeStats
}