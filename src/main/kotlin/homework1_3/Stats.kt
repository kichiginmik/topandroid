package homework1_3



import java.time.Instant
class History<T> : Iterable<T> {
    private val items = mutableListOf<T>()

    fun add(item: T) { items += item }
    fun toList(): List<T> = items.toList()

    override fun iterator(): Iterator<T> = items.iterator()
}

class Stats {
    private var sessionStart: Instant = Instant.now()
    private var roundStart: Instant = Instant.now()
    private var roundsPlayed: Int = 0
    private var bestAttempts: Int? = null
    private val attemptsPerRound = History<Int>()

    fun onRoundFinished(attempts: Int) {
        attemptsPerRound.add(attempts)
        roundsPlayed += 1
        bestAttempts = when (bestAttempts) {
            null -> attempts
            else -> minOf(bestAttempts!!, attempts)
        }
    }
    fun startNewRound() {
        roundStart = Instant.now()
    }

    fun formatSession(): String {
        val attemptsText = attemptsPerRound.toList().joinToString()
        return """            
            Раундов сыграно: $roundsPlayed
            Попытки по раундам: $attemptsText
            Лучший результат (меньше — лучше): ${bestAttempts ?: "-"}
        """.trimIndent()
    }
}
