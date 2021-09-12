package com.mobline.presentation.base


import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import com.mobline.domain.base.BaseUseCase
import com.mobline.domain.base.CompletionBlock
import com.mobline.domain.exceptions.NetworkError
import com.mobline.domain.exceptions.ServerError
import com.mobline.presentation.tools.NavigationCommand
import com.mobline.presentation.tools.SingleLiveEvent
import com.mobline.marleyspoon.presentation.R
import kotlinx.coroutines.launch
import timber.log.Timber

open class BaseViewModel<State, Effect>() : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    private val _effect = SingleLiveEvent<Effect>()
    val effect: SingleLiveEvent<Effect>
        get() = _effect

    private val _commonEffect = SingleLiveEvent<BaseEffect>()
    val commonEffect: LiveData<BaseEffect>
        get() = _commonEffect

    private val _navigationCommands = SingleLiveEvent<NavigationCommand>()
    val navigationCommands: SingleLiveEvent<NavigationCommand>
        get() = _navigationCommands


    //=== Util functions ===
    protected fun postState(state: State) {
        _state.value = state
    }

    protected fun postEffect(effect: Effect) {
        _effect.postValue(effect)
    }

    fun navigate(directions: NavDirections, extras: Navigator.Extras? = null) {
        _navigationCommands.postValue(NavigationCommand.To(directions, extras))
    }

    fun navigate(command: NavigationCommand) {
        _navigationCommands.postValue(command)
    }


    //=== Argument handlers ===
    private var arguments: Bundle? = null

    fun setArguments(data: Bundle?) {
        arguments = data
    }

    fun getArguments(): Bundle? {
        return arguments
    }

    /**
     * Override if arguments will be used
     */
    open fun loadArguments() {}


    //=== Error handlers ===
    protected fun handleError(t: Throwable) {
        Timber.e(t)
        when (t) {
            is ServerError.ServerIsDown -> _commonEffect.postValue(BackEndError())
            is ServerError.Unexpected -> _commonEffect.postValue(MessageError(R.string.error_unexpected))
            is ServerError.KnownError -> _commonEffect.postValue(MessageErrorWithText(t.serverMessage))
            is NetworkError -> _commonEffect.postValue(NoInternet())
            else -> _commonEffect.postValue(UnknownError(cause = t))
        }
    }

    protected fun handleLoading(state: Boolean) {
        _isLoading.postValue(state)
    }

    //=== Launch for all types ===
    fun launchAll(
        loadingHandle: (Boolean) -> Unit = ::handleLoading,
        errorHandle: (Throwable) -> Unit = ::handleError,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch {
            try {
                loadingHandle(true)
                block()
            } catch (t: Throwable) {
                errorHandle(t)
            } finally {
                loadingHandle(false)
            }
        }
    }


    //==== Launch for BaseUseCases ====
    suspend fun <P, R> BaseUseCase<P, R>.getWith(param: P): R {
        var result: R? = null
        val block: CompletionBlock<R> = {
            onSuccess = { result = it }
            onError = { throw it }
        }
        execute(param, block)
        return result!!
    }

    protected fun <P, R, U : BaseUseCase<P, R>> U.launch(
        param: P,
        loadingHandle: (Boolean) -> Unit = ::handleLoading,
        block: CompletionBlock<R> = {}
    ) {
        viewModelScope.launch {
            val actualRequest = BaseUseCase.Request<R>().apply(block)

            val proxy: CompletionBlock<R> = {
                onStart = {
                    loadingHandle(true)
                    actualRequest.onStart?.invoke()
                }
                onSuccess = {
                    actualRequest.onSuccess(it)
                }
                onCancel = {
                    actualRequest.onCancel?.invoke(it)
                }
                onTerminate = {
                    loadingHandle(false)
                    actualRequest.onTerminate
                }
                onError = {
                    Timber.e(it)
                    actualRequest.onError?.invoke(it) ?: handleError(it)
                }
            }
            execute(param, proxy)
        }
    }

    protected fun <P, R, U : BaseUseCase<P, R>> U.launchNoLoading(
        param: P,
        block: CompletionBlock<R> = {}
    ) {
        viewModelScope.launch {
            val actualRequest = BaseUseCase.Request<R>().apply(block)


            val proxy: CompletionBlock<R> = {
                onStart = actualRequest.onStart
                onSuccess = actualRequest.onSuccess
                onCancel = actualRequest.onCancel
                onTerminate = actualRequest.onTerminate
                onError = {
                    Timber.e(it)
                    actualRequest.onError?.invoke(it) ?: handleError(it)
                }
            }
            execute(param, proxy)
        }
    }

    protected fun <P, R, U : BaseUseCase<P, R>> U.launchUnsafe(
        param: P,
        block: CompletionBlock<R> = {}
    ) {
        viewModelScope.launch { execute(param, block) }
    }

}