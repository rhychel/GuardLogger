package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.ShiftsGateway
import com.rhymartmanchus.guardlogger.domain.exceptions.EmployeeAlreadyRegisteredException
import com.rhymartmanchus.guardlogger.domain.exceptions.EmployeeNotRegisteredException
import com.rhymartmanchus.guardlogger.domain.models.Session
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class RegisterEmployeeUseCaseTest {

    @MockK
    lateinit var gateway: ShiftsGateway

    lateinit var useCase: RegisterEmployeeUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = RegisterEmployeeUseCase(gateway)
    }


    @Test
    fun `should be able to signup user using employee id, name and pin`() = runBlocking {
        coEvery { gateway.signup(any(), any(), any()) } returns Unit

        useCase.execute(
            RegisterEmployeeUseCase.Params(
                "1234",
                "Rhymart Manchus",
                "9876"
            )
        )

        val credsCaptor = mutableListOf<String>()
        coVerify { gateway.signup(capture(credsCaptor), capture(credsCaptor), capture(credsCaptor)) }

        assertEquals("1234", credsCaptor.first())
        assertEquals("Rhymart Manchus", credsCaptor[1])
        assertEquals("9876", credsCaptor.last())
    }

    @Test
    fun `should throw an exception when employee id is empty`() = runBlocking {
        try {
            useCase.execute(
                RegisterEmployeeUseCase.Params(
                    "",
                    "Rhymart Manchus",
                    "9876"
                )
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
    fun `should throw an exception when name is empty`() = runBlocking {
        try {
            useCase.execute(
                RegisterEmployeeUseCase.Params(
                    "1234",
                    "",
                    "1234"
                )
            )
            fail("Use case succeeded")
        } catch (e: Exception) {
            if(e is IllegalArgumentException) {
                if(e.message != "Name cannot be empty") {
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
                RegisterEmployeeUseCase.Params(
                    "1234",
                    "Rhymart",
                    ""
                )
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

    @Test(expected = EmployeeAlreadyRegisteredException::class)
    fun `should throw an exception when employee id is already registered`() = runBlocking {
        coEvery { gateway.signup(any(), any(), any())
        } throws EmployeeAlreadyRegisteredException()

        useCase.execute(
            RegisterEmployeeUseCase.Params(
                "1234",
                "Rhymart",
                "1234"
            )
        )
    }
}