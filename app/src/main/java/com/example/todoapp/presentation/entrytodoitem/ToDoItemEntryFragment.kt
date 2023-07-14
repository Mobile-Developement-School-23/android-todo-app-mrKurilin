package com.example.todoapp.presentation.entrytodoitem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.di.dataWorkComponent
import com.example.todoapp.di.lazyViewModel
import com.example.todoapp.presentation.entrytodoitem.compose.ToDoItemEntryScreen
import kotlinx.coroutines.launch

class ToDoItemEntryFragment : Fragment() {

    private val toDoEntryViewModel: ToDoItemEntryViewModel by lazyViewModel {
        dataWorkComponent().toDoItemEntryViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ToDoItemEntryScreen(
                    toDoEntryViewModel.uiState,
                    onToDoItemEntryUIAction = toDoEntryViewModel::onToDoItemEntryUIAction
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toDoEntryViewModel.loadToDoItem(arguments?.getString(TO_DO_ITEM_ID_KEY))

        lifecycleScope.launch {
            toDoEntryViewModel.canBeClosed.collect { canBeClosed ->
                if (canBeClosed) {
                    findNavController().navigate(R.id.toDoListFragment)
                }
            }
        }
    }

    companion object {

        const val TO_DO_ITEM_ID_KEY = "TO_DO_ITEM_ID_KEY"
    }
}