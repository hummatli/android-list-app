package com.mobline.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.mobline.presentation.delegate.viewBinding
import com.mobline.marleyspoon.presentation.R
import com.mobline.marleyspoon.presentation.databinding.GeneralDialogBinding

class GeneralDialog : DialogFragment() {

    private val binding by viewBinding(GeneralDialogBinding::bind)

    private var title: Int = R.string.unknown_error_title
    private var text: String? = null

    private var canCancel: Boolean = true
    private var buttonText: Int = R.string.close
    private var action: (GeneralDialog) -> Unit = {}
    private var buttonTextColor: Int = R.color.grayish_violet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.general_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.setText(title)
        binding.text.text = text ?: resources.getString(R.string.unknown_error_text)

        binding.button.setText(buttonText)
        binding.button.setTextColor(ContextCompat.getColor(requireContext(), buttonTextColor))
        binding.button.setOnClickListener {
            action(this)
        }
        isCancelable = canCancel

    }

    companion object {
        fun build(
            title: Int = R.string.unknown_error_title,
            text: String,
            cancelable: Boolean = true,
            buttonText: Int = R.string.close,
            buttonTextColor: Int = R.color.grayish_violet,
            action: (GeneralDialog) -> Unit = {}
        ): GeneralDialog {
            return GeneralDialog().apply {
                this.title = title
                this.text = text
                this.canCancel = cancelable
                this.buttonText = buttonText
                this.buttonTextColor = buttonTextColor
                this.action = action
            }
        }

        fun build(
            title: Int = R.string.unknown_error_title,
            cancelable: Boolean = true,
            buttonText: Int = R.string.close,
            buttonTextColor: Int = R.color.grayish_violet,
            action: (GeneralDialog) -> Unit = {}
        ): GeneralDialog {
            return GeneralDialog().apply {
                this.title = title
                this.canCancel = cancelable
                this.buttonText = buttonText
                this.buttonTextColor = buttonTextColor
                this.action = action
            }
        }
    }
}