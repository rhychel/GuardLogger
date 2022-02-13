package com.rhymartmanchus.guardlogger.screens.login

import com.rhymartmanchus.guardlogger.domain.IAppDispatchers
import com.rhymartmanchus.guardlogger.domain.exceptions.EmployeeAlreadyRegisteredException
import com.rhymartmanchus.guardlogger.domain.exceptions.EmployeeNotRegisteredException
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.interactors.GetCurrentSessionUseCase
import com.rhymartmanchus.guardlogger.domain.interactors.LoginEmployeeUseCase
import com.rhymartmanchus.guardlogger.domain.interactors.RegisterEmployeeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainPresenter @Inject constructor(
    private val appDispatchers: IAppDispatchers,
    private val loginEmployeeUseCase: LoginEmployeeUseCase,
    private val registerEmployeeUseCase: RegisterEmployeeUseCase,
    private val getCurrentSessionUseCase: GetCurrentSessionUseCase
) : MainContract.Presenter, CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = appDispatchers.ui() + job

    private var view: MainContract.View? = null
    override fun takeView(view: MainContract.View) {
        this.view = view
    }

    override fun onViewCreated() {
        launch {
            try {
                withContext(appDispatchers.io()) {
                    getCurrentSessionUseCase.execute(Unit)
                }
                view?.navigateToHome()
            } catch (e: NoDataException) {
                view?.renderView()
            }
        }
    }

    override fun onLoginClicked(employeeId: String, pin: String) {
        launch {
            try {
                withContext(appDispatchers.io()) {
                    loginEmployeeUseCase.execute(
                        LoginEmployeeUseCase.Params(employeeId, pin)
                    )
                }
                view?.navigateToHome()
            } catch (e: EmployeeNotRegisteredException) {
                view?.toastError("Employee is not registered.")
            } catch (e: IllegalArgumentException) {
                view?.toastError(e.message ?: "Unknown error: ${e.cause}")
            }
        }
    }

    override fun onSignupClicked() {
        launch {
            view?.showSignUpDialog { employeeId, name, pin ->
                try {
                    withContext(appDispatchers.io()) {
                        registerEmployeeUseCase.execute(
                            RegisterEmployeeUseCase.Params(
                                employeeId, name, pin
                            )
                        )
                    }
                    SignUpState.Success
                } catch (e: EmployeeAlreadyRegisteredException) {
                    view?.toastError("Employee is already registered.")
                    SignUpState.NotRegistered
                } catch (e: IllegalArgumentException) {
                    view?.toastError(e.message ?: "Unknown error: ${e.cause}")
                    SignUpState.NotRegistered
                }
            }
        }
    }

}