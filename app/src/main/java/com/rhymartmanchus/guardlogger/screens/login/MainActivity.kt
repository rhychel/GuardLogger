package com.rhymartmanchus.guardlogger.screens.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.rhymartmanchus.guardlogger.R
import com.rhymartmanchus.guardlogger.databinding.ActivityMainBinding
import com.rhymartmanchus.guardlogger.screens.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainContract.View {

    @Inject lateinit var presenter: MainContract.Presenter
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.takeView(this)
        presenter.onViewCreated()
    }

    private fun registerListeners() {
        with(binding) {
            btnLogin.setOnClickListener {
                presenter.onLoginClicked(
                    etEmployeeId.text.toString(),
                    etPIN.text.toString()
                )
            }
            btnSignup.setOnClickListener {
                presenter.onSignupClicked()
            }
        }
    }

    override fun renderView() {
        with(binding) {
            tilEmployeeId.visibility = View.VISIBLE
            tilPin.visibility = View.VISIBLE
            btnLogin.visibility = View.VISIBLE
            btnSignup.visibility = View.VISIBLE
        }
        registerListeners()
    }

    override fun navigateToHome() {
        finish()
        startActivity(Intent(this, HomeActivity::class.java))
    }

    override fun showSignUpDialog(
        onSignUpClicked: suspend (employeeId: String, name: String, pin: String) -> SignUpState
    ) {
        val dialog = SignUpDialogFragment()
        dialog.onSignUpClickedListener = SignUpDialogFragment.OnSignUpClickedListener { employeeId, name, pin ->
            lifecycleScope.launch {
                if(onSignUpClicked.invoke(employeeId, name, pin) == SignUpState.Success) {
                    dialog.dismiss()
                }
            }
        }
        dialog.show(supportFragmentManager, "SIGN_UP")
    }

    override fun toastError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}