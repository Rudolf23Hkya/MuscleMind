package com.elte_r532ov.musclemind.ui.screens.login

import androidx.lifecycle.ViewModel
import com.elte_r532ov.musclemind.data.MuscleMindRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: MuscleMindRepository) : ViewModel() {


}