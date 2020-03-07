package com.pranjaldesai.coronavirustracker.ui.shared

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class SnackbarComponent(containerView: View) {

    private val snackbar = Snackbar.make(
        containerView,
        DEFAULT_ERROR_TEXT, Snackbar.LENGTH_LONG
    )

    fun showErrorSnackBar(duration: Int? = null) {
        generateDuration(duration)
        snackbar.show()
    }

    fun showSnackBar(textId: Int, duration: Int? = null) {
        snackbar.setText(textId)
        generateDuration(duration)
        snackbar.show()
    }

    fun showSnackBar(text: String, duration: Int? = null) {
        snackbar.setText(text)
        generateDuration(duration)
        snackbar.show()
    }

    fun showSnackBarWithAction(textId: Int, actionText: String, action: () -> Unit) {
        showSnackBarWithAction(textId, actionText, action, null, null)
    }

    fun showSnackBarWithAction(
        textId: Int,
        actionText: String,
        action: () -> Unit,
        actionTextColor: Int?,
        duration: Int?
    ) {
        snackbar.setText(textId)
        prepareAction(actionText, action)
        generateActionTextColor(actionTextColor)
        generateDuration(duration)
        snackbar.show()
    }

    fun showSnackBarWithAction(text: String, actionText: String, action: () -> Unit) {
        showSnackBarWithAction(text, actionText, action, null, null)
    }

    fun showSnackBarWithAction(
        text: String,
        actionText: String,
        action: () -> Unit,
        actionTextColor: Int? = null,
        duration: Int? = null
    ) {
        snackbar.setText(text)
        prepareAction(actionText, action)
        generateActionTextColor(actionTextColor)
        generateDuration(duration)
        snackbar.show()
    }

    fun showCustomSnackBar(
        textId: Int,
        backgroundColor: Int? = null,
        textColor: Int? = Color.WHITE,
        duration: Int? = null
    ) {
        snackbar.setText(textId)
        generateDuration(duration)
        val textView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        generateBackgroundColor(backgroundColor)
        generateTextColor(textView, textColor)
        snackbar.show()
    }

    fun showCustomSnackBar(
        text: String,
        backgroundColor: Int? = null,
        textColor: Int? = Color.WHITE,
        duration: Int? = null
    ) {
        snackbar.setText(text)
        generateDuration(duration)
        val textView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        generateBackgroundColor(backgroundColor)
        generateTextColor(textView, textColor)
        snackbar.show()
    }

    fun showCustomSnackBarWithAction(
        textId: Int,
        actionText: String,
        action: () -> Unit,
        actionTextColor: Int? = null,
        backgroundColor: Int? = null,
        textColor: Int? = Color.WHITE,
        duration: Int? = null
    ) {
        snackbar.setText(textId)
        generateDuration(duration)
        val textView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        generateBackgroundColor(backgroundColor)
        generateTextColor(textView, textColor)
        prepareAction(actionText, action)
        generateActionTextColor(actionTextColor)
        snackbar.show()
    }

    fun showCustomSnackBarWithAction(
        text: String,
        actionText: String,
        action: () -> Unit,
        actionTextColor: Int? = null,
        backgroundColor: Int? = null,
        textColor: Int? = Color.WHITE,
        duration: Int? = null
    ) {
        snackbar.setText(text)
        generateDuration(duration)
        val textView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        generateBackgroundColor(backgroundColor)
        generateTextColor(textView, textColor)
        prepareAction(actionText, action)
        generateActionTextColor(actionTextColor)
        snackbar.show()
    }

    fun dismissSnackbar() {
        snackbar.dismiss()
    }

    private fun generateDuration(duration: Int?) {
        if (duration != null) {
            snackbar.duration = duration
        }
    }

    private fun generateBackgroundColor(backgroundColor: Int?) {
        if (backgroundColor != null) {
            snackbar.view.setBackgroundColor(backgroundColor)
        }
    }

    private fun generateTextColor(textView: TextView, textColor: Int?) {
        if (textColor != null) {
            textView.setTextColor(textColor)
        }
    }

    private fun generateActionTextColor(actionTextColor: Int?) {
        if (actionTextColor != null) {
            snackbar.setActionTextColor(actionTextColor)
        }
    }

    private fun prepareAction(actionText: String, action: () -> Unit) {
        snackbar.setAction(actionText) {
            action()
        }
    }

    companion object {
        const val DEFAULT_ERROR_TEXT = "Something went wrong!"
    }
}