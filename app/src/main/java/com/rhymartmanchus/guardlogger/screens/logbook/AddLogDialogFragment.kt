package com.rhymartmanchus.guardlogger.screens.logbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.rhymartmanchus.guardlogger.databinding.FragmentAddlogBinding
import com.rhymartmanchus.guardlogger.databinding.FragmentSignupBinding
import com.rhymartmanchus.guardlogger.utils.DateTimeFormatter
import java.util.*

class AddLogDialogFragment : DialogFragment() {

    private var startTime: Date? = null
    private var endTime: Date? = null

    private var _binding: FragmentAddlogBinding? = null
    private val binding: FragmentAddlogBinding
        get() = _binding!!
    var onSaveLogClickedListener: OnSaveLogClickedListener? = null

    fun interface OnSaveLogClickedListener {
        fun onClicked(starTime: String, endTime: String, description: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddlogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnSaveLog.setOnClickListener {
                onSaveLogClickedListener?.onClicked(
                    etStartTime.text.toString(),
                    etEndTime.text.toString(),
                    etLogs.text.toString()
                )
            }
            btnClose.setOnClickListener {
                dismiss()
            }
            etStartTime.setOnClickListener {
                showTimePicker("Start Time", startTime) {
                    startTime = it
                    etStartTime.setText(DateTimeFormatter.toDateTime(startTime ?: throw IllegalArgumentException("startTime is null")))
                }
            }
            etEndTime.setOnClickListener {
                showTimePicker("End Time", endTime) {
                    endTime = it
                    etEndTime.setText(DateTimeFormatter.toDateTime(endTime ?: throw IllegalArgumentException("endTime is null")))
                }
            }
        }
    }

    private fun showTimePicker(title: String, dateTime: Date?, onSet: (Date) -> Unit) {
        val currentTime = Calendar.getInstance()
        dateTime?.let {
            val startTimeCalendar = Calendar.getInstance()
            startTimeCalendar.time = it
            currentTime.set(Calendar.HOUR, startTimeCalendar.get(Calendar.HOUR))
            currentTime.set(Calendar.MINUTE, startTimeCalendar.get(Calendar.MINUTE))
        }
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(currentTime.get(Calendar.HOUR))
            .setMinute(currentTime[Calendar.MINUTE])
            .setTitleText(title)
            .setHour(currentTime[Calendar.HOUR])
            .setMinute(currentTime[Calendar.MINUTE])
            .build()
        picker.addOnPositiveButtonClickListener {
            currentTime.set(Calendar.HOUR, picker.hour)
            currentTime.set(Calendar.MINUTE, picker.minute)
            onSet.invoke(currentTime.time)
        }
        picker.show(childFragmentManager, "TIME_PICKER")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


}