package com.example.home

import org.junit.Assert
import org.junit.Test

class LoginFragmentTest
{
    @Test
    fun testCheckFieldIsEmpty_both_empty() {
        Assert.assertTrue(LoginFragment().checkFieldIsEmpty("", ""))
    }
    @Test
    fun testCheckFieldIsEmpty_username_empty() {
        Assert.assertTrue(LoginFragment().checkFieldIsEmpty("", "asdasd"))
    }
    @Test
    fun testCheckFieldIsEmpty_password_empty() {
        Assert.assertTrue(LoginFragment().checkFieldIsEmpty("test", ""))
    }
    @Test
    fun testCheckFieldIsEmpty_password_filled() {
        Assert.assertFalse(LoginFragment().checkFieldIsEmpty("test", "asdasd"))
    }
}