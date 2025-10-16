package homework1_3
import base_project.RandomProvider

class GameLogic (
    private val config: SimpleConfig,
    private val randomProvider: RandomProvider
) {
    private var logic: Int = randomProvider.nextInt(config.min, config.max)
    private var attempts: Int = 0
    private val history: MutableList<Int> = mutableListOf()

    fun GuessNumber(guess: Int): Result.MiniResult {
        if (guess < config.min || guess > config.max) {
            return Result.MiniResult.OutOfRange(config.min, config.max)
        }
        attempts += 1
        history += guess

        // Исправляем подсчет оставшихся попыток - не меньше 0
        val remaining = attemptsCountRemain()?.let { if (it < 0) 0 else it }

        return when {
            guess < logic -> Result.MiniResult.TooLow(logic - guess, remaining)
            guess > logic -> Result.MiniResult.TooHigh(guess - logic, remaining)
            else -> Result.MiniResult.Correct(attempts)
        }
    }

    fun attemptsCountRemain(): Int? = config.maxAttempts?.let { it - attempts }

    fun SeeLogic(): Int = logic

    fun reset() {
        logic = randomProvider.nextInt(config.min, config.max)
        attempts = 0
        history.clear()
    }

    fun historySnapshot(): List<Int> = history.toList()
}