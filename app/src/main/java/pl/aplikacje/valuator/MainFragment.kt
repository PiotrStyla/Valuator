package pl.aplikacje.valuator

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_main.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import pl.aplikacje.valuator.model.CarnetDetectResponse
import pl.aplikacje.valuator.network.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
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



    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                MainActivity.FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(MainActivity.TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    Log.d("onImageSaved", "Photo capture succeeded: $savedUri")
                    uploadImage(savedUri)
                }
            })
    }

    private fun uploadImage(imageUri: Uri) {
        val file = File(imageUri.path)

        // create RequestBody instance from file
        val requestFile = file.asRequestBody("image/*".toMediaType())

        val parameters = mutableMapOf(
            "box_min_width" to "180",
            "box_min_height" to "180",
            "box_min_ratio" to "1",
            "box_max_ratio" to "3.15",
            "box_select" to "center",
            "region" to "DEF"
        )

        val call = NetworkUtils.uploadService.upload(parameters.toMap(), requestFile)
        camera_capture_button.isEnabled = false

        showToast("Loading...")
        call.enqueue(object : Callback<CarnetDetectResponse> {
            override fun onResponse(
                call: Call<CarnetDetectResponse>,
                response: Response<CarnetDetectResponse>
            ) {
                camera_capture_button.isEnabled = true

                response.body()?.detections?.firstOrNull()?.let {
                    Log.d("detections:", it.mmg.first().modelName)
                    showToast("Sukces, Model: ${it.mmg.first().modelName}, Marka:${it.mmg.first().makeName}, Rok: ${it.mmg.first().years}")
                    //Web Search in new intent
                    val intent = Intent(Intent.ACTION_WEB_SEARCH)
                    val term = "otomoto/osobowe/  ${it.mmg.first().makeName}/ ${it.mmg.first().modelName}/ od ${it.mmg.first().years}"
                    intent.putExtra(SearchManager.QUERY, term)
                    startActivity(intent)
                }?: run {
                    showToast("Success, No detections found!")
                }
            }

            override fun onFailure(call: Call<CarnetDetectResponse>, throwable: Throwable?) {
                camera_capture_button.isEnabled = true
                Log.d("failure:", throwable.toString())
                showToast("failure: ${throwable.toString()}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainFragment, message, Toast.LENGTH_SHORT).show()
    }
}