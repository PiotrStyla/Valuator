package pl.aplikacje.valuator

import android.Manifest
import android.app.SearchManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_value_page.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import org.jetbrains.anko.uiThread
import pl.aplikacje.valuator.database.AppDatabase
import pl.aplikacje.valuator.model.CarnetDetectResponse
import pl.aplikacje.valuator.network.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import pl.aplikacje.valuator.databinding.FragmentMainBinding
import pl.aplikacje.valuator.recyclerview.ItemListAdapter
import pl.aplikacje.valuator.viewmodel.AppViewModel
import kotlin.toString as toString1

// import org.jetbrains.anko.doAsync

class MainFragment : Fragment(), View.OnClickListener {
    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!


    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null

    lateinit var navController: NavController

//    private lateinit var appViewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)


        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(
                REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        binding.cameraCaptureButton.setOnClickListener { takePhoto() }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.buttonHistory.setOnClickListener(this)
        binding.buttonSettings.setOnClickListener(this)
        binding.cameraCaptureButton.setOnClickListener { takePhoto()}
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.button_history -> navController.navigate(R.id.action_mainFragment_to_historyFragment)
            R.id.button_settings -> navController.navigate(R.id.action_mainFragment_to_settingsFragment)


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
                MainFragment.FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    Log.d("onImageSaved", "Photo capture suceeded: $savedUri")
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

                    //Save to the db


                    //Web Search in new intent
                    val intent = Intent(Intent.ACTION_WEB_SEARCH)
                    val term =
                        "otomoto/osobowe/  ${it.mmg.first().makeName}/ ${it.mmg.first().modelName}/ od ${it.mmg.first().years}"
                    intent.putExtra(SearchManager.QUERY, term)
                    startActivity(intent)
                } ?: run {
                    showToast("Success, No detections found!")
                }
            }

            override fun onFailure(call: Call<CarnetDetectResponse>, throwable: Throwable?) {
                camera_capture_button.isEnabled = true
                Log.d("failure:", throwable.toString1())
                showToast("failure: ${throwable.toString1()}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                }

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(MainFragment.TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getOutputDirectory(): File {
        val mediaDir = requireContext().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireContext().filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    context,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                onStop()  // finish()
            }
        }
    }
}