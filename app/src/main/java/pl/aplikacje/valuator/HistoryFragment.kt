package pl.aplikacje.valuator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation


class HistoryFragment : Fragment(), View.OnClickListener {

    lateinit var navController : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.button_settings).setOnClickListener(this)
        view.findViewById<Button>(R.id.camera_new_capture_button).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.camera_new_capture_button -> navController!!.navigate(R.id.action_historyFragment_to_mainFragment)
            R.id.button_settings -> navController!!.navigate(R.id.action_historyFragment_to_settingsFragment)

        }
    }
}

