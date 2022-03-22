package com.saffarid.bowsandcows.model

import android.util.Log
import java.util.ArrayList
import kotlin.properties.Delegates

object Game {

    val listeners = ArrayList<() -> Unit>()
    val notifyListeners = { listeners.forEach { it() } }

    /**
     * Карта результата для расчета ошибки
     * */
    private lateinit var lastResultTable: MutableMap<Int, ResultItems>

    private val player: Player = Player(this)

    /**
     * Загаданное число
     * */
    private var puzzledNumber = arrayOf(1, 2, 3, 4)
        get() = field
        set(value) {
            field = value
        }

    /**
     * Статус игры
     * */
    private lateinit var gameStatus: GameStatus

    /**
     * Счётчик ошибок
     * */
    private var mistakesCount: Int = 0

    /**
     * История результатов ответов.
     * Представляет собой список результатов в виде неизменяемой карты.
     * */
    private val history: ArrayList<HistoryCell> = arrayListOf()



    /**
     * Сложность игры
     * */
    private var difficulty: Difficulty = Difficulty.EASY
        set(value) {
            field = value
        }

    init {
        startNewGame(Difficulty.NORMAL)
    }

    /**
     * Старт новой игры
     * */
    public fun startNewGame(difficulty: Difficulty) {
        this.difficulty = difficulty
        val number = mutableSetOf<Int>()
        lastResultTable = mutableMapOf();
        while (number.size < 4) {
            val element = (0..9).random()
            number.add(element)
            lastResultTable[element] = ResultItems.NO
        }
        gameStatus = GameStatus.RUN
        puzzledNumber = number.toTypedArray()
        Log.i("tag", puzzledNumber.joinToString(""))
        history.clear()
        player.startNewGame()
        mistakesCount = 0
        notifyListeners()
    }

    /**
     * Функция проверки числа на кол-во быков и кол-во коров
     * */
    public fun check(num: Array<Int>): HistoryCell {

        if (num.toSet().size != 4) {
            throw DuplicateNumberException()
        }

        //Проверяем на существование числа
        history.forEach {
            if (it.number contentEquals num) return history.last()
        }

        var bulls: Int = 0
        var cows: Int = 0
        var localResultTable = mutableMapOf<Int, ResultItems>()
        for (i in puzzledNumber) {
            localResultTable[i] = ResultItems.NO
        }
        for (i in 0..3) {
            for (j in 0..3) {
                if (i == j) {
                    if (this.puzzledNumber[i] == num[j]) {
                        localResultTable[this.puzzledNumber[i]] = ResultItems.BULL
                        bulls++
                    }
                } else {
                    if (this.puzzledNumber[i] == num[j]) {
                        localResultTable[this.puzzledNumber[i]] = ResultItems.COW
                        cows++
                    }
                }
            }
        }
        if (bulls == 4){
            gameStatus = GameStatus.WIN
        } else {
            calcMistake(localResultTable)
        }
        val historyCell = HistoryCell(num, cows = cows, bulls = bulls)
        putNewResultIntoHistory(historyCell)
        return historyCell
    }

    private fun putNewResultIntoHistory(result: HistoryCell) {
        history.add(result)
        /*
         * Оповещение происходит в этой точке т.к.
         * делегат отрабатывает слушателя только при изменении значения переменной.
         * В случае истории результатов,
         * для срабатывания слушателя делегата
         * необходимо каждый раз переприсваивать переменной ссылку на список.
         * */
        notifyListeners()
    }

    private fun calcMistake(currentRes: MutableMap<Int, ResultItems>) {
        var hasMistake: Boolean = false
        for (num in puzzledNumber) {
            val b = currentRes[num]!!.ordinal <= lastResultTable[num]!!.ordinal
            if (puzzledNumber.indexOf(num) == 0) {
                hasMistake = b
            } else {
                hasMistake = hasMistake && b
            }
        }
        if (hasMistake)
            mistakesCount++
            if(mistakesCount == 6) {
                gameStatus = GameStatus.FAIL
            }
        else
            lastResultTable = currentRes
    }


    fun getDifficult(): Difficulty {
        return difficulty
    }

    public fun getPlayer(): Player {
        return player
    }

    public fun getHistory(): ArrayList<HistoryCell> {
        return history
    }

    public fun getMistakeCount(): Int {
        return mistakesCount
    }

    public fun getGameStatus(): GameStatus {
        return gameStatus
    }
}