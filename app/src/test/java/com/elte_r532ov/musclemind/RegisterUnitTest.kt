package com.elte_r532ov.musclemind

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elte_r532ov.musclemind.data.enums.ExperienceLevel
import com.elte_r532ov.musclemind.data.enums.Gender
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.ui.screens.register.RegisterEvent
import com.elte_r532ov.musclemind.ui.screens.register.SharedRegisterViewModel
import com.elte_r532ov.musclemind.ui.util.OptiListElement
import com.elte_r532ov.musclemind.ui.util.Routes
import com.elte_r532ov.musclemind.ui.util.UiEvent
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class RegisterUnitTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var repository: MuscleMindRepository
    private lateinit var viewModel: SharedRegisterViewModel

    @Before
    fun setUp() {
        repository = mock(MuscleMindRepository::class.java)
        viewModel = SharedRegisterViewModel(repository)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `onGenderChosen should send navigation event`() = runBlockingTest {
        val events = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.collect { event ->
                events.add(event)
            }
        }

        viewModel.onEvent(RegisterEvent.onGenderChosen(Gender.MALE))

        assertTrue(events.contains(UiEvent.Navigate(Routes.REGISTER_FIZ_DATA)))
        job.cancel()
    }

    @Test
    fun `onFizDataChosen should send navigation event when data is valid`() = runBlockingTest {
        val events = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.collect { event ->
                events.add(event)
            }
        }

        viewModel.onEvent(RegisterEvent.onFizDataChosen("70", "25", "175"))

        assertTrue(events.contains(UiEvent.Navigate(Routes.REGISTER_EXP)))
        job.cancel()
    }

    @Test
    fun `onExperienceChosen should send navigation event`() = runBlockingTest {
        val events = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.collect { event ->
                events.add(event)
            }
        }

        viewModel.onEvent(RegisterEvent.onExperienceChosen(ExperienceLevel.NEW))

        assertTrue(events.contains(UiEvent.Navigate(Routes.REGISTER_DISEASE)))
        job.cancel()
    }

    @Test
    fun `onDiseasesChosen should send navigation event`() = runBlockingTest {
        val events = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.collect { event ->
                events.add(event)
            }
        }

        val diseases = listOf(
            OptiListElement("Asthma", true),
            OptiListElement("Bad Knee", false),
            OptiListElement("Cardiovascular Disease", true),
            OptiListElement("Osteoporosis", false)
        )

        viewModel.onEvent(RegisterEvent.onDiseasesChosen(diseases))

        assertTrue(events.contains(UiEvent.Navigate(Routes.REGISTER_ACCOUNT_DATA)))
        job.cancel()
    }

    @Test
    fun `onUserDataChosen should send navigation event when data is valid`() = runBlockingTest {
        val events = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.collect { event ->
                events.add(event)
            }
        }

        viewModel.onEvent(RegisterEvent.onUserDataChosen("password", "password", "test@example.com", "username"))

        job.cancel()
    }

    @Test
    fun `onUserDataChosen should send error event when passwords do not match`() = runBlockingTest {
        val events = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.collect { event ->
                events.add(event)
            }
        }

        viewModel.onEvent(RegisterEvent.onUserDataChosen("password1", "password2", "test@example.com", "username"))

        assertTrue(events.contains(UiEvent.ErrorOccured("Passwords do not match!")))
        job.cancel()
    }

    @Test
    fun `onUserDataChosen should send error event when email is invalid`() = runBlockingTest {
        val events = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.collect { event ->
                events.add(event)
            }
        }

        viewModel.onEvent(RegisterEvent.onUserDataChosen("password", "password", "invalid-email", "username"))

        assertTrue(events.contains(UiEvent.ErrorOccured("Invalid E-mail address!")))
        job.cancel()
    }

    @Test
    fun `onUserDataChosen should send error event when username is too short`() = runBlockingTest {
        val events = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.collect { event ->
                events.add(event)
            }
        }

        viewModel.onEvent(RegisterEvent.onUserDataChosen("password", "password", "test@example.com", "u"))

        assertTrue(events.contains(UiEvent.ErrorOccured("Username must be at least 2 characters long!")))
        job.cancel()
    }

    @Test
    fun `onUserDataChosen should send error event when username is invalid`() = runBlockingTest {
        val events = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.collect { event ->
                events.add(event)
            }
        }

        viewModel.onEvent(RegisterEvent.onUserDataChosen("password", "password", "test@example.com", "user name"))

        assertTrue(events.contains(UiEvent.ErrorOccured("Enter a valid username. This value may contain only letters, numbers, and @/./+/-/_ characters.")))
        job.cancel()
    }
}