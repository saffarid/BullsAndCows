package com.saffarid.bowsandcows.gui.fragments

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.*
import android.os.Build

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.saffarid.bowsandcows.R
import com.saffarid.bowsandcows.databinding.FragmentLeadBinding
import com.saffarid.bowsandcows.model.Game
import com.saffarid.bowsandcows.model.GameStatus

class LeadFragment() : Fragment() {

    private val imagePartsId = arrayListOf(
        R.id.housing,
        R.id.beam_up,
        R.id.beam_hor,
        R.id.beam_down,
        R.id.head,
        R.id.body,
        R.id.left_hand,
        R.id.right_hand,
        R.id.left_foot,
        R.id.right_foot
    )

    private var notMistake = R.color.not_mistake
    private var mistake = R.color.mistake

    private lateinit var hangmanDrawable: LayerDrawable
    private lateinit var binding: FragmentLeadBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lead, container, false)

        hangmanDrawable = resources.getDrawable(R.drawable.hangman, null) as LayerDrawable
        binding.hangman.setImageDrawable(hangmanDrawable)

        refreshLastResult()

        return binding.root
    }

    private fun refreshLastResult() {
        if (Game.getGameStatus() == GameStatus.RUN) {
            if (Game.getHistory().isNotEmpty()) {
                binding.historyCellView.cell = Game.getHistory().last()

                if (Game.getMistakeCount() > 0) {
                    hangmanDrawable.getDrawable(Game.getMistakeCount()).colorFilter =
                        PorterDuffColorFilter(resources.getColor(mistake), PorterDuff.Mode.MULTIPLY)
                }
            } else {
                binding.historyCellView.cell = null

                for (i in 1..hangmanDrawable.numberOfLayers - 1) {
                    hangmanDrawable.getDrawable(i).colorFilter =
                        PorterDuffColorFilter(resources.getColor(notMistake), PorterDuff.Mode.SRC_IN)
                }
            }

        }
    }
}

