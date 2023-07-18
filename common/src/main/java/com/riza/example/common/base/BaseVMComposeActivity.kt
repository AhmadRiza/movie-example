package com.riza.example.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import javax.inject.Inject
import kotlinx.coroutines.launch

abstract class BaseVMComposeActivity
<Intent, State, Effect, VM : com.riza.example.common.base.BaseViewModel<Intent, State, Effect>> : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: VM

    protected abstract fun inject()

    abstract fun provideViewModel(): Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        viewModel = ViewModelProvider(this, viewModelFactory).get(provideViewModel())

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.effect.collect { effect ->
                    effect?.getContentIfNotHandled()?.let {
                        renderEffect(it)
                    }
                }
            }
        }
    }

    abstract fun renderEffect(effect: Effect)

    protected fun dispatch(intent: Intent) {
        viewModel.onIntentReceived(intent)
    }
}
