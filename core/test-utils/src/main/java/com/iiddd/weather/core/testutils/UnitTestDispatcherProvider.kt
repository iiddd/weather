package com.iiddd.weather.core.testutils

import com.iiddd.weather.core.utils.coroutines.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class UnitTestDispatcherProvider @OptIn(ExperimentalCoroutinesApi::class) constructor(
    val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : DispatcherProvider {
    override val main = dispatcher
    override val mainImmediate = dispatcher
    override val default = dispatcher
    override val io = dispatcher
}