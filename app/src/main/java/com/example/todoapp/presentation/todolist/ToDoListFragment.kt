package com.example.todoapp.presentation.todolist

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentToDoListBinding
import com.example.todoapp.di.dataWorkComponent
import com.example.todoapp.di.lazyViewModel
import com.example.todoapp.presentation.Notification
import com.example.todoapp.presentation.UI_DELAY
import com.example.todoapp.presentation.entrytodoitem.ToDoItemEntryFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ToDoListFragment : Fragment() {

    private val toDoListViewModel: ToDoListViewModel by lazyViewModel {
        dataWorkComponent().toDoListViewModel()
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

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            toDoListViewModel.updateData()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        setRecyclerView()

        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                delay(UI_DELAY)
                toDoListViewModel.updateData()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            toDoListViewModel.toDoListUIStateStateFlow.collect { toDoListUIState ->
                updateUI(toDoListUIState)
            }
        }
    }

    private fun setRecyclerView() {
        binding.recyclerView.adapter = ToDoItemsAdapter(
            deleteToDoItem = { toDoItemId ->
                toDoListViewModel.deleteToDoItem(toDoItemId)
            },
            setDoneToDoItem = { toDoItemId ->
                toDoListViewModel.setDoneToDoItem(toDoItemId)
            },
            editToDoItem = { toDoItemId ->
                val bundle = bundleOf(ToDoItemEntryFragment.TO_DO_ITEM_ID_KEY to toDoItemId)
                findNavController().navigate(R.id.toDoItemEntryFragment, bundle)
            }
        )
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun setOnClickListeners() {
        binding.doneItemsVisibilityCheckbox.setOnClickListener {
            toDoListViewModel.changeDoneItemsVisibility()
        }

        val logOutDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.log_out))
            .setMessage(getString(R.string.sure_to_log_out))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                toDoListViewModel.logOut()
                findNavController().navigate(R.id.loginFragment)
            }
            .setNegativeButton(getString(R.string.no), null)
        binding.logOutImageButton.setOnClickListener {
            logOutDialog.show()
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.toDoItemEntryFragment)
        }
    }

    private fun updateUI(toDoListUIState: ToDoListUIState) {
        binding.doneItemsVisibilityCheckbox.isChecked = toDoListUIState.isDoneItemsVisible

        (binding.recyclerView.adapter as ToDoItemsAdapter).setToDoItems(
            toDoListUIState.toDoListItemUIModelList
        )

        binding.doneCountTextView.text = getString(
            R.string.done_count, toDoListUIState.doneToDoItemsCount
        )

        if (toDoListUIState.notification != null) {
            showNotificationSnackBar(toDoListUIState.notification)
            toDoListViewModel.notificationShown()
        }

        binding.logOutImageButton.isVisible = toDoListUIState.isAuthorized

        if (toDoListUIState.isNoInternetConnection || !toDoListUIState.isAuthorized) {
            binding.syncWarningImageView.isVisible = true
            bindSyncWarning(toDoListUIState)
        } else {
            binding.syncWarningImageView.isVisible = false
        }
    }

    private fun bindSyncWarning(toDoListUIState: ToDoListUIState) {
        val alertDialog = if (!toDoListUIState.isAuthorized) {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.not_authorized))
                .setMessage(getString(R.string.cant_sync_without_auth))
                .setPositiveButton(getString(R.string.it_is_clear)) { _, _ ->
                    findNavController().navigate(R.id.loginFragment)
                }
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.no_network))
                .setMessage(getString(R.string.cant_sync_without_auth))
                .setPositiveButton(getString(R.string.it_is_clear), null)
        }
        binding.syncWarningImageView.setOnClickListener {
            alertDialog.show()
        }
    }

    private fun showNotificationSnackBar(notification: Notification) {
        binding.swipeRefreshLayout.isRefreshing = false
        val stringId = if (notification == Notification.DATA_SYNCHRONIZED) {
            R.string.data_synchronized
        } else {
            R.string.synchronization_error
        }

        Snackbar.make(
            requireView(),
            stringId,
            Snackbar.LENGTH_SHORT
        ).setTextColor(ContextCompat.getColor(requireContext(), R.color.color_white)).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}