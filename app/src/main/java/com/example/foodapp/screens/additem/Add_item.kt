package com.example.foodapp.screens.additem

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentAddItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import java.io.File

class Add_item : Fragment() {
    private lateinit var back:ImageView
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()
        back = view.findViewById(R.id.backbtn)
        binding.progressbar.visibility=View.GONE
        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data: Intent? = result.data
                val imageUri: Uri? = data?.data
                binding.uploadimg.setImageURI(imageUri)

                if (imageUri != null) {
                    binding.uploadimg.tag = imageUri
                }
            }
        }
        binding.addPhoto.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }
        back.setOnClickListener{
            findNavController().popBackStack()
        }
        binding.savechanges.setOnClickListener {
            val itemName = binding.edititemname.text.toString().trim()
            val price = binding.edittextprice.text.toString().trim()
            val details = binding.edittextdetails.text.toString().trim()

            if (itemName.isEmpty() || price.isEmpty() || details.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val imageUri = binding.uploadimg.tag as? Uri
            if (imageUri == null) {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.progressbar.visibility = View.VISIBLE
            uploadImageToPostImages(imageUri) { directUrl ->
                if (directUrl != null) {
                    val item = hashMapOf(
                        "name" to itemName,
                        "price" to price,
                        "details" to details,
                        "imageUrl" to directUrl,
                        "timestamp" to System.currentTimeMillis()
                    )
                    db.collection("items")
                        .add(item)
                        .addOnSuccessListener {
                            binding.progressbar.visibility = View.GONE
                            Toast.makeText(requireContext(), "Item saved!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_add_item2_to_menu)
                        }
                        .addOnFailureListener { e ->
                            binding.progressbar.visibility = View.GONE
                            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadImageToPostImages(uri: Uri, onUploaded: (String?) -> Unit) {
        val file = File(requireContext().cacheDir, "upload.jpg")

        requireContext().contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        val apiKey = "4c07f6c117d49c18532bd1f8043af88e"
            .toRequestBody("text/plain".toMediaTypeOrNull())
        RetrofitClient.api.uploadImage(apiKey, body)
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: retrofit2.Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val json = response.body()?.string()
                        try {
                            val jsonObj = JSONObject(json!!)
                            val directUrl = jsonObj
                                .getJSONObject("data")
                                .getString("url")
                            onUploaded(directUrl)
                        } catch (e: Exception) {
                            onUploaded(null)
                        }
                    } else {
                        onUploaded(null)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    onUploaded(null)
                }
            })
    }

}