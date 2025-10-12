package homework1_2

// Цель: добавить уровни сложности, лимит попыток (для HARD), список истории.
// См. "homework 1.2.md".


import kotlin.random.Random


private const val APP_NAME: String = "Guess 0..100"
fun main() {
    println("=== $APP_NAME ===")

    while (true) {
        println("Выберите сложность: EASY | NORMAL | HARD")

        val difficultyInput = readlnOrNull()?.trim()?.uppercase()

        val difficulty = when (difficultyInput) {
            "EASY" -> SimpleConfig.Difficulty.EASY
            "NORMAL" -> SimpleConfig.Difficulty.NORMAL
            "HARD" -> SimpleConfig.Difficulty.HARD
            else -> {
                println("Неизвестная сложность. Используется NORMAL")
                SimpleConfig.Difficulty.NORMAL
            }
        }

        println("Команды: help | exit | stats")
        val config = SimpleConfig.fromDifficutly(difficulty)
        var attemptsCount = 0
        val history = mutableListOf<Int>()
        val randomNumber = Random.nextInt(config.max - config.min + 1) + config.min
        println("Игра ${config.min}".."${config.max} Сложность: $difficulty")


         while (true) {
            print("Введите число [${config.min}..${config.max}] ")
            val input = readlnOrNull()?.trim()
            when {
                input == null || input.isEmpty() -> {
                    println("Пожалуйста, введите число или команду")
                    continue
                }
                input.trim().equals("exit", ignoreCase = true) -> {
                    println("EOF.До встречи!")
                    break
                }
                input.trim().equals("help", ignoreCase = true) -> {
                    println("Команды: help - справка, exit - выход")
                    continue
                }
                else -> {
                    val guess = input.toIntOrNull()
                    if (guess == null) {
                        println("Ошибка: введите число или команду")
                        continue
                    }
                    attemptsCount++
                    history.add(guess)
                    when {
                        guess < randomNumber -> println("Мое число больше")
                        guess > randomNumber -> println("Мое число меньше")
                        else -> {
                            println("Угадал!)")
                            break

                        }
                    }
                    if (config.maxAttempts != null && attemptsCount >= config.maxAttempts) {
                        println("Попытки закончились! Вы проиграли.")
                        println("История попыток: ${history.joinToString(", ")}")
                        println("Загаданное число было: $randomNumber")
                        break
                    }
                }
            }
        }
        println("Хотите сыграть еще раз? (yes/no)")
        val playAgain = readlnOrNull()?.trim()?.lowercase()
        if (playAgain != "yes" && playAgain != "y") {
            println("Спасибо за игру! До встречи!")
            break
        }
        println()

    }
}





    // TODO: запросить сложность через when
    // TODO: создать SimpleConfig из сложности
    // TODO: загадать число
    // TODO: вести счётчик попыток и историю (MutableList<Int>)
    // TODO: если maxAttempts исчерпан — завершить раунд
