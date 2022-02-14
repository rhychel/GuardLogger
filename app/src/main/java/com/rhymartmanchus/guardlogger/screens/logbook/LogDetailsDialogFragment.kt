package com.rhymartmanchus.guardlogger.screens.logbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.rhymartmanchus.guardlogger.R
import com.rhymartmanchus.guardlogger.databinding.FragmentAddlogBinding
import com.rhymartmanchus.guardlogger.databinding.FragmentLogdetailsBinding
import com.rhymartmanchus.guardlogger.databinding.FragmentSignupBinding
import com.rhymartmanchus.guardlogger.domain.models.CheckInLog
import com.rhymartmanchus.guardlogger.utils.DateTimeFormatter
import java.util.*

class LogDetailsDialogFragment(
    private val checkInLog: CheckInLog
) : DialogFragment() {

    private var _binding: FragmentLogdetailsBinding? = null
    private val binding: FragmentLogdetailsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogdetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnClose.setOnClickListener {
                dismiss()
            }
            with(checkInLog) {
                etStartTime.setText(startTime)
                etEndTime.setText(endTime)
                etLogs.setText(log)
                tvLoggedBy.text = getString(R.string.logged_by, employeeName)
            }
        }
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