package pl.aplikacje.valuator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_main.*
import pl.aplikacje.valuator.databinding.FragmentMainBinding as FragmentMainBinding1


class MainFragment : Fragment(), View.OnClickListener {
    private var _binding : FragmentMainBinding1? = null
            private val binding get() = _binding!!



    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding1.inflate(inflater,container,false)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.buttonHistory.setOnClickListener(this)
        binding.buttonSettings.setOnClickListener(this)
        binding.cameraCaptureButton.setOnClickListener(this) 
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.button_history -> navController!!.navigate(R.id.action_mainFragment_to_historyFragment)
            R.id.button_settings -> navController!!.navigate(R.id.action_mainFragment_to_settingsFragment)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}