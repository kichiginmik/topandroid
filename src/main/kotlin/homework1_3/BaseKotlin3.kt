// TODO: собрать простую логику игры
// TODO: выделить (MiniResult) и лямбду (MiniResult) -> String
// TODO: сохранять attempts в History<Int>, по завершении вывести сводку

import base_project.KotlinRandomProvider
import base_project.RandomProvider
import homework1_3.GameLogic
import homework1_3.Result
import homework1_3.SimpleConfig
import homework1_3.Stats

private const val APP_NAME: String = "Guess 0..100"


private val resultFormatter: (Result.MiniResult) -> String = { resultArgument ->
    when (resultArgument) {
        is Result.MiniResult.TooLow -> {
            val base = "Моё число больше."
            resultArgument.remainingAttempts?.let { remaining ->
                "$base | Осталось попыток: $remaining"
            } ?: base
        }
        is Result.MiniResult.TooHigh -> {
            val base = "Моё число меньше."
            resultArgument.remainingAttempts?.let { remaining ->
                "$base | Осталось попыток: $remaining"
            } ?: base
        }
        is Result.MiniResult.Correct -> "Поздравляю! Угадано за ${resultArgument.attempts} попыток"
        is Result.MiniResult.OutOfRange -> "Число вне диапазона ${resultArgument.min}..${resultArgument.max}"
    }
}
typealias ResultFormatter = (Result.MiniResult) -> String
fun main() {
    println("=== $APP_NAME ===")

    val difficulty = askedDifficulty()
    val cfg = SimpleConfig.fromDifficutly(difficulty)
    val randomProvider: RandomProvider = KotlinRandomProvider()

    val logic = GameLogic(cfg, randomProvider)
    val stats = Stats()

    while (true) {
        print("Введите число [${cfg.min}..${cfg.max}] или команду: ")
        val input = readlnOrNull()?.trim()

        when {
            input == null -> {
                println("EOF. Выход.")
                break
            }

            input.equals("exit", ignoreCase = true) -> {
                println("До встречи!")
                println(stats.finalizeAndFormat())
                return
            }

            input.equals("help", ignoreCase = true) -> {
                println(helpText(cfg))
                continue
            }

            input.equals("stats", ignoreCase = true) -> {
                println(stats.formatSession())
                continue
            }

            else -> {
                val guess = input.toIntOrNull()
                if (guess == null) {
                    println("Ошибка: введите число или команду")
                    continue
                }

                val result: Result.MiniResult = logic.guessNumber(guess)
                // Убедимся, что ormatter вызывается правильно:
                val formattedResult = resultFormatter(result)
                println(formattedResult) // Выводим отформатированный результат

                stats.onGuess(result)

                if (result is Result.MiniResult.Correct) {
                    println("Секретное число: ${logic.seeLogic()} (угадано за ${result.attempts} попыток)")
                    stats.onRoundFinished(result.attempts)

                    val playAgain = askYesNo("Сыграть еще?")
                    if (playAgain) {
                        logic.reset()
                        stats.startNewRound()
                    } else {
                        break
                    }
                }
            }
        }
    }
    println(stats.finalizeAndFormat())
}

// Остальные функции без изменений...
private fun askedDifficulty(): SimpleConfig.Difficulty {
    while (true) {
        println("Выберите сложность: EASY | NORMAL | HARD")
        val input = readlnOrNull()?.trim()?.uppercase()
        val difficulty = when (input) {
            "EASY" -> SimpleConfig.Difficulty.EASY
            "NORMAL" -> SimpleConfig.Difficulty.NORMAL
            "HARD" -> SimpleConfig.Difficulty.HARD
            else -> {
                println("Неизвестная сложность. Используется NORMAL")
                SimpleConfig.Difficulty.NORMAL
            }
        }
        return difficulty
    }
}

private fun askYesNo(prompt: String): Boolean {
    while (true) {
        print("$prompt (y/n): ")
        when (readlnOrNull()?.trim()?.lowercase()) {
            "y", "yes", "да", "д" -> return true
            "n", "no", "нет", "н" -> return false
            else -> {
                println("Введите y/n или да/нет.")
            }
        }
    }
}

private fun helpText(cfg: SimpleConfig): String = """
    Правила:
    — Я загадываю целое число в диапазоне ${cfg.min}..${cfg.max}.
    — Вводите число, а я отвечаю: больше/меньше/угадал.
    — Команды: help, stats, exit.
""".trimIndent()