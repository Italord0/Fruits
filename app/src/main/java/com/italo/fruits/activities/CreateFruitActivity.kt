package com.italo.fruits.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.italo.fruits.R
import com.italo.fruits.dao.FruitDao
import com.italo.fruits.database.FruitDatabase
import com.italo.fruits.databinding.ActivityCreateFruitBinding
import com.italo.fruits.model.Fruit
import java.io.*


class CreateFruitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateFruitBinding
    private val PICK_IMAGE = 1
    private lateinit var selectedImage: Bitmap
    var pictureSelected: Boolean = false
    private lateinit var dao: FruitDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateFruitBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.setTitle(R.string.create_a_fruit)

        dao = FruitDatabase.getInstance(this).fruitDao()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.fruit_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.register) {
            saveFruit()
        }
        return super.onOptionsItemSelected(item)
    }

    fun onSelectImage(view: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, getString(R.string.select_a_picture)),
            PICK_IMAGE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            val imageUri: Uri? = data?.data
            val imageStream: InputStream? = contentResolver.openInputStream(imageUri!!)
            selectedImage = BitmapFactory.decodeStream(imageStream)
            binding.ivFruit.setImageBitmap(selectedImage)
            pictureSelected = true
        } else {
            Toast.makeText(this, R.string.image_canceled, Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveFruit() {
        if (binding.tilFruitName.editText?.text.toString().isNotEmpty() &&
            binding.tilFruitBenefits.editText?.text.toString().isNotEmpty() && pictureSelected
        ) {
            if (dao.getByName(binding.tilFruitName.editText?.text.toString()) == 0) {
                val file =
                    File(getExternalFilesDir("fruit_images"), System.currentTimeMillis().toString())
                val fruit = Fruit(
                    0,
                    binding.tilFruitName.editText?.text.toString(),
                    binding.tilFruitBenefits.editText?.text.toString(),
                    file.absolutePath,
                    dao.order() + 1
                )
                println(fruit)
                val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, os)

                val intent = Intent()
                intent.putExtra(Fruit::class.java.simpleName, fruit)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                showExistsDialog()
            }
        } else {
            Toast.makeText(
                this,
                getString(R.string.please_fill_all_the_fields_correctly),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showExistsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.atention))
        builder.setMessage(getString(R.string.fruit_already_registered))

        builder.setPositiveButton(getString(R.string.ok)) { _, _ ->

        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}