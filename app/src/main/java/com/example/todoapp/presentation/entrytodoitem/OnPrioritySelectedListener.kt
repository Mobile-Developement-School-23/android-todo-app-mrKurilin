package com.example.todoapp.presentation.entrytodoitem

import android.view.View
import android.widget.AdapterView

class OnPrioritySelectedListener(
    private val onItemSelectedAction: (AdapterView<*>?, View?, Int, Long) -> Unit
) : AdapterView.OnItemSelectedListener {

    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        onItemSelectedAction(parent, view, position, id)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) = Unit
}