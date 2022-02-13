package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.ShiftsGateway
import com.rhymartmanchus.guardlogger.domain.exceptions.EmployeeNotRegisteredException
import com.rhymartmanchus.guardlogger.domain.models.Session
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class LoginEmployeeUseCaseTest {

    @MockK
    lateinit var gateway: ShiftsGateway

    lateinit var useCase: LoginEmployeeUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = LoginEmployeeUseCase(gateway)
    }

    @Test
    fun `should be able to login user using employee id and pin`() = runBlocking {
        val session = mockk<Session>()
        coEvery { gateway.login(any(), any()) } returns session
        coEvery { gateway.saveSession(any()) } returns Unit

        val result = useCase.execute(LoginEmployeeUseCase.Params("1234", "4321"))

        val credsCaptor = mutableListOf<String>()
        coVerifySequence {
            gateway.login(capture(credsCaptor), capture(credsCaptor))
            gateway.saveSession(session)
        }
        assertEquals(session, result.session)
        assertEquals("1234", credsCaptor.first())
        assertEquals("4321", credsCaptor.last())
    }

    @Test
    fun `should throw an exception when employee id is empty`() = runBlocking {
        try {
            useCase.execute(
                LoginEmployeeUseCase.Params("", "4321")
            )
            fail("Use case succeeded")
        } catch (e: Exception) {
            if(e is IllegalArgumentException) {
                if(e.message != "Employee ID cannot be empty") {
                    fail("Exception message is incorrect")
                } else {
                    assert(true)
                }
            } else {
                fail("Exception is not IllegalArgumentException")
            }
        }
    }

    @Test
    fun `should throw an exception when pin is empty`() = runBlocking {
        try {
            useCase.execute(
                LoginEmployeeUseCase.Params("1234", "")
            )
            fail("Use case succeeded")
        } catch (e: Exception) {
            if(e is IllegalArgumentException) {
                if(e.message != "PIN cannot be empty") {
                    fail("Exception message is incorrect")
                } else {
                    assert(true)
                }
            } else {
                fail("Exception is not IllegalArgumentException")
            }
        }
    }

    @Test(expected = EmployeeNotRegisteredException::class)
    fun `should throw an exception when employee id is not yet registered`() = runBlocking {
        coEvery { gateway.login(any(), any())
        } throws EmployeeNotRegisteredException()

        useCase.execute(
            LoginEmployeeUseCase.Params("1234", "4321")
        )

        Unit
    }
}