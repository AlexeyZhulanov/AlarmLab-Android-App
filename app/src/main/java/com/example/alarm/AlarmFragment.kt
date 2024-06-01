package com.example.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alarm.databinding.FragmentAlarmBinding
import com.example.alarm.model.Alarm
import com.example.alarm.model.AlarmService
import com.example.alarm.model.AlarmsListener


class AlarmFragment : Fragment() {

    private lateinit var adapter: AlarmsAdapter

    private val alarmsService: AlarmService
        get() = (requireActivity().applicationContext as App).alarmsService


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentAlarmBinding.inflate(inflater, container, false)
        adapter = AlarmsAdapter(object: AlarmActionListener {
            override fun onAlarmEnabled(alarm: Alarm) {
                alarm.isEnabled = !alarm.isEnabled
                Toast.makeText(requireContext(), "IsEnabled: ${alarm.isEnabled}", Toast.LENGTH_SHORT).show()
            }

            override fun onAlarmDelete(alarm: Alarm) {
                Toast.makeText(requireContext(), "Alarm minutes: ${alarm.timeMinutes}", Toast.LENGTH_SHORT).show()
            }

            override fun onAlarmChange(alarm: Alarm) {
                Toast.makeText(requireContext(), "Alarm hours: ${alarm.timeHours}", Toast.LENGTH_SHORT).show()
            }
        })
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = adapter

        alarmsService.addListener(alarmsListener)
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar) //adds a button

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        alarmsService.removeListener(alarmsListener)
    }


    private val alarmsListener: AlarmsListener = {
        adapter.alarms = it
    }
}