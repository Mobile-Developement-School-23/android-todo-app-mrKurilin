package com.example.todoapp.presentation.entrytodoitem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.todoapp.R
import com.example.todoapp.di.dataWorkComponent
import com.example.todoapp.di.lazyViewModel
import com.example.todoapp.presentation.entrytodoitem.compose.AppTheme
import com.example.todoapp.presentation.entrytodoitem.compose.ToDoItemEntryScreen

class ToDoItemEntryFragment : Fragment() {

    private val toDoEntryViewModel: ToDoItemEntryViewModel by lazyViewModel {
        dataWorkComponent().toDoItemEntryViewModel()
    }

//    private var _binding: FragmentEntryToDoItemBinding? = null
//    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        _binding = FragmentEntryToDoItemBinding.inflate(inflater, container, false)
//        return binding.root
        return inflater.inflate(R.layout.fragment_entry_to_do_item_compose, container, false)
            .apply {
                findViewById<ComposeView>(R.id.compose_view).setContent {
                    AppTheme {
                        ToDoItemEntryScreen(
                            arguments?.getString(TO_DO_ITEM_ID_KEY),
                            toDoEntryViewModel
                        )
                    }
                }
            }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        toDoEntryViewModel.loadToDoItem(arguments?.getString(TO_DO_ITEM_ID_KEY))
//
//        setClickListeners()
//        setChangeListeners()
//        setEditText()
//
//        binding.deleteTextView.isEnabled = arguments?.getString(TO_DO_ITEM_ID_KEY) != null
//
//        lifecycleScope.launch {
//            toDoEntryViewModel.toDoItemEntryUIStateMutableStateFlow.collect { toDoItemEntryUIState ->
//                updateUI(toDoItemEntryUIState)
//            }
//        }
//    }

//    private fun setEditText() {
//        binding.textEditText.imeOptions = EditorInfo.IME_ACTION_DONE
//        binding.textEditText.setRawInputType(InputType.TYPE_CLASS_TEXT)
//        binding.textEditText.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                hideKeyboard()
//                true
//            } else {
//                false
//            }
//        }
//    }

//    private fun setChangeListeners() {
//        binding.textEditText.doAfterTextChanged { text ->
//            toDoEntryViewModel.textChanged(text.toString())
//        }
//
//        binding.datePickerCalendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
//            toDoEntryViewModel.onDeadLineDateChanged(
//                Calendar.getInstance().also { it.set(year, month, dayOfMonth) }.timeInMillis
//            )
//        }
//
//        val itemSelectedListener = OnPrioritySelectedListener { _, _, pos, _ ->
//            toDoEntryViewModel.onSpinnerItemSelectedListener(pos)
//        }
//        binding.prioritySpinner.onItemSelectedListener = itemSelectedListener
//    }

//    private fun setClickListeners() {
//        binding.closeButton.setOnClickListener {
//            findNavController().popBackStack()
//        }
//
//        binding.saveTextView.setOnClickListener {
//            toDoEntryViewModel.onSavePressed(arguments?.getString(TO_DO_ITEM_ID_KEY))
//        }
//
//        binding.deleteTextView.setOnClickListener {
//            toDoEntryViewModel.deleteToDoItem(arguments?.getString(TO_DO_ITEM_ID_KEY))
//        }
//
//        binding.deadlineSwitch.setOnClickListener {
//            toDoEntryViewModel.onDeadLineSwitchPressed()
//        }
//    }

//    private fun updateUI(toDoItemEntryUIState: ToDoItemEntryUIState) {
//        when (toDoItemEntryUIState) {
//            ToDoItemEntryUIState.Closing -> {
//                findNavController().popBackStack()
//            }
//
//            ToDoItemEntryUIState.Loading -> {
//                binding.saveTextView.visibility = View.GONE
//                binding.progressBar.visibility = View.VISIBLE
//            }
//
//            is ToDoItemEntryUIState.ToDoItemUIModelUpdated -> {
//                binding.saveTextView.visibility = View.VISIBLE
//                binding.progressBar.visibility = View.GONE
//                bindToDoItemUIModel(toDoItemEntryUIState.toDoItemUIModel)
//            }
//
//            ToDoItemEntryUIState.ShowInit -> {
//                binding.saveTextView.visibility = View.VISIBLE
//                binding.progressBar.visibility = View.GONE
//            }
//        }
//    }

//    private fun bindToDoItemUIModel(toDoItemUIModel: ToDoItemUIModel) {
//
//        binding.saveTextView.isEnabled = toDoItemUIModel.text.isNotEmpty()
//
//        binding.deadlineSwitch.isChecked = toDoItemUIModel.deadLineDate != null
//
//        if (toDoItemUIModel.deadLineDate != null) {
//            binding.deadlineGroup.visibility = View.VISIBLE
//            binding.deadlineDateTextView.text = SimpleDateFormat(
//                "dd MMMM yyyy",
//                Locale.getDefault()
//            ).format(Date(toDoItemUIModel.deadLineDate))
//        } else {
//            binding.deadlineGroup.visibility = View.GONE
//        }
//
//        binding.textEditText.setText(toDoItemUIModel.text)
//        binding.textEditText.setSelection(toDoItemUIModel.text.length)
//
//        binding.datePickerCalendarView.date = toDoItemUIModel.deadLineDate ?: Date().time
//
//        binding.prioritySpinner.setSelection(toDoItemUIModel.priorityValue)
//    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }

    companion object {

        const val TO_DO_ITEM_ID_KEY = "TO_DO_ITEM_ID_KEY"
    }
}