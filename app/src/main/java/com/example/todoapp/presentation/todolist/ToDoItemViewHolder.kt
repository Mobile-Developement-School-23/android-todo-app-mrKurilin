package com.example.todoapp.presentation.todolist

import android.graphics.Paint
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todoapp.R
import com.example.todoapp.databinding.ToDoItemViewHolderBinding
import com.example.todoapp.domain.model.ToDoItemImportance
import com.example.todoapp.presentation.todolist.model.ToDoListItemUIModel

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

        val paintFlags: Int

        when (toDoListItemUIModel.priority) {
            ToDoItemImportance.LOW -> {
                binding.priorityImageView.visibility = View.VISIBLE
                val lowPriorityDrawable = AppCompatResources.getDrawable(
                    itemView.context,
                    R.drawable.low_priority
                )
                binding.priorityImageView.setImageDrawable(lowPriorityDrawable)
            }

            ToDoItemImportance.BASIC -> {
                binding.priorityImageView.visibility = View.GONE
            }

            ToDoItemImportance.IMPORTANT -> {
                binding.priorityImageView.visibility = View.VISIBLE
                val highPriorityDrawable = AppCompatResources.getDrawable(
                    itemView.context,
                    R.drawable.high_priority
                )
                binding.priorityImageView.setImageDrawable(highPriorityDrawable)
            }
        }

        if (toDoListItemUIModel.isDone) {
            binding.isDoneCheckbox.buttonTintList = ContextCompat.getColorStateList(
                itemView.context, R.color.color_green
            )
            paintFlags = binding.textTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else if (toDoListItemUIModel.priority == ToDoItemImportance.IMPORTANT) {
            binding.isDoneCheckbox.buttonTintList = ContextCompat.getColorStateList(
                itemView.context, R.color.color_red
            )
            paintFlags = binding.textTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        } else {
            binding.isDoneCheckbox.buttonTintList = ContextCompat.getColorStateList(
                itemView.context, R.color.support_separator
            )
            paintFlags = binding.textTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        binding.textTextView.paintFlags = paintFlags
    }

    fun isDone(): Boolean {
        return binding.isDoneCheckbox.isChecked
    }
}