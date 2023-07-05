package com.example.todoapp.presentation.entrytodoitem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentEntryToDoItemBinding
import com.example.todoapp.di.appComponent
import com.example.todoapp.di.lazyViewModel
import com.example.todoapp.presentation.entrytodoitem.model.ToDoItemUIModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ToDoItemEntryFragment : Fragment(R.layout.fragment_entry_to_do_item) {

    private val toDoEntryViewModel: ToDoItemEntryViewModel by lazyViewModel {
        appComponent().toDoItemEntryViewModel()
    }

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

        toDoEntryViewModel.loadToDoItem(arguments?.getString(TO_DO_ITEM_ID_KEY))

        binding.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.textEditText.doAfterTextChanged { text ->
            toDoEntryViewModel.textChanged(text.toString())
        }

        binding.saveTextView.setOnClickListener {
            toDoEntryViewModel.onSavePressed(arguments?.getString(TO_DO_ITEM_ID_KEY))
        }

        binding.deleteTextView.setOnClickListener {
            toDoEntryViewModel.deleteToDoItem(arguments?.getString(TO_DO_ITEM_ID_KEY))
        }

        binding.deadlineSwitch.setOnClickListener {
            toDoEntryViewModel.onDeadLineSwitchPressed()
        }

        binding.datePickerCalendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            toDoEntryViewModel.onDeadLineDateChanged(
                Calendar.getInstance().also { it.set(year, month, dayOfMonth) }.timeInMillis
            )
        }

        binding.prioritySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    toDoEntryViewModel.onSpinnerItemSelectedListener(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //do nothing
                }
            }

        binding.deleteTextView.isEnabled = arguments?.getString(TO_DO_ITEM_ID_KEY) != null

        lifecycleScope.launch {
            launch {
                toDoEntryViewModel.toDoItemEntryUIStateMutableStateFlow.collect { toDoItemEntryUIState ->
                    updateUI(toDoItemEntryUIState)
                }
            }
        }
    }

    private fun updateUI(toDoItemEntryUIState: ToDoItemEntryUIState) {
        when (toDoItemEntryUIState) {
            ToDoItemEntryUIState.CanBeClosed -> {
                findNavController().popBackStack()
            }

            ToDoItemEntryUIState.Loading -> {
                binding.saveTextView.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }

            is ToDoItemEntryUIState.ToDoItemUIModelUpdated -> {
                binding.saveTextView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                bindToDoItemUIModel(toDoItemEntryUIState.toDoItemUIModel)
            }
        }
    }

    private fun bindToDoItemUIModel(toDoItemUIModel: ToDoItemUIModel) {

        binding.saveTextView.isEnabled = toDoItemUIModel.text.isNotEmpty()

        binding.deadlineSwitch.isChecked = toDoItemUIModel.deadLineDate != null

        if (toDoItemUIModel.deadLineDate != null) {
            binding.deadlineGroup.visibility = View.VISIBLE
            binding.deadlineDateTextView.text = SimpleDateFormat(
                "dd MMMM yyyy",
                Locale.getDefault()
            ).format(Date(toDoItemUIModel.deadLineDate))
        } else {
            binding.deadlineGroup.visibility = View.GONE
        }

        binding.textEditText.setText(toDoItemUIModel.text)
        binding.textEditText.setSelection(toDoItemUIModel.text.length)

        binding.datePickerCalendarView.date = toDoItemUIModel.deadLineDate ?: Date().time

        binding.prioritySpinner.setSelection(toDoItemUIModel.priorityValue)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val TO_DO_ITEM_ID_KEY = "TO_DO_ITEM_ID_KEY"
    }
}