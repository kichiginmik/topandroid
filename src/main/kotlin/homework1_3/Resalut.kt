package homework1_3

sealed class Result {
    data class TooLow(val delta: Int) : Result()
    data class TooHigh(val delta: Int) : Result()
    data class Correct(val attempts: Int) : Result()
    data class OutOfRange(val min: Int, val max: Int) : Result()

    sealed class MiniResult {
        data class TooLow(val delta: Int, val remainingAttempts: Int?) : MiniResult()
        data class TooHigh(val delta: Int, val remainingAttempts: Int?) : MiniResult()
        data class Correct(val attempts: Int) : MiniResult()
        data class OutOfRange(val min: Int, val max: Int) : MiniResult()
    }

}
