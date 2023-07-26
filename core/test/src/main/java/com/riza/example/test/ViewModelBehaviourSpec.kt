package com.riza.example.test

import androidx.lifecycle.Observer
import com.riza.example.common.base.BaseViewModel
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.scopes.BehaviorSpecGivenContainerScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
abstract class ViewModelBehaviourSpec<Intent, State, Effect,
    VM : BaseViewModel<Intent, State, Effect>> : BehaviorSpec() {

    abstract val viewModel: VM

    protected val observedStateList: MutableList<State> = mutableListOf()
    protected val observedEffectList: MutableList<Effect> = mutableListOf()

    private val stateObserver = Observer<State> { changedState ->
        observedStateList.add(changedState)
    }

    private var effectCollectJob: Job? = null

    override fun isolationMode() = IsolationMode.InstancePerLeaf

    protected fun BehaviorSpecGivenContainerScope.setupBefore(
        additionalBeforeRun: () -> Unit = {}
    ) {
        beforeEach {
            viewModel.state.observeForever(stateObserver)

            effectCollectJob = testScope.launch(UnconfinedTestDispatcher()) {
                viewModel.effect.collect { event ->
                    event?.peekContent()?.let {
                        observedEffectList.add(it)
                    }
                }
            }
            additionalBeforeRun()
        }
    }

    protected fun BehaviorSpecGivenContainerScope.setupAfter(
        additionalAfterRun: () -> Unit = {}
    ) {
        afterEach {
            viewModel.state.removeObserver(stateObserver)
            effectCollectJob?.cancel()
            observedEffectList.clear()
            observedStateList.clear()
            additionalAfterRun()
        }
    }
}
