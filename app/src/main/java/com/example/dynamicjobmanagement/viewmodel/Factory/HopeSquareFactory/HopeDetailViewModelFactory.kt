package com.example.dynamicjobmanagement.viewmodel.Factory.HopeSquareFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dynamicjobmanagement.model.model.SeekHelp
import com.example.dynamicjobmanagement.viewmodel.viewModel.hopeSquareViewModel.HopeDetailViewModel

class HopeDetailViewModelFactory(private val seekHelp:SeekHelp): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HopeDetailViewModel::class.java)) {
            return HopeDetailViewModel(seekHelp) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}