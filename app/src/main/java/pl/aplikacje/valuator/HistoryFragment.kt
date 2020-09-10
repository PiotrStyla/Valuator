package pl.aplikacje.valuator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_value_page.*
import pl.aplikacje.valuator.database.CarPhotoInDatabase
import pl.aplikacje.valuator.databinding.FragmentHistoryBinding
import pl.aplikacje.valuator.databinding.FragmentMainBinding
import pl.aplikacje.valuator.recyclerview.ItemListAdapter
import pl.aplikacje.valuator.viewmodel.AppViewModel


class HistoryFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    lateinit var appViewModel: AppViewModel

    lateinit var navController: NavController
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ItemListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        binding.recyclerView


        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.buttonSettings.setOnClickListener(this)
        binding.cameraNewCaptureButton.setOnClickListener(this)

        R.id.recycler_view
        val adapter = ItemListAdapter(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        appViewModel.allPositions.observe(viewLifecycleOwner,
            Observer { items -> items?.let {adapter.setItems(it)} })

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

