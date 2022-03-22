package com.saffarid.bowsandcows.gui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.saffarid.bowsandcows.R
import com.saffarid.bowsandcows.model.Difficulty
import com.saffarid.bowsandcows.model.Game

class StartNewGameDialog() : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val v = LayoutInflater.from(activity!!).inflate(R.layout.start_new_game_dialog, null)

        var difficulty = Game.getDifficult()

        val easy:RadioButton = v.findViewById(R.id.easy)
        val normal:RadioButton = v.findViewById(R.id.normal)
        val hard:RadioButton = v.findViewById(R.id.hard)
        val apply:Button = v.findViewById(R.id.apply)

        when(difficulty){
            Difficulty.EASY -> easy.isChecked = true
            Difficulty.NORMAL -> normal.isChecked = true
            Difficulty.HARD -> hard.isChecked = true
        }

        easy.setOnClickListener(View.OnClickListener { difficulty = Difficulty.EASY })
        normal.setOnClickListener(View.OnClickListener { difficulty = Difficulty.NORMAL })
        hard.setOnClickListener(View.OnClickListener { difficulty = Difficulty.HARD })

        apply.setOnClickListener( View.OnClickListener {
            Game.startNewGame(difficulty)
            dismiss()
        } )

        val dialog =
            AlertDialog.Builder(activity!!)
            .setView(v)
            .create()

        return dialog
    }
}