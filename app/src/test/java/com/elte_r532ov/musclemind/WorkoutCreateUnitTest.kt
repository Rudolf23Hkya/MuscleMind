package com.elte_r532ov.musclemind

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import com.elte_r532ov.musclemind.data.api.responses.Exercise
import com.elte_r532ov.musclemind.data.api.responses.Workout
import com.elte_r532ov.musclemind.data.enums.ExperienceLevel
import com.elte_r532ov.musclemind.ui.screens.workouts.create.SharedCreateWorkoutViewModel
import com.elte_r532ov.musclemind.ui.util.OptiListElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.Test

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SharedCreateWorkoutViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var repository: MuscleMindRepository
    private lateinit var viewModel: SharedCreateWorkoutViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(MuscleMindRepository::class.java)
        viewModel = SharedCreateWorkoutViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onWorkoutClicked should update selected workout and exercises`() = runBlockingTest {
        val workout = Workout(
            category = listOf(),
            drawablepicname = "image.png",
            exercise_order = listOf(1, 2),
            exercises = listOf(
                Exercise(1, "Running", "boy", 0, 1, "NEW", "ABS", "Legs", 10),
                Exercise(2, "Jumping Jacks", "boy", 15, 2, "NEW", "ABS", "Full Body", 0)
            ),
            experiencelevel = ExperienceLevel.NEW,
            musclegroup = listOf(),
            name = "Workout 1",
            workoutid = 1,
            userWorkoutId = 1,
            weekly = 4
        )

        viewModel.onWorkoutClicked(workout)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `workoutDataSet with valid data should update trx and weightlifting`() = runBlockingTest {
        val equipmentList = listOf(
            OptiListElement("Trx", true),
            OptiListElement("Dumbbells", true)
        )
        val doWeekly = "3"

        viewModel.workoutDataSet(equipmentList, doWeekly)

        // Verifying internal state changes by checking LiveData values or private variables via reflection
        Assert.assertTrue(viewModel.trx)
        Assert.assertTrue(viewModel.weightlifting)
        Assert.assertEquals(3, viewModel.doWeekly)
    }

}
