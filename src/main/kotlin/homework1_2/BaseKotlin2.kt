package homework1_2

import kotlin.random.Random

// Цель: добавить уровни сложности, лимит попыток (для HARD), список истории.
// См. "homework 1.2.md".

data class SimpleConfig(val min: Int, val max: Int, val maxAttempts: Int?)
enum class SimpleDifficulty { EASY, NORMAL, HARD }

fun main() {
    println("Игра 0..100 — уровни сложности")
    println("Команды: help | exit")

    // Главный цикл: выбор сложности и запуск раунда; по завершении можно сыграть снова
    while (true) {
        // Выбор сложности
        val difficulty: SimpleDifficulty = run {
            while (true) {
                println("Выберите сложность: EASY | NORMAL | HARD (help | exit)")
                val input: String? = readlnOrNull()?.trim()

                when {
                    input == null -> {
                        println("EOF. До встречи!")
                        return
                    }
                    input.isEmpty() -> {
                        println("Пожалуйста, введите сложность или команду")
                        continue
                    }
                }

                when (input.lowercase()) {
                    "easy" -> return@run SimpleDifficulty.EASY
                    "normal" -> return@run SimpleDifficulty.NORMAL
                    "hard" -> return@run SimpleDifficulty.HARD
                    "help" -> {
                        println("EASY/NORMAL — без лимита попыток; HARD — ограничение попыток (например, 7)")
                        continue
                    }
                    "exit" -> {
                        println("До встречи!")
                        return
                    }
                    else -> println("Неизвестная сложность/команда: '$input'. Введите EASY, NORMAL или HARD")
                }
            }
        }

        // Построение конфигурации под выбранную сложность
        val config: SimpleConfig = when (difficulty) {
            SimpleDifficulty.EASY -> SimpleConfig(min = 0, max = 100, maxAttempts = null)
            SimpleDifficulty.NORMAL -> SimpleConfig(min = 0, max = 100, maxAttempts = null)
            SimpleDifficulty.HARD -> SimpleConfig(min = 0, max = 100, maxAttempts = 7)
        }

        val secretNumber: Int = Random.nextInt(config.min, config.max + 1)
        val guessesHistory: MutableList<Int> = mutableListOf()

        println("Загадано число от ${config.min} до ${config.max}. Угадай!")

        var attemptsLeft: Int = config.maxAttempts ?: Int.MAX_VALUE

        // Цикл одного раунда
        while (true) {
            println("Введи число (help | exit)")
            val input: String? = readlnOrNull()?.trim()

            when {
                input == null -> {
                    println("EOF. До встречи!")
                    return
                }
                input.isEmpty() -> {
                    println("Пожалуйста, введите число или команду")
                    continue
                }
            }

            // Команды в ходе раунда
            when (input.lowercase()) {
                "help" -> {
                    println("Угадывай число из диапазона. Вводи целые числа. 'exit' — выход")
                    continue
                }
                "exit" -> {
                    println("До встречи!")
                    return
                }
            }

            // Парсинг числа и проверка диапазона
            val guess: Int? = input.toIntOrNull()
            if (guess == null) {
                println("Ошибка: введите целое число или команду")
                continue
            }
            if (guess < config.min || guess > config.max) {
                println("Число вне диапазона ${config.min}..${config.max}")
                continue
            }

            guessesHistory.add(guess)

            when {
                guess < secretNumber -> println("Моё число больше")
                guess > secretNumber -> println("Моё число меньше")
                else -> {
                    println("Угадал!)")
                    println("История: ${guessesHistory.joinToString(", ")}")
                    if (!askPlayAgain()) return else break
                }
            }

            // Обработка лимита попыток для HARD
            if (config.maxAttempts != null) {
                attemptsLeft -= 1
                if (attemptsLeft <= 0) {
                    println("Попытки закончились. Вы проиграли. Число было $secretNumber")
                    println("История: ${guessesHistory.joinToString(", ")}")
                    if (!askPlayAgain()) return else break
                } else {
                    println("Осталось попыток: $attemptsLeft")
                }
            }
        }
    }
}

private fun askPlayAgain(): Boolean {
    while (true) {
        println("Сыграть ещё? (y/n)")
        val input: String? = readlnOrNull()?.trim()
        when {
            input == null -> {
                println("EOF. До встречи!")
                return false
            }
            input.equals("y", ignoreCase = true) || input.equals("yes", ignoreCase = true) ||
                input.equals("д", ignoreCase = true) || input.equals("да", ignoreCase = true) -> return true
            input.equals("n", ignoreCase = true) || input.equals("no", ignoreCase = true) ||
                input.equals("н", ignoreCase = true) || input.equals("нет", ignoreCase = true) -> return false
            input.equals("exit", ignoreCase = true) -> {
                println("До встречи!")
                return false
            }
            input.equals("help", ignoreCase = true) -> {
                println("Введите 'y' (да) или 'n' (нет). 'exit' — выход")
            }
            else -> println("Пожалуйста, введите 'y' или 'n'")
        }
    }
}