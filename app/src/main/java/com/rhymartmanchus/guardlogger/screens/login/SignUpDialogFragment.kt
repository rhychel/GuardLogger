package com.rhymartmanchus.guardlogger.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.rhymartmanchus.guardlogger.databinding.FragmentSignupBinding

class SignUpDialogFragment : DialogFragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding: FragmentSignupBinding
        get() = _binding!!
    var onSignUpClickedListener: OnSignUpClickedListener? = null

    fun interface OnSignUpClickedListener {
        fun onClicked(employeeId: String, name: String, pin: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnSignup.setOnClickListener {
                onSignUpClickedListener?.onClicked(
                    etEmployeeId.text.toString(),
                    etName.text.toString(),
                    etPIN.text.toString()
                )
            }
            btnClose.setOnClickListener {
                dismiss()
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