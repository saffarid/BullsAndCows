package com.saffarid.bowsandcows.model

import kotlin.properties.Delegates

class Player(game: Game) {

    private val tag: String = "player.class"

    /**
     * Объект ведущего
     * */
    private val game: Game

    var listeners = ArrayList<() -> Unit>()
    val notifyListeners = { listeners.forEach { it() }}

    /**
     * Предполагаемое число
     * */
    var number: Array<Int> by Delegates.observable(
        arrayOf(
            1,
            2,
            3,
            4
        )
    ) { property, oldValue, newValue -> notifyListeners() }

    init {
        this.game = game
    }

    public fun startNewGame(){
        number = arrayOf( 1, 2, 3, 4 )
    }

    public fun sendNumber() {
        game.check(number)
    }

    public fun increase(index: Int) {
        val newArray = number.clone()
        var toInt = newArray[index]
        if ((toInt + 1) == 10) {
            toInt = 0
        } else {
            toInt++
        }
        newArray[index] = toInt
        number = newArray
    }

    public fun decrease(index: Int) {
        val newArray = number.clone()
        var toInt = newArray[index]

        if ((toInt - 1) == -1) {
            toInt = 9
        } else {
            toInt--
        }
        newArray[index] = toInt
        number = newArray
    }

}