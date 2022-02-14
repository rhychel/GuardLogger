package com.rhymartmanchus.guardlogger.screens.login

sealed interface MainContract {

    interface View {

        fun renderView()
        fun navigateToHome()
        fun showSignUpDialog(
            onSignUpClicked: suspend (employeeId: String, name: String, pin: String) -> SignUpState
        )
        fun toastError(message: String)

    }

    interface Presenter {

        fun takeView(view: View)
        fun onViewCreated()
        fun onLoginClicked(employeeId: String, pin: String)
        fun onSignupClicked()

    }

}