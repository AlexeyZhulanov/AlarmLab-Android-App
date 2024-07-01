package com.example.alarm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.alarm.databinding.FragmentBottomsheetBinding
import com.example.alarm.model.Alarm
import com.example.alarm.model.AlarmService
import com.example.alarm.model.MyAlarmManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

interface BottomSheetListener {
    fun onAddAlarm(alarm: Alarm)
    fun onChangeAlarm(alarmOld: Alarm, alarmNew: Alarm)
}

class BottomSheetFragment(
    private val isAdd: Boolean,
    private val oldAlarm: Alarm,
    private val alarmViewModel: AlarmViewModel,
    private val bottomSheetListener: BottomSheetListener
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomsheetBinding

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.timePicker.setIs24HourView(true)
        if(isAdd) {
            binding.timePicker.hour = 7
            binding.timePicker.minute = 0
        }
        else {
            binding.heading.text = "Изменить будильник"
            binding.timePicker.hour = oldAlarm.timeHours
            binding.timePicker.minute = oldAlarm.timeMinutes
            if((oldAlarm.name != "default") && (oldAlarm.name != "")) binding.signalName.setText(oldAlarm.name)
        }
        binding.confirmButton.setOnClickListener {
            if(isAdd) { addNewAlarm() }
            else { changeAlarm(oldAlarm) }
        }
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun addNewAlarm() {
        val alarm = Alarm(
            id = 0,
            timeHours = binding.timePicker.hour,
            timeMinutes = binding.timePicker.minute,
            name = if(binding.signalName.text.toString() == "") "default" else binding.signalName.text.toString() ,
            enabled = 1
        )
        uiScope.launch {
            if(alarmViewModel.addAlarm(alarm, requireContext())) {
                bottomSheetListener.onAddAlarm(alarm)
            }
            else {
                Toast.makeText(context, getString(R.string.error_is_exist), Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }
    }
    private fun changeAlarm(oldAlarm: Alarm) {
        val alarmNew = Alarm(
            id = oldAlarm.id,
            timeHours = binding.timePicker.hour,
            timeMinutes = binding.timePicker.minute,
            name = if(binding.signalName.text.toString() == "") "default" else binding.signalName.text.toString(),
            enabled = oldAlarm.enabled
        )
        uiScope.launch {
            if(alarmViewModel.updateAlarm(alarmNew, requireContext())) {
                bottomSheetListener.onChangeAlarm(oldAlarm, alarmNew)
            }
            else {
                Toast.makeText(context, getString(R.string.error_is_exist), Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }
    }
}