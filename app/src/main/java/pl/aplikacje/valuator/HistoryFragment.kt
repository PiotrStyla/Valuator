package pl.aplikacje.valuator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.aplikacje.valuator.databinding.FragmentHistoryBinding
import pl.aplikacje.valuator.recyclerview.ItemListAdapter
import pl.aplikacje.valuator.viewmodel.AppViewModel


class HistoryFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var appViewModel: AppViewModel

    lateinit var navController: NavController
    lateinit var adapter: ItemListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.buttonSettings.setOnClickListener(this)
        binding.cameraNewCaptureButton.setOnClickListener(this)

        adapter = ItemListAdapter(requireContext()) //room_trial_2 I delivered a new adapter which was empty instead odf the global one: val adapter = ...
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                RecyclerView.VERTICAL
            )
        )

        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        setupObservers()  //room_trial_2 introduced here in this version
                            // to observe allCarData changes in appViewModel

        appViewModel.getAllCarRecords()
    }

    private fun setupObservers() {
        appViewModel.allCarData.observe(viewLifecycleOwner, { items ->
            items?.let {
                adapter.setItems(it)
            }
        })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.camera_new_capture_button -> navController.navigate(R.id.action_historyFragment_to_mainFragment)
            R.id.button_settings -> navController.navigate(R.id.action_historyFragment_to_settingsFragment)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

