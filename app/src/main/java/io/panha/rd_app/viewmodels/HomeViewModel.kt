package io.panha.rd_app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _types: MutableLiveData<List<SSEType>> = MutableLiveData()
    val types: LiveData<List<SSEType>> = this._types

    init {
        this._types.value = SSEType.values().toList()
    }

    fun handleSelectType(type: SSEType?) {
    }
}