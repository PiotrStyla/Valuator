package pl.aplikacje.valuator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_value_page.*
import pl.aplikacje.valuator.databinding.FragmentSettingsBinding
import pl.aplikacje.valuator.databinding.FragmentValuePageBinding
import pl.aplikacje.valuator.repository.AppReository
import pl.aplikacje.valuator.viewmodel.AppViewModel


class ValuePageFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentValuePageBinding? = null
    private val binding get() = _binding!!

    lateinit var navController: NavController

    private lateinit var appViewModel: AppViewModel

    val id = appViewModel.id


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentValuePageBinding.inflate(inflater,container,false)
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
        appViewModel.latestPosition

        binding.textView.setText(appViewModel.latestPosition)
        binding.textView.setText(id)


//        appViewModel.latestPosition.observe(viewLifecycleOwner,
//            Observer { items -> items?.let { textView.setText(id) } })




        //binding.imageView.setImageResource(id)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.camera_new_capture_button -> navController.navigate(R.id.action_valuePageFragment_to_mainFragment)
            R.id.button_settings -> navController.navigate(R.id.action_valuePageFragment_to_settingsFragment)
            R.id.button_history-> navController.navigate(R.id.action_valuePageFragment_to_historyFragment)

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
