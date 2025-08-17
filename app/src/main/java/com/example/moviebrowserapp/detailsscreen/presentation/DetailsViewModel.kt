package com.example.moviebrowserapp.detailsscreen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviebrowserapp.detailsscreen.domain.DetailsUseCase
import com.example.moviebrowserapp.detailsscreen.entities.DetailsUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(val useCase: DetailsUseCase ): ViewModel(){

    private var _details = MutableStateFlow<DetailsUi?>(null)
    var details : StateFlow<DetailsUi?> = _details.asStateFlow()

    private var _error = MutableStateFlow<String?>(null)
    var error : StateFlow<String?> = _error.asStateFlow()
    private var _isLoading = MutableStateFlow(true)
    var isLoading : StateFlow<Boolean> = _isLoading.asStateFlow()



  fun getDetails(movieId : Int) {
      viewModelScope.launch {
          _isLoading.value = true
          _details.value = null
          viewModelScope.launch {
              try {
                  _details.value = useCase.invokeDetails(movieId)


              } catch (e: Exception) {
                  _error.value = e.message ?: "Failed To Load Details"
              } finally {
                  _isLoading.value = false
              }
          }
      }
  }
}