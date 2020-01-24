package br.com.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class TripleMediatorLiveData<T, S, R>(t: LiveData<T>, s: LiveData<S>, r: LiveData<R>) : MediatorLiveData<Triple<T?, S?, R?>>() {

    init {
        addSource(t) {value = Triple(it, s.value, r.value)}
        addSource(s) {value = Triple(t.value, it, r.value)}
        addSource(r) {value = Triple(t.value, s.value, it)}
    }
}