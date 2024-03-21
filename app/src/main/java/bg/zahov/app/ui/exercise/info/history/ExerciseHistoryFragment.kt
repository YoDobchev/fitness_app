package bg.zahov.app.ui.exercise.info.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import androidx.recyclerview.widget.LinearLayoutManager
import bg.zahov.app.data.model.state.ExerciseHistoryUiMapper
import bg.zahov.app.hideBottomNav
import bg.zahov.fitness.app.databinding.FragmentExerciseHistoryBinding

class ExerciseHistoryFragment : Fragment() {
    private var _binding: FragmentExerciseHistoryBinding? = null
    private val binding
        get() = requireNotNull(_binding)
    private val historyInfoViewModel: ExerciseHistoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentExerciseHistoryBinding.inflate(inflater, container, false)
        requireActivity().hideBottomNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val setAdapter = ExerciseHistoryAdapter()
            historyInfoViewModel.state.map { ExerciseHistoryUiMapper.map(it) }
                .observe(viewLifecycleOwner) {
                    circularProgressIndicator.visibility = it.loadingVisibility
                    setsRecyclerView.visibility = it.recyclerViewVisibility
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    if (it.shutdown) {
                    } //TODO()
                    setAdapter.updateItems(it.data)
                    it.data
                }
            setsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = setAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}