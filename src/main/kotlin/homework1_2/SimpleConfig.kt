package homework1_2

data class SimpleConfig(
    val min: Int,
    val max: Int,
    val maxAttempts: Int?
){
    init {
        require(min < max) { "min должен быть меньше max" }
    }
    companion object {
        fun fromDifficutly(d: Difficulty) : SimpleConfig = when(d) {
            Difficulty.EASY -> SimpleConfig(0,  50, null)
            Difficulty.NORMAL -> SimpleConfig(0,  100, null)
            Difficulty.HARD -> SimpleConfig(0,  100, 5)
        }

    }
    enum class Difficulty {
        EASY, NORMAL, HARD
    }
}
