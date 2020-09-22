package pl.aplikacje.valuator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import pl.aplikacje.valuator.databinding.FragmentValuePageBinding
import pl.aplikacje.valuator.viewmodel.AppViewModel


class ValuePageFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentValuePageBinding? = null
    private val binding get() = _binding!!

    lateinit var navController: NavController

    private lateinit var appViewModel: AppViewModel
    private val args: ValuePageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentValuePageBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.buttonSettings.setOnClickListener(this)
        binding.cameraNewCaptureButton.setOnClickListener(this)
        binding.buttonHistory.setOnClickListener(this)

        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        setupObservers()

        appViewModel.getCarDataById(args.recordId)
    }

    private fun setupObservers() {
        appViewModel.latestCarData.observe(viewLifecycleOwner, { item ->
            item?.let {
                binding.textView.text = it.make_name
            }
        })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.camera_new_capture_button -> navController.navigate(R.id.action_valuePageFragment_to_mainFragment)
            R.id.button_settings -> navController.navigate(R.id.action_valuePageFragment_to_settingsFragment)
            R.id.button_history -> navController.navigate(R.id.action_valuePageFragment_to_historyFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


