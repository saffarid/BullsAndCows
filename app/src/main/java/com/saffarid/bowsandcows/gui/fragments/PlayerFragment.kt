package com.saffarid.bowsandcows.gui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.saffarid.bowsandcows.R
import com.saffarid.bowsandcows.model.DuplicateNumberException
import com.saffarid.bowsandcows.model.Player

class PlayerFragment(player: Player): Fragment() {

    private val player: Player

    private var buttonsInc: Array<ImageButton>   = arrayOf()
    private var textViews : Array<TextView> = arrayOf()
    private var buttonsDec: Array<ImageButton>   = arrayOf()

    private lateinit var accept: ImageButton

    init {
        this.player = player
        this.player.listeners.add(this::refresh)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_player, container, false)

        buttonsInc = arrayOf(v.findViewById(R.id.firstInc), v.findViewById(R.id.secondInc), v.findViewById(R.id.thirdInc), v.findViewById(R.id.fourthInc))
        textViews = arrayOf(v.findViewById(R.id.firstNum), v.findViewById(R.id.secondNum), v.findViewById(R.id.thirdNum), v.findViewById(R.id.fourthNum))
        buttonsDec = arrayOf(v.findViewById(R.id.firstDec), v.findViewById(R.id.secondDec), v.findViewById(R.id.thirdDec), v.findViewById(R.id.fourthDec))

        accept = v.findViewById(R.id.accept)
        accept.setOnClickListener(this::accept)

        for(btn in buttonsInc){
            btn.setOnClickListener(this::increase)
        }

        refresh()

        for(btn in buttonsDec){
            btn.setOnClickListener(this::decrease)
        }

        return v
    }

    /**
     * Функция принятия числа
     * */
    public fun accept(v: View){
        try {
            player.sendNumber()
        } catch (e: DuplicateNumberException){
            Toast.makeText(activity!!, "Есть дубликаты", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Функция уменьшения числа
     * */
    public fun decrease(v: View){
        when (v.id){
            R.id.firstDec   -> player.decrease(0)
            R.id.secondDec  -> player.decrease(1)
            R.id.thirdDec   -> player.decrease(2)
            R.id.fourthDec  -> player.decrease(3)
        }
    }

    /**
     * Функция увеличения числа
     * */
    public fun increase(v: View){
        when (v.id){
            R.id.firstInc   -> player.increase(0)
            R.id.secondInc  -> player.increase(1)
            R.id.thirdInc   -> player.increase(2)
            R.id.fourthInc  -> player.increase(3)
        }
    }

    private fun refresh(){
        for(textView in textViews){
            textView.setText(player.number[textViews.indexOf(textView)].toString())
        }
    }
}