package ru.fintech.presentation.adapters

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView


object SupportedView {

    fun setupSearchViewCondition(
        searchLoop: ImageButton,
        cancelLoop: ImageButton,
        searchView: android.widget.SearchView,
        toolbarTextView: TextView
    ) {
        searchLoop.setOnClickListener {
            searchView.onActionViewExpanded()
            searchLoop.visibility = View.INVISIBLE
            toolbarTextView.visibility = View.INVISIBLE
            searchView.visibility = View.VISIBLE
            cancelLoop.visibility = View.VISIBLE
        }
        cancelLoop.setOnClickListener {
            searchView.visibility = View.INVISIBLE
            cancelLoop.visibility = View.INVISIBLE
            searchLoop.visibility = View.VISIBLE
            toolbarTextView.visibility = View.VISIBLE
        }
    }

    fun isKeyboardOpen(activity: Activity): Boolean {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.isAcceptingText
    }
}
