package pl.aplikacje.valuator

import android.Manifest
import android.app.SearchManager
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraXThreads.TAG
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_main.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import pl.aplikacje.valuator.MainFragment.Companion.REQUIRED_PERMISSIONS
import pl.aplikacje.valuator.MainFragment.Companion.TAG
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
import pl.aplikacje.valuator.databinding.FragmentMainBinding as FragmentMainBinding1


class MainFragment : Fragment(), View.OnClickListener {
    private var _binding : FragmentMainBinding1? = null
            private val binding get() = _binding!!


    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null



    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding1.inflate(inflater,container,false)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, MainFragment.REQUIRED_PERMISSIONS, MainFragment.REQUEST_CODE_PERMISSIONS
            )
        }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()

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
                MainFragment.FILENAME_FORMAT, Locale.US
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
                    Log.e(MainFragment.TAG, "Photo capture failed: ${exc.message}", exc)
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

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

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

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = MainFragment.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
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
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}