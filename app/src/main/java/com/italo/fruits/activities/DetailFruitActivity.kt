package com.italo.fruits.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.italo.fruits.R
import com.italo.fruits.databinding.ActivityDetailFruitBinding
import com.italo.fruits.model.Fruit
import java.io.File

class DetailFruitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailFruitBinding
    private lateinit var fruit: Fruit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFruitBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.setTitle(R.string.fruit_details)
        fruit = intent.getParcelableExtra(Fruit::class.java.simpleName)!!
        setupFruit(fruit)
    }

    fun onRemoveFruit(view: View) {
        showDeleteDialog()
    }

    private fun setupFruit(fruit: Fruit?) {
        Glide.with(this).load(File(fruit?.image!!)).into(binding.ivFruit)
        binding.tvFruitName.text = fruit.name
        binding.tvFruitBenefits.text = fruit.benefits
    }

    private fun showDeleteDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.atention))
        builder.setMessage(getString(R.string.would_you_like_to_remove_the_fruit))

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            val intent = Intent()
            intent.putExtra(Fruit::class.java.simpleName, fruit)
            setResult(RESULT_OK, intent)
            finish()
        }

        builder.setNegativeButton(getString(R.string.no)) { _, _ ->

        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}