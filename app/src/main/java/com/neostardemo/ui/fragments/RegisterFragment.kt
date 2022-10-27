package com.neostardemo.ui.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.neostardemo.R
import com.neostardemo.databinding.FragmentRegisterBinding
import com.neostardemo.utils.AppConstants
import com.neostardemo.utils.DialogCallbackCameraGallery
import com.neostardemo.utils.getRealPathFromUri
import com.neostardemo.utils.showCameraGalleryDialog
import com.neostardemo.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var imageUri: Uri
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    var permissionsStr = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()) {/*take from camera*/
        with(binding) {
            ivUser.setImageURI(null)
            viewModel.imageUri.set(imageUri.toString())
            ivUser.setImageURI(imageUri)
        }

    }
    private val galleryContract = registerForActivityResult(ActivityResultContracts.GetContent()) { /*pick from gallery*/
        with(binding) {
            val uri = requireContext().getRealPathFromUri(it!!)
            viewModel.imageUri.set(uri)
            ivUser.setImageURI(it)
        }

    }


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val list: ArrayList<Boolean> = ArrayList(result!!.values)
            val permissionsList = ArrayList<String>()
            var permissionsCount = 0
            for (i in 0 until list.size) {
                if (shouldShowRequestPermissionRationale(permissionsStr.get(i))) {
                    permissionsList.add(permissionsStr[i])
                } else if (!hasPermission(requireContext(), permissionsStr.get(i))) {
                    permissionsCount++
                }
            }
            if (permissionsList.size > 0) {
                //Some permissions are denied and can be asked again.
                showSnackBarMsg()
            } else if (permissionsCount > 0) {
                //Show alert dialog
                showSnackBarMsg()
            } else {
                showCameraGalleryDialog()
                //All permissions granted. Do your stuff 🤞
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.mainViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createImageUri()
        setupViews()
        setupObservers()
        clickEvents()

    }

    private fun createImageUri() { //creating image uri
        val image = File(requireContext().applicationContext.filesDir, "my_photos.png")
        imageUri = FileProvider.getUriForFile(
            requireContext().applicationContext,
            "${requireContext().packageName}.utils.provider",
            image
        )
    }

    private fun setupViews() {
        binding.toolbar.tvTitle.text = getString(R.string.register)
    }

    private fun clickEvents() {
        with(binding) {
            rbMale.setOnClickListener {
                viewModel.gender.set(AppConstants.MALE)
            }
            rbFemale.setOnClickListener {
                viewModel.gender.set(AppConstants.FEMALE)
            }
            toolbar.ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            ivEdit.setOnClickListener {
                val  permissionsList = ArrayList<String>()
                var  permissionsCount = 0
                for (i in permissionsStr.indices) {
                    if (shouldShowRequestPermissionRationale(permissionsStr[i])) {
                        permissionsList.add(permissionsStr[i])
                    } else if (!hasPermission(requireContext(), permissionsStr[i])) {
                        permissionsCount++
                    }
                }
                when {
                    permissionsCount==permissionsList.size-> {
                        // You can use the API that requires the permission.
                        showCameraGalleryDialog()
                    }

                    permissionsList.size > 0 -> {
                        showSnackBarMsg()
                    }
                    else -> {
                        // The registered ActivityResultCallback gets the result of this request
                        requestPermissionLauncher.launch(permissionsStr)
                    }


                }
            }

        }
    }

    private fun showSnackBarMsg() {
        Snackbar.make(binding.rlImage, "Some Permission Denied.", Snackbar.LENGTH_LONG)
            .setAction("Settings") {
                // Responds to click on the action
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.data =
                    Uri.fromParts("package", requireContext().packageName, null)
                startActivity(intent)
            }.show()
    }

    private fun setupObservers() {
        viewModel.errorChannel.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.asString(requireContext()), Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun showCameraGalleryDialog() {
        requireContext().showCameraGalleryDialog(object : DialogCallbackCameraGallery {
            override fun onYes(from: String) {
                if (from == AppConstants.GALLERY) {
                    galleryContract.launch("image/*")

                } else {
                    contract.launch(imageUri)
                }
            }
        })
    }

    private fun hasPermission(context: Context, permissionStr: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permissionStr
        ) == PackageManager.PERMISSION_GRANTED
    }

}


