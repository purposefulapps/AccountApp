package com.example.home

import junit.framework.TestCase
import org.junit.Assert

class RegisterFragmentTest : TestCase() {

    fun testCheckFieldIsEmpty_username_empty() {
        Assert.assertTrue(RegisterFragment().checkFieldIsEmpty("", "asdasd", "asd"))
    }

    fun testCheckFieldIsEmpty_password1_empty() {
        Assert.assertTrue(RegisterFragment().checkFieldIsEmpty("test", "", "asd"))
    }

    fun testCheckFieldIsEmpty_password2_empty() {
        Assert.assertTrue(RegisterFragment().checkFieldIsEmpty("test", "asdasd", ""))
    }

    fun testCheckFieldIsEmpty_all_filled() {
        Assert.assertFalse(RegisterFragment().checkFieldIsEmpty("test", "asdasd", "asdasd"))
    }

    fun testCheckIsPasswordMatch_match() {
        Assert.assertTrue(RegisterFragment().checkIsPasswordMatch("asdasd", "asdasd"))
    }

    fun testCheckIsPasswordMatch_mismatch() {
        Assert.assertFalse(RegisterFragment().checkIsPasswordMatch("asd", "asdasd"))
    }
}