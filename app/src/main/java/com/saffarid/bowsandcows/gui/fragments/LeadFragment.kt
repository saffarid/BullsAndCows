package com.saffarid.bowsandcows.gui.fragments

import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.saffarid.bowsandcows.R
import com.saffarid.bowsandcows.model.Game
import com.saffarid.bowsandcows.model.GameStatus

class LeadFragment() : Fragment() {

    private val imagePartsId = arrayListOf(
        R.id.housing,
        R.id.beam_up,
        R.id.beam_hor,
        R.id.beam_down,
        R.id.head,
        R.id.body
    )

    private var notMistake = R.color.not_mistake
    private var mistake = R.color.mistake

    private lateinit var hangmanDrawable: LayerDrawable

    private lateinit var cowsCount: TextView
    private lateinit var bullsCount: TextView
    private lateinit var number: TextView

    private lateinit var hangman: ImageView

    init {
        Game.listeners.add(this::refreshLastResult)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_lead, container, false)

        cowsCount = v.findViewById(R.id.cowsCount)
        bullsCount = v.findViewById(R.id.bullsCount)
        number = v.findViewById(R.id.number)

        hangman = v.findViewById(R.id.hangman)

        hangmanDrawable = resources.getDrawable(R.drawable.hangman) as LayerDrawable

        hangman.setImageDrawable(hangmanDrawable)

        refreshLastResult()

        return v
    }

    private fun refreshLastResult() {
        if (Game.getGameStatus() == GameStatus.RUN) {
            if (Game.getHistory().isNotEmpty()) {
                number.setText(Game.getHistory().last().number.joinToString(""))
                cowsCount.setText(Game.getHistory().last().cows.toString())
                bullsCount.setText(Game.getHistory().last().bulls.toString())

                val index = Game.getMistakeCount()
                if (index > 0)
                    hangmanDrawable.getDrawable(index)!!
                        .setColorFilter(resources.getColor(mistake), PorterDuff.Mode.ADD)
            } else {
                number.setText("")
                cowsCount.setText("")
                bullsCount.setText("")

                for (i in 1..(hangmanDrawable.numberOfLayers - 1)) {
                    hangmanDrawable.getDrawable(i)!!
                        .setColorFilter(resources.getColor(notMistake), PorterDuff.Mode.MULTIPLY)
                }
            }

        }
    }
}

