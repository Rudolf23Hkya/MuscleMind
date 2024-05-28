package com.elte_r532ov.musclemind
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.enums.StatsFormat
import com.elte_r532ov.musclemind.data.enums.StatsMode
import com.elte_r532ov.musclemind.ui.screens.stats.StatsViewModel
import com.elte_r532ov.musclemind.ui.util.UiEvent
import junit.framework.TestCase.assertEquals
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
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class StatsUnitTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var repository: MuscleMindRepository
    private lateinit var viewModel: StatsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = Mockito.mock(MuscleMindRepository::class.java)
        viewModel = StatsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `nextWeek should update currentDate and fetch new stats`() = runBlockingTest {
        val initialDate = viewModel.currentDate.value
        val expectedDate = initialDate.plusWeeks(1)

        viewModel.nextWeek()

        assertEquals(expectedDate, viewModel.currentDate.value)
        Mockito.verify(repository).getStats(expectedDate.year, expectedDate.monthValue, expectedDate.dayOfMonth)
    }

    @Test
    fun `previousWeek should update currentDate and fetch new stats`() = runBlockingTest {
        val initialDate = viewModel.currentDate.value
        val expectedDate = initialDate.minusWeeks(1)

        viewModel.previousWeek()

        assertEquals(expectedDate, viewModel.currentDate.value)
        Mockito.verify(repository).getStats(expectedDate.year, expectedDate.monthValue, expectedDate.dayOfMonth)
    }

    @Test
    fun `setMode should update selectedMode and reload data`() = runBlockingTest {
        val initialMode = viewModel.selectedMode.value
        val newMode = if (initialMode == StatsMode.KCAL) StatsMode.TIME else StatsMode.KCAL

        viewModel.setMode(newMode)

        assertEquals(newMode, viewModel.selectedMode.value)
    }

    @Test
    fun `sendStatsByEmail should call repository and send snackbar event`() = runBlockingTest {
        val events = mutableListOf<UiEvent>()
        val job = launch {
            viewModel.uiEvent.collect { event ->
                events.add(event)
            }
        }

        viewModel.sendStatsByEmail(StatsFormat.CSV)
        Mockito.verify(repository).getStatsViaEmail(csv = true, pdf = false)

        viewModel.sendStatsByEmail(StatsFormat.PDF)
        Mockito.verify(repository).getStatsViaEmail(csv = false, pdf = true)

        assertTrue(events.contains(UiEvent.ShowSnackbar("Your e-mail will arrive soon!")))
        job.cancel()
    }
}