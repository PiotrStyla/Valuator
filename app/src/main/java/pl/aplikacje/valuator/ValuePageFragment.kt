package pl.aplikacje.valuator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import pl.aplikacje.valuator.databinding.FragmentSettingsBinding
import pl.aplikacje.valuator.databinding.FragmentValuePageBinding


class ValuePageFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentValuePageBinding? = null
    private val binding get() = _binding!!

    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentValuePageBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_value_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.buttonSettings.setOnClickListener(this)
        binding.cameraNewCaptureButton.setOnClickListener(this)
        binding.buttonHistory.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.camera_new_capture_button -> navController!!.navigate(R.id.action_valuePageFragment_to_mainFragment)
            R.id.button_settings -> navController!!.navigate(R.id.action_valuePageFragment_to_settingsFragment)
            R.id.button_history-> navController!!.navigate(R.id.action_valuePageFragment_to_historyFragment)

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
