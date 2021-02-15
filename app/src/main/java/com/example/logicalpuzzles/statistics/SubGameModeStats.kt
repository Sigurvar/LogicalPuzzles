package com.example.logicalpuzzles.statistics

data class SubGameModeStats(override val name: String,
                       override val unlockCriteria: Int,
                       val hintInterval: Int,
                       var completedLevels: Int,
                       var totalNumberOfLevels: Int
                       ) : Stats {
    override fun getNumberOfLevels(): Int = totalNumberOfLevels
    override fun getNumberOfCompletedLevels(): Int = completedLevels

    fun isLastLevel(level: Int): Boolean = level == totalNumberOfLevels

    fun isHintRewardingLevel(level: Int): Boolean {
        if (level == completedLevels + 1) {
            completedLevels++
            return level != 1 && level % hintInterval == 1
        }
        return false
    }
}