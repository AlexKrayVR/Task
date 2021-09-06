package alex.com.taskrsschool.ui.editor

import alex.com.taskrsschool.R
import alex.com.taskrsschool.common.EDITOR_RESULT_KEY
import alex.com.taskrsschool.common.DEFAULT_HUMAN_CARD_COLOR
import alex.com.taskrsschool.databinding.FragmentEditorBinding
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import yuku.ambilwarna.AmbilWarnaDialog

@AndroidEntryPoint
class EditorFragment : Fragment(R.layout.fragment_editor) {
    private var _binding: FragmentEditorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditorViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditorBinding.bind(view)
        initViews()
        initEvent()
    }

    private fun initViews() {
        binding.apply {
            name.setText(viewModel.humanName)
            age.setText(viewModel.humanAge)
            profession.setText(viewModel.humanProfession)

            colorPicker.backgroundTintList =
                ColorStateList.valueOf(
                    if (viewModel.humanCardColor == DEFAULT_HUMAN_CARD_COLOR) {
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.teal_200
                        )
                    } else
                        viewModel.humanCardColor
                )

            colorPicker.setOnClickListener {
                openColorPicker()
            }

            name.addTextChangedListener {
                viewModel.humanName = it.toString()
            }
            age.addTextChangedListener {
                viewModel.humanAge = it.toString()
            }

            profession.addTextChangedListener {
                viewModel.humanProfession = it.toString()
            }

            saveHuman.setOnClickListener {
                viewModel.onSavedClick()
            }
        }
    }

    private fun initEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.editorEvent.collect { event ->
                    when (event) {
                        is EditorViewModel.EditorEvent.ShowInvalidInputMessage -> {
                            Snackbar.make(
                                requireView(),
                                event.message,
                                Snackbar.LENGTH_LONG
                            )
                                .show()
                        }
                        is EditorViewModel.EditorEvent.NavigateBackWithResult -> {
                            setFragmentResult(
                                EDITOR_RESULT_KEY,
                                bundleOf(EDITOR_RESULT_KEY to event.result)
                            )
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    private fun openColorPicker() {
        val colorPicker = AmbilWarnaDialog(
            requireContext(),
            ContextCompat.getColor(
                requireContext(),
                R.color.teal_200
            ),
            false,
            object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog?) {
                }

                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    binding.colorPicker.backgroundTintList =
                        ColorStateList.valueOf(color)
                    viewModel.humanCardColor = color
                }
            })
        colorPicker.show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}