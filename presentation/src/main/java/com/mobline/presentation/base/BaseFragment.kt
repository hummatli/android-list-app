package com.mobline.presentation.base

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.mobline.marleyspoon.presentation.R
import com.mobline.presentation.tools.NavigationCommand
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass

abstract class BaseFragment<State, Effect, ViewModel : BaseViewModel<State, Effect>, B : ViewBinding> :
    Fragment(), LifecycleOwner {

    protected abstract val vmClazz: KClass<ViewModel>
    protected abstract val bindingCallback: (LayoutInflater, ViewGroup?, Boolean) -> B

    val viewModel: ViewModel by lazy { getViewModel(vmClazz) { parametersOf(arguments) } }

    protected lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = bindingCallback.invoke(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.setArguments(arguments)
            viewModel.loadArguments()
        }
    }

    protected open val bindViews: B.() -> Unit = {}

    protected open val loadingDialog: DialogFragment? by lazy { LoadingDialog.build() }

    protected open val noInternetDialog: DialogFragment? by lazy {
        GeneralDialog.build(
            title = R.string.no_internet_title,
            text = resources.getString(R.string.no_internet_error_text),
            action = { it.dismiss() }
        )
    }

    protected open fun observeState(state: State) {
        // override when observing
    }

    protected open fun observeEffect(effect: Effect) {
        // override when observing
    }

    protected open fun showNoInternet() {
        if (noInternetDialog?.isAdded == false)
            noInternetDialog?.show("internet-error-dialog")
    }

    protected open fun showBackEndError() =
        GeneralDialog.build(action = {
            it.dismiss()
        }).show("general-error-dialog")


    protected open fun showError(
        message: Int,
        title: Int? = R.string.unknown_error_title
    ) = GeneralDialog.build(
        text = resources.getString(message),
        title = title ?: R.string.unknown_error_title,
        action = {
            it.dismiss()
        }
    ).show("show-error-dialog")


    protected open fun showError(
        message: String,
        title: Int? = R.string.unknown_error_title
    ) = GeneralDialog.build(
        text = message,
        title = title ?: R.string.unknown_error_title,
        action = {
            it.dismiss()
        }
    ).show("show-error-dialog")


    private fun showToast(message: Int, duration: Int) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    protected fun showToastShort(message: Int) {
        showToast(message, Toast.LENGTH_SHORT)
    }

    protected fun showToastLong(message: Int) {
        showToast(message, Toast.LENGTH_LONG)
    }


    protected open fun showLoading() {
        if (loadingDialog?.isAdded == false) loadingDialog?.show()
    }

    protected open fun hideLoading() {
        if (loadingDialog?.isAdded == true) loadingDialog?.dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(binding)
    }

    @SuppressLint("RestrictedApi")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, ::observeState)
        viewModel.effect.observe(viewLifecycleOwner, ::observeEffect)
        viewModel.commonEffect.observe(viewLifecycleOwner) {
            when (it) {
                is NoInternet -> showNoInternet()
                is BackEndError -> showBackEndError()
                is UnknownError -> showBackEndError()
                is MessageError -> showError(it.messageId, it.titleId)
                is MessageErrorWithText -> showError(it.message)
                else -> error("Unknown BaseEffect: $it")
            }
        }

        viewModel.navigationCommands.observe(viewLifecycleOwner) { command ->
            when (command) {
                is NavigationCommand.To -> {
                    command.extras?.let { extras ->
                        findNavController().navigate(
                            command.directions,
                            extras
                        )
                    } ?: run {
                        findNavController().navigate(
                            command.directions
                        )
                    }
                }
                is NavigationCommand.BackTo -> findNavController().getBackStackEntry(command.destinationId)
                is NavigationCommand.Back -> findNavController().popBackStack()
                is NavigationCommand.ToRoot -> findNavController().popBackStack(
                    findNavController().backStack.first.destination.id,
                    false
                )
            }
        }
    }

    protected fun <T : DialogFragment> T.show(tag: String? = null): T {
        this.show(this@BaseFragment.parentFragmentManager, tag)
        return this
    }

    open fun onNewIntent(intent: Intent?) {}
}