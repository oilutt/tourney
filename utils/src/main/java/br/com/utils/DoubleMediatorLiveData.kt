package br.com.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class DoubleMediatorLiveData<T, S>(t: LiveData<T>, s: LiveData<S>) : MediatorLiveData<Pair<T?, S?>>() {

    init {
        addSource(t) {value = it to s.value}
        addSource(s) {value = t.value to it}
    }
}