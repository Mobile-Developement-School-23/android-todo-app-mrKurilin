package com.example.todoapp.presentation.to_do_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentToDoListBinding
import com.example.todoapp.di.appComponent
import com.example.todoapp.di.lazyViewModel
import kotlinx.coroutines.launch

class ToDoListFragment : Fragment() {

    private val toDoListViewModel: ToDoListViewModel by lazyViewModel {
        appComponent().toDoListViewModel()
    }

    private var _binding: FragmentToDoListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToDoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.doneItemsVisibilityImageView.setOnClickListener {
            toDoListViewModel.changeDoneItemsVisibility()
        }

        binding.floatingActionButton.setOnClickListener {
            val action = ToDoListFragmentDirections.actionToDoListFragmentToToDoEntryFragment()
            findNavController().navigate(action)
        }

        binding.doneItemsVisibilityImageView.setOnClickListener {
            toDoListViewModel.changeDoneItemsVisibility()
        }

        val adapter = ToDoItemsAdapter(
            deleteToDoItem = { toDoItemId ->
                toDoListViewModel.deleteToDoItem(toDoItemId)
            },
            setDoneToDoItem = { toDoItemId ->
                toDoListViewModel.setDoneToDoItem(toDoItemId)
            },
            editToDoItem = { toDoItemId ->
                val action =
                    ToDoListFragmentDirections.actionToDoListFragmentToToDoEntryFragment(toDoItemId)
                findNavController().navigate(action)
                onDestroy()
            }
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        val itemAnimator = binding.recyclerView.itemAnimator
        if (itemAnimator is SimpleItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                toDoListViewModel.getToDoItemListFlow().collect { toDoItemList ->
                    adapter.setToDoItems(toDoItemList)
                    val doneCount = toDoItemList.count { it.isDone }
                    binding.doneCountTextView.text = getString(R.string.done_count, doneCount)
                }
            }

            launch {
                toDoListViewModel.textToShowFlow.collect { text ->
                    if (text != null) {
                        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
                        toDoListViewModel.toastShown()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}