package com.sand.flickergalary

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

inline fun <reified T> mock() = Mockito.mock(T::class.java)

inline fun <reified T> whenever(methodCall: T) =  Mockito.`when`(methodCall)
