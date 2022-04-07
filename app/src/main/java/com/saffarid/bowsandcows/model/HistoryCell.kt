package com.saffarid.bowsandcows.model

class HistoryCell(
    number: Array<Int>,
    cows: Int,
    bulls: Int
) {

    val number: Array<Int>
    val cows: Int
    val bulls: Int

    init {
        this.number = number
        this.cows = cows
        this.bulls = bulls
    }

    fun getNumberAsString():String{
        return number.joinToString("")
    }

}