package com.saffarid.bowsandcows.gui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saffarid.bowsandcows.R
import com.saffarid.bowsandcows.databinding.HistoryCellViewBinding
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

        private lateinit var binding: HistoryCellViewBinding

        constructor(v: HistoryCellViewBinding) : super(v.root){
            binding = v
        }

        public fun bind(historyCell: HistoryCell){
            binding.cell = historyCell
            binding.executePendingBindings()
        }
    }

    internal inner class HistoryAdapter(datas: ArrayList<HistoryCell>): RecyclerView.Adapter<HistoryCellHolder>() {

        private var datas: ArrayList<HistoryCell> = arrayListOf()

        init{
            this.datas = datas
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryCellHolder {
            val inflater = LayoutInflater.from(parent.context)
            return HistoryCellHolder( DataBindingUtil.inflate<HistoryCellViewBinding>(inflater, R.layout.history_cell_view, parent, false) )
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