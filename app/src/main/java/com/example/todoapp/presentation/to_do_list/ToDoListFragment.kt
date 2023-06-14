package com.example.todoapp.presentation.to_do_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentToDoListBinding
import com.example.todoapp.presentation.to_do_list.model.ToDoListItemUIModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ToDoListFragment : Fragment() {

    private val toDoListViewModel: ToDoListViewModel by viewModels()

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

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                toDoListViewModel.toDoListStateFlow.collect { toDoItemList ->
                    if (!toDoListViewModel.isDoneItemsVisibleStateFlow.value) {
                        adapter.setToDoItems(toDoItemList.filter { !it.isDone })
                    } else {
                        adapter.setToDoItems(toDoItemList)
                    }
                    val doneCount = toDoItemList.count { it.isDone }
                    binding.doneCountTextView.text = getString(R.string.done_count, doneCount)
                }
            }

            launch {
                toDoListViewModel.isDoneItemsVisibleStateFlow.collect { isVisible ->
                    val drawableRes: Int
                    val toDoItemList: List<ToDoListItemUIModel>

                    if (isVisible) {
                        drawableRes = R.drawable.visibility
                        toDoItemList = toDoListViewModel.toDoListStateFlow.first()
                    } else {
                        drawableRes = R.drawable.visibility_off
                        toDoItemList = toDoListViewModel.toDoListStateFlow.first().filter {
                            !it.isDone
                        }
                    }

                    binding.doneItemsVisibilityImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            drawableRes
                        )
                    )

                    adapter.setToDoItems(toDoItemList)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}