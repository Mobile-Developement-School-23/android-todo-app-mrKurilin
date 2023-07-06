package com.example.todoapp.presentation.todolist

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todoapp.R
import com.example.todoapp.databinding.ToDoItemViewHolderBinding
import com.example.todoapp.domain.model.ToDoItemImportance
import com.example.todoapp.presentation.todolist.model.ToDoListItemUIModel
import com.example.todoapp.presentation.util.getColorStateList
import com.example.todoapp.presentation.util.getDrawable

class ToDoItemViewHolder(
    private val binding: ToDoItemViewHolderBinding,
    private val deleteToDoItem: (Int) -> Unit,
    private val editToDoItem: (Int) -> Unit,
    private val setDoneToDoItem: (Int) -> Unit,
) : ViewHolder(binding.root) {

    init {
        binding.root.isLongClickable = true
        binding.root.setOnLongClickListener {
            binding.additionalButtons.visibility = View.VISIBLE
            true
        }
        binding.root.setOnClickListener {
            editToDoItem(adapterPosition)
        }

        binding.infoButton.setOnClickListener {
            editToDoItem(adapterPosition)
        }

        binding.deleteButton.setOnClickListener {
            deleteToDoItem(adapterPosition)
        }

        binding.hideAdditionalButtonsButton.setOnClickListener {
            binding.additionalButtons.visibility = View.GONE
        }

        binding.isDoneCheckbox.setOnClickListener {
            it.isEnabled = false
            setDoneToDoItem(adapterPosition)
        }
    }

    fun bind(toDoListItemUIModel: ToDoListItemUIModel) {
        binding.isDoneCheckbox.isEnabled = true
        binding.isDoneCheckbox.visibility = View.VISIBLE
        binding.additionalButtons.visibility = View.GONE
        binding.isDoneCheckbox.isChecked = toDoListItemUIModel.isDone
        binding.textTextView.text = toDoListItemUIModel.text

        if (toDoListItemUIModel.deadLineDate != null) {
            binding.dateTextView.visibility = View.VISIBLE
            binding.dateTextView.text = toDoListItemUIModel.deadLineDate
        } else {
            binding.dateTextView.visibility = View.GONE
        }

        setImportanceIcon(toDoListItemUIModel.importance)
        setTextPaintFlags(toDoListItemUIModel)
    }

    private fun setTextPaintFlags(toDoListItemUIModel: ToDoListItemUIModel) {
        val paintFlags: Int
        val colorStateList: ColorStateList?

        if (toDoListItemUIModel.isDone) {
            colorStateList = getColorStateList(R.color.color_green)
            paintFlags = binding.textTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else if (toDoListItemUIModel.importance == ToDoItemImportance.IMPORTANT) {
            colorStateList = getColorStateList(R.color.color_red)
            paintFlags = binding.textTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        } else {
            colorStateList = getColorStateList(R.color.support_separator)
            paintFlags = binding.textTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        binding.isDoneCheckbox.buttonTintList = colorStateList
        binding.textTextView.paintFlags = paintFlags
    }

    private fun setImportanceIcon(importance: ToDoItemImportance) = when (importance) {
        ToDoItemImportance.LOW -> {
            binding.priorityImageView.visibility = View.VISIBLE
            val lowPriorityDrawable = getDrawable(R.drawable.low_priority)
            binding.priorityImageView.setImageDrawable(lowPriorityDrawable)
        }

        ToDoItemImportance.BASIC -> {
            binding.priorityImageView.visibility = View.GONE
        }

        ToDoItemImportance.IMPORTANT -> {
            binding.priorityImageView.visibility = View.VISIBLE
            val highPriorityDrawable = getDrawable(R.drawable.high_priority)
            binding.priorityImageView.setImageDrawable(highPriorityDrawable)
        }
    }

    fun isDone(): Boolean {
        return binding.isDoneCheckbox.isChecked
    }
}