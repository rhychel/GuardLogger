package com.rhymartmanchus.guardlogger.data

import android.content.Context
import android.content.SharedPreferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.auth.FirebaseAuth
import com.rhymartmanchus.guardlogger.data.db.AuthenticationsDao
import com.rhymartmanchus.guardlogger.data.db.GuardLoggerDatabase
import com.rhymartmanchus.guardlogger.data.db.models.UserDB
import com.rhymartmanchus.guardlogger.domain.exceptions.EmployeeAlreadyRegisteredException
import com.rhymartmanchus.guardlogger.domain.exceptions.EmployeeNotRegisteredException
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.models.Session
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ShiftsRepositoryTest {

    lateinit var authenticationsDao: AuthenticationsDao
    lateinit var shiftsRepository: ShiftsRepository
    val appContext by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
    lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp() {
        val database = GuardLoggerDatabase.getInMemoryInstance(appContext)
        authenticationsDao = database.authenticationsDao()
        sharedPreferences = appContext.getSharedPreferences("session_test", Context.MODE_PRIVATE)

        shiftsRepository = ShiftsRepository(
            authenticationsDao,
            sharedPreferences,
            FirebaseAuth.getInstance()
        )
    }

    @Test
    fun getCurrentSession() = runBlocking {
        sharedPreferences.edit()
            .putString("NAME", "rhy")
            .putString("USER_ID", "1")
            .commit()

        val session = shiftsRepository.getCurrentSession()

        assertEquals("rhy", session.name)
        assertEquals("1", session.userId)
    }

    @Test(expected = IllegalStateException::class)
    fun throwIllegalStateExceptionWhenThereIsNoSession() = runBlocking {

        shiftsRepository.getCurrentSession()

        Unit
    }

    @Test(expected = IllegalStateException::class)
    fun throwIllegalStateExceptionWhenNameIsEmpty() = runBlocking {
        sharedPreferences.edit().apply()
        sharedPreferences.edit()
            .putString("USER_ID", "1")
            .commit()

        shiftsRepository.getCurrentSession()

        Unit
    }

    @Test
    fun login() = runBlocking {
        val userId = UUID.randomUUID().toString()
        authenticationsDao.saveUser(
            UserDB(userId, "Rhy", "123-123", "4321")
        )

        val session = shiftsRepository.login("123-123", "4321")

        assertEquals(userId, session.userId)
        assertEquals("Rhy", session.name)
    }

    @Test(expected = EmployeeNotRegisteredException::class)
    fun throwEmployeeNotRegisteredExceptionWhenAuthFailed() = runBlocking {
        shiftsRepository.login("123-123", "4321")

        Unit
    }

    @Test
    fun signUp() = runBlocking {

        shiftsRepository.signup("1234-1234", "Mich", "1234")

        val user = authenticationsDao.getUserByEmployeeId("1234-1234")

        assertEquals(user?.employeeId, "1234-1234")
        assertEquals(user?.name, "Mich")
        assertEquals(user?.pin, "1234")
    }

    @Test(expected = EmployeeAlreadyRegisteredException::class)
    fun throwEmployeeAlreadyRegisteredExceptionWhenItAlreadyExists() = runBlocking {

        val userId = UUID.randomUUID().toString()
        authenticationsDao.saveUser(
            UserDB(userId, "Mich", "1234-1234", "1234")
        )

        shiftsRepository.signup("1234-1234", "Mich", "1234")
    }

    @Test
    fun saveSession() = runBlocking {

        val userId = UUID.randomUUID().toString()
        shiftsRepository.saveSession(
            Session(userId, "Gab")
        )

        val session = shiftsRepository.getCurrentSession()

        assertEquals(userId, session.userId)
        assertEquals("Gab", session.name)
    }

    @After
    fun tearDown() {
        sharedPreferences.edit().clear().commit()
        GuardLoggerDatabase.clearAllTables()
        GuardLoggerDatabase.destroyInstance()
    }
}