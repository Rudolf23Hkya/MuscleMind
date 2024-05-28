package com.elte_r532ov.musclemind

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.ui.screens.login.LoginEvent
import com.elte_r532ov.musclemind.ui.screens.login.LoginViewModel
import com.elte_r532ov.musclemind.ui.util.Routes
import com.elte_r532ov.musclemind.ui.util.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.mock
import kotlin.time.ExperimentalTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalCoroutinesApi
@ExperimentalTime
class LoginUnitTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var repository: MuscleMindRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        repository = mock(MuscleMindRepository::class.java)
        viewModel = LoginViewModel(repository)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `onSignUpClicked should send navigation event`() = runBlockingTest {
        val events = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.collect { event ->
                events.add(event)
            }
        }

        viewModel.onEvent(LoginEvent.onSignUpClicked)

        assertEquals(listOf(UiEvent.Navigate(Routes.REGISTRATION_ROUTE)), events)
        job.cancel()
    }

    @Test
    fun `onError should send error event`() = runBlockingTest {
        val events = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.collect { event ->
                events.add(event)
            }
        }

        val exception = Exception("Test exception")
        viewModel.onError(exception)

        assertEquals(listOf(UiEvent.ErrorOccured(exception.toString())), events)
        job.cancel()
    }
}