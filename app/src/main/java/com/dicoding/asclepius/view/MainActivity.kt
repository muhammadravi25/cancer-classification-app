package com.dicoding.asclepius.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_form, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_menu_history -> {
                val moveToHistoryPredictionIntent = Intent(this@MainActivity, HistoryPredictionActivity::class.java)
                startActivity(moveToHistoryPredictionIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        currentImageUri?.let {
            startUCrop(it)
        }
    }

    private fun startUCrop(uri: Uri) {
        val destinationFileName = "${System.currentTimeMillis()}.jpg"
        val destinationDir = File(cacheDir, "cropped_images")
        if (!destinationDir.exists()) {
            destinationDir.mkdirs()
        }
        val destinationFile = File(destinationDir, destinationFileName)
        val destinationUri = Uri.fromFile(destinationFile)

        val options = UCrop.Options()
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        options.setCompressionQuality(80)

        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withOptions(options)
            .start(this, UCrop.REQUEST_CROP)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            currentImageUri = resultUri
            showImage()

            imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        runOnUiThread {
                            showToast(error.toString())
                        }
                    }

                    override fun onResults(result: List<Classifications>?) {
                        runOnUiThread {
                            result?.let {it ->
                                if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                    println(it)
                                    val sortedCategories = it[0].categories.sortedByDescending { it?.score }
                                    val highestPrediction = sortedCategories.firstOrNull()
                                    highestPrediction?.let { category ->
                                        val label = category.label
                                        val score = category.score
                                        val displayResult = "$label " + NumberFormat.getPercentInstance().format(score).trim()
                                        showToast(displayResult)
                                        moveToResult(score, label, currentImageUri)
                                    }
                                }

                            }
                        }
                    }
                }
            )
            currentImageUri?.let { imageClassifierHelper.classifyStaticImage(it) }
        }
    }

    private fun moveToResult(score: Float, label: String, currentImageUri: Uri?) {
        if(currentImageUri !== null) {
            val intent = Intent(this@MainActivity, ResultActivity::class.java)
            intent.putExtra(ResultActivity.CONFIDENCESCORE,
                NumberFormat.getPercentInstance().format(score).trim()
            )
            intent.putExtra(ResultActivity.LABEL_PREDICT, label)
            intent.putExtra(ResultActivity.IMAGE_PREDICT, currentImageUri)
            startActivity(intent)
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            showToast(getString(R.string.no_media_selected))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}