package com.iiddd.weather.core.testutils

import com.iiddd.weather.core.utils.coroutines.DispatcherProvider
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class UnitTestDispatcherProvider(
    val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : DispatcherProvider {
    override val main = dispatcher
    override val mainImmediate = dispatcher
    override val default = dispatcher
    override val io = dispatcher
}