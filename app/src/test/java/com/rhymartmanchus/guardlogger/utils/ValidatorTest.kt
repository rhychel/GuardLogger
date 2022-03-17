package com.rhymartmanchus.guardlogger.utils

import org.junit.Test

class ValidatorTest {

    @Test
    fun isEmailValid() {

        val isValid = Validator.isEmailValid("rhymart@gmail.com")

        assert(isValid)
    }
}