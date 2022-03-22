package com.saffarid.bowsandcows.gui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saffarid.bowsandcows.R
import com.saffarid.bowsandcows.model.Game
import com.saffarid.bowsandcows.model.GameStatus
import com.saffarid.bowsandcows.model.HistoryCell

class HistoryFragment : Fragment() {

    private lateinit var history: RecyclerView
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_history, container, false)

        history = v.findViewById(R.id.history)
        history.layoutManager = LinearLayoutManager(activity)
        Game.listeners.add (this::updateUI)
        updateUI()

        return v
    }

    private fun updateUI(){
        if(Game.getGameStatus() == GameStatus.RUN)
        if(history.adapter == null) {
            adapter = HistoryAdapter(Game.getHistory())
            history.adapter = adapter
        }else{
            adapter.setNewData(Game.getHistory())
        }
    }

    internal inner class HistoryCellHolder : RecyclerView.ViewHolder {

        private val number:TextView
        private val bulls:TextView
        private val cows:TextView

        constructor(v: View) : super(v){
            number = v.findViewById(R.id.number)
            bulls = v.findViewById(R.id.bullsCount)
            cows = v.findViewById(R.id.cowsCount)
        }

        public fun bind(historyCell: HistoryCell){
            number.text = historyCell.number.joinToString("")
            bulls.text = historyCell.bulls.toString()
            cows.text = historyCell.cows.toString()
        }
    }

    internal inner class HistoryAdapter(datas: ArrayList<HistoryCell>): RecyclerView.Adapter<HistoryCellHolder>() {

        private var datas: ArrayList<HistoryCell> = arrayListOf()

        init{
            this.datas = datas
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryCellHolder {
            val from = LayoutInflater.from(activity)
            return HistoryCellHolder( from.inflate(R.layout.history_cell_view, parent, false) )
        }

        override fun getItemCount(): Int {
            return this.datas.size
        }

        override fun onBindViewHolder(holder: HistoryCellHolder, position: Int) {
            holder.bind(this.datas[position])
        }

        public fun setNewData(datas: ArrayList<HistoryCell>){
            this.datas = datas
            notifyDataSetChanged()
        }

    }
}