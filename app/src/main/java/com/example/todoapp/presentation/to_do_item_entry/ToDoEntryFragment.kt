package com.example.todoapp.presentation.to_do_item_entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentEntryToDoItemBinding
import com.example.todoapp.domain.model.ToDoItemPriority
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class ToDoEntryFragment : Fragment(R.layout.fragment_entry_to_do_item) {

    private val toDoEntryViewModel: ToDoItemEntryViewModel by viewModels()
    private val args: ToDoEntryFragmentArgs by navArgs()

    private var _binding: FragmentEntryToDoItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntryToDoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toDoEntryViewModel.loadToDoItem(args.toDoItemId)

        binding.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveTextView.setOnClickListener {
            val priority = when (binding.prioritySpinner.selectedItem.toString()) {
                getString(R.string.high) -> {
                    ToDoItemPriority.HIGH
                }

                getString(R.string.low) -> {
                    ToDoItemPriority.LOW
                }

                getString(R.string.no) -> {
                    ToDoItemPriority.NORMAL
                }

                else -> {
                    throw IllegalStateException()
                }
            }

            val deadLineDate = if (binding.deadlineSwitch.isChecked) {
                Date(binding.datePickerCalendarView.date)
            } else {
                null
            }

            toDoEntryViewModel.onSavePressed(
                text = binding.textEditText.text.toString(),
                priority = priority,
                deadLineDate = deadLineDate,
            )

            findNavController().popBackStack()
        }

        binding.deleteTextView.setOnClickListener {
            toDoEntryViewModel.deleteToDoItem()
            findNavController().popBackStack()
        }

        binding.deadlineSwitch.setOnClickListener {
            toDoEntryViewModel.onSwitchPressed()
        }

        binding.datePickerCalendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = Calendar.getInstance()
            date.set(year, month, dayOfMonth)
            toDoEntryViewModel.onSelectedDateChanged(date.timeInMillis)
        }

        binding.deleteTextView.isEnabled = false

        lifecycleScope.launch {
            launch {
                toDoEntryViewModel.uiStateFlow.collect { toDoEntryUIState ->
                    updateUI(toDoEntryUIState)
                }
            }

            launch {
                toDoEntryViewModel.selectedDateStringFlow.collect { date ->
                    binding.deadlineDateTextView.text = date
                }
            }
        }
    }

    private fun updateUI(toDoEntryUIState: ToDoEntryUIState) {
        binding.saveTextView.isEnabled = toDoEntryUIState.ableToSave

        binding.deadlineSwitch.isChecked = toDoEntryUIState.enabledDeadLine

        if (toDoEntryUIState.enabledDeadLine) {
            binding.deadlineGroup.visibility = View.VISIBLE
        } else {
            binding.deadlineGroup.visibility = View.GONE
        }

        if (toDoEntryUIState.toDoItemUIModel != null) {
            val toDoItemUIModel = toDoEntryUIState.toDoItemUIModel
            binding.textEditText.setText(toDoItemUIModel.text)
            if (toDoItemUIModel.deadLineDate != null) {
                binding.datePickerCalendarView.date = toDoItemUIModel.deadLineDate
            }

            val position = (binding.prioritySpinner.adapter as ArrayAdapter<String>).getPosition(
                getString(toDoItemUIModel.priorityStringId)
            )

            binding.prioritySpinner.setSelection(position)

            binding.deleteTextView.isEnabled = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}