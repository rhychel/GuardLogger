package com.rhymartmanchus.guardlogger.screens.login

import com.rhymartmanchus.guardlogger.TestAppDispatcher
import com.rhymartmanchus.guardlogger.domain.IAppDispatchers
import com.rhymartmanchus.guardlogger.domain.exceptions.EmployeeAlreadyRegisteredException
import com.rhymartmanchus.guardlogger.domain.exceptions.EmployeeNotRegisteredException
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.interactors.GetCurrentSessionUseCase
import com.rhymartmanchus.guardlogger.domain.interactors.LoginEmployeeUseCase
import com.rhymartmanchus.guardlogger.domain.interactors.RegisterEmployeeUseCase
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class MainPresenterTest {

    @MockK
    lateinit var loginEmployeeUseCase: LoginEmployeeUseCase

    @MockK
    lateinit var registerEmployeeUseCase: RegisterEmployeeUseCase

    @MockK
    lateinit var getCurrentSessionUseCase: GetCurrentSessionUseCase

    @MockK(relaxUnitFun = true)
    lateinit var view: MainContract.View

    lateinit var presenter: MainPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        presenter = MainPresenter(
            TestAppDispatcher(), loginEmployeeUseCase, registerEmployeeUseCase, getCurrentSessionUseCase
        )
        presenter.takeView(view)
    }

    @Test
    fun `should login when session is not available`() = runBlocking {
        coEvery { getCurrentSessionUseCase.execute(Unit)
            } throws NoDataException()

        presenter.onViewCreated()

        verify(exactly = 1) { view.renderView() }
    }

    @Test
    fun `should navigate to home screen when session is available`() = runBlocking {
        coEvery { getCurrentSessionUseCase.execute(Unit)
            } returns mockk()

        presenter.onViewCreated()

        verify(exactly = 1) { view.navigateToHome() }
    }

    @Test
    fun `should navigate to home screen when login is successful`() = runBlocking {
        coEvery { loginEmployeeUseCase.execute(any())
            } returns LoginEmployeeUseCase.Response(mockk())

        presenter.onLoginClicked("1234-1234", "1234")

        val captor = slot<LoginEmployeeUseCase.Params>()
        coVerifySequence {
            loginEmployeeUseCase.execute(capture(captor))
            view.navigateToHome()
        }
        assertEquals("1234-1234", captor.captured.employeeId)
        assertEquals("1234", captor.captured.pin)
    }

    @Test
    fun `should toast a message when login failed`() = runBlocking {
        coEvery { loginEmployeeUseCase.execute(any())
            } throws EmployeeNotRegisteredException()

        presenter.onLoginClicked("1234-1234", "1234")

        verify { view.toastError("Employee is not registered.") }
    }

    @Test
    fun `should signup employee successfully`() = runBlocking {
        coEvery { registerEmployeeUseCase.execute(any())
            } returns Unit

        presenter.onSignupClicked()

        val captor = slot<RegisterEmployeeUseCase.Params>()
        val onSignUpCaptor = slot<suspend (employeeId: String, name: String, pin: String) -> SignUpState>()
        coVerify {
            view.showSignUpDialog(capture(onSignUpCaptor))
        }
        onSignUpCaptor.captured.invoke("1234-1234", "rhy", "pin")
        coVerify {
            registerEmployeeUseCase.execute(capture(captor))
        }

        with(captor.captured) {
            assertEquals("1234-1234", employeeId)
            assertEquals("rhy", name)
            assertEquals("pin", pin)
        }
    }

    @Test
    fun `should toast a message when signing up an already existing user`() = runBlocking {
        coEvery { registerEmployeeUseCase.execute(any())
        } throws EmployeeAlreadyRegisteredException()

        presenter.onSignupClicked()

        val onSignUpCaptor = slot<suspend (employeeId: String, name: String, pin: String) -> SignUpState>()
        coVerify {
            view.showSignUpDialog(capture(onSignUpCaptor))
        }
        onSignUpCaptor.captured.invoke("1234-1234", "rhy", "pin")
        coVerify {
            registerEmployeeUseCase.execute(any())
            view.toastError("Employee is already registered.")
        }
    }
}