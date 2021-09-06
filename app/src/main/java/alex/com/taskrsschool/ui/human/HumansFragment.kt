package alex.com.taskrsschool.ui.human

import alex.com.taskrsschool.R
import alex.com.taskrsschool.common.EDITOR_RESULT_KEY
import alex.com.taskrsschool.common.logDebug
import alex.com.taskrsschool.data.room.Human
import alex.com.taskrsschool.databinding.FragmentHumansBinding
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HumansFragment : Fragment(R.layout.fragment_humans), HumansAdapter.OnItemClickListener {
    private var _binding: FragmentHumansBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HumansViewModel by viewModels()
    private val humansAdapter = HumansAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHumansBinding.bind(view)

        initViews()
        initEvents()
        initFragmentListener()
        setHasOptionsMenu(true)

        viewModel.humans.observe(viewLifecycleOwner) {
            humansAdapter.submitList(it)
            logDebug("HumansFragment: " + it.toString())
        }

    }

    private fun initFragmentListener() {
        setFragmentResultListener(EDITOR_RESULT_KEY) { _, bundle ->
            val result = bundle.getInt(EDITOR_RESULT_KEY)
            viewModel.onEditorResult(result)
        }
    }

    private fun initEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.humansEvent.collect { event ->
                    when (event) {
                        is HumansViewModel.HumansEvent.NavigateToAddHumanScreen -> {
                            val action =
                                HumansFragmentDirections.actionHumansFragmentToEditorFragment(
                                    getString(R.string.newHuman),
                                    null
                                )
                            findNavController().navigate(action)
                        }
                        is HumansViewModel.HumansEvent.NavigateToEditHumanScreen -> {
                            val action =
                                HumansFragmentDirections.actionHumansFragmentToEditorFragment(
                                    getString(R.string.editHuman),
                                    event.human
                                )
                            findNavController().navigate(action)
                        }
                        is HumansViewModel.HumansEvent.NavigateToSortSettingsFragment -> {
                            val action =
                                HumansFragmentDirections.actionGlobalSettingsFragment()
                            findNavController().navigate(action)
                        }
                        is HumansViewModel.HumansEvent.ShowHumanSavedConfirmationMessage -> {
                            Snackbar.make(
                                requireView(),
                                getString(event.message),
                                Snackbar.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun initViews() {
        binding.apply {
            humansRecycler.apply {

                adapter = humansAdapter

                setHasFixedSize(true)

                ItemTouchHelper(object :
                    ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    ) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val human = humansAdapter.currentList[viewHolder.adapterPosition]
                        viewModel.onHumanSwiped(human)
                    }
                }).attachToRecyclerView(this)
            }

            addHuman.setOnClickListener {
                viewModel.onAddNewHumanClick()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_humans, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter -> {
                viewModel.onFilterClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(human: Human) {
        viewModel.onHumanSelected(human)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}