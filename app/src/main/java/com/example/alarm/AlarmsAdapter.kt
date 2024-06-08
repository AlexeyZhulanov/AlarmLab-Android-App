package com.example.alarm

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alarm.databinding.ItemAlarmBinding
import com.example.alarm.model.Alarm

interface AlarmActionListener {
    fun onAlarmEnabled(alarm: Alarm)
    fun onAlarmChange(alarm: Alarm)
    fun onAlarmLongClicked()
}

class AlarmsAdapter(
    private val actionListener: AlarmActionListener
) : RecyclerView.Adapter<AlarmsAdapter.AlarmsViewHolder>(), View.OnClickListener, View.OnLongClickListener {

    var alarms: List<Alarm> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    var canLongClick: Boolean = true
    private var checkedPositions: MutableSet<Int> = mutableSetOf()

    fun getDeleteList(): List<Alarm> {
        val list = mutableListOf<Alarm>()
        for(i in checkedPositions) list.add(alarms[i])
        return list
    }

    fun clearPositions() {
        canLongClick = true
        checkedPositions.clear()
    }
    private fun savePosition(alarm: Alarm) {
            for (i in alarms.indices) {
                if (alarms[i] == alarm) {
                    if (i in checkedPositions) {
                        checkedPositions.remove(i)
                    } else {
                        checkedPositions.add(i)
                    }
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(v: View) {
        val alarm = v.tag as Alarm
        when(v.id) {
            R.id.switch1 -> {
                if(canLongClick) actionListener.onAlarmEnabled(alarm)
            }
            R.id.checkBox -> {
                if(!canLongClick) {
                    savePosition(alarm)
                }
            }
            else -> {
                if(canLongClick) actionListener.onAlarmChange(alarm)
                else {
                    savePosition(alarm)
                    notifyDataSetChanged()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onLongClick(v: View?): Boolean {
        if(canLongClick) {
            val alarm = v?.tag as Alarm
            savePosition(alarm)
            actionListener.onAlarmLongClicked()
            notifyDataSetChanged()
            canLongClick = false
        }
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAlarmBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this) //list<alarm> element
        binding.root.setOnLongClickListener(this)
        binding.switch1.setOnClickListener(this)
        binding.checkBox.setOnClickListener(this)
        binding.root.isHapticFeedbackEnabled = true //for test
        return AlarmsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmsViewHolder, position: Int) {
            val alarm = alarms[position]
            with(holder.binding) {
                holder.itemView.tag = alarm
                switch1.tag = alarm
                var tm = ""
                if (alarm.timeMinutes == 0) tm = "0"
                val txt: String = "${alarm.timeHours}:${alarm.timeMinutes}${tm}"
                timeTextView.text = txt
                switch1.isChecked = alarm.enabled == 1
                if (!canLongClick) {
                    checkBox.tag = alarm
                    switch1.visibility = View.INVISIBLE
                    checkBox.visibility = View.VISIBLE
                    checkBox.isChecked = position in checkedPositions
                }
                else {
                    switch1.visibility = View.VISIBLE
                    checkBox.visibility = View.GONE
                }
            }
    }
    override fun getItemCount(): Int = alarms.size

    class AlarmsViewHolder(
        val binding: ItemAlarmBinding
    ) : RecyclerView.ViewHolder(binding.root)
}