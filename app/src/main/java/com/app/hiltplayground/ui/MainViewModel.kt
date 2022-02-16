package com.app.hiltplayground.ui

import androidx.lifecycle.*
import com.app.hiltplayground.model.Blog
import com.app.hiltplayground.repository.Repository
import com.app.hiltplayground.utils.DataState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val mainRepository: Repository,
): ViewModel(){


    private val _dataState: MutableLiveData<DataState<List<Blog>>> = MutableLiveData()
    val dataState: LiveData<DataState<List<Blog>>>
    get() = _dataState

    fun setStateEvent(mainStateEvent: MainStateEvent){
        viewModelScope.launch {
            when(mainStateEvent){
                is  MainStateEvent.GetBlogEvents -> {
                    mainRepository.getBlog()
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
                is MainStateEvent.None -> {
                    //do nothing
                }
            }
        }
    }


}

sealed class MainStateEvent{
    object GetBlogEvents: MainStateEvent()

    object None: MainStateEvent()
}
