package io.panha.rd_app.feature.ml_kits

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MLKitsViewModel : ViewModel() {

    val types: StateFlow<List<MLKitType>> = MutableStateFlow(MLKitType.values().toList())
}