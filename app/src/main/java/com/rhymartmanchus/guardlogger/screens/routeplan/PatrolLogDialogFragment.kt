package com.rhymartmanchus.guardlogger.screens.routeplan

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.rhymartmanchus.guardlogger.databinding.FragmentAddlogBinding
import com.rhymartmanchus.guardlogger.databinding.FragmentPatrollogBinding
import com.rhymartmanchus.guardlogger.databinding.FragmentSignupBinding
import com.rhymartmanchus.guardlogger.utils.DateTimeFormatter
import java.util.*

class PatrolLogDialogFragment : DialogFragment() {

    private lateinit var locationName: String
    private var startTime: Date? = null
    private var endTime: Date? = null

    private var _binding: FragmentPatrollogBinding? = null
    private val binding: FragmentPatrollogBinding
        get() = _binding!!
    var onSaveLogClickedListener: OnSaveLogClickedListener? = null
    var onDismissClickedListener: OnDismissClickedListener? = null

    fun interface OnSaveLogClickedListener {
        fun onClicked(starTime: String, endTime: String, isCleared: Boolean, description: String)
    }
    fun interface OnDismissClickedListener {
        fun onClicked()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatrollogBinding.inflate(inflater)

        locationName = arguments?.getString("LOCATION_NAME", "") ?: ""

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvLocationName.text = locationName
            btnSaveLog.setOnClickListener {
                onSaveLogClickedListener?.onClicked(
                    etStartTime.text.toString(),
                    etEndTime.text.toString(),
                    swIsCleared.isChecked,
                    etLogs.text.toString()
                )
            }
            btnClose.setOnClickListener {
                onDismissClickedListener?.onClicked()
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