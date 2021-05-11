package com.italo.fruits.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.italo.fruits.adapter.FruitAdapter
import com.italo.fruits.databinding.ActivityMainBinding
import com.italo.fruits.model.Fruit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var fruits = listOf(
        Fruit("Fruit 1", "Some benefits here", ""),
        Fruit("Fruit 2", "Some benefits here", "")
    )

    private val mFruitAdapter = FruitAdapter(this, fruits, this::onFruitClickListener)

    private fun onFruitClickListener(fruit: Fruit) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupRecyclerview()
    }

    private fun setupRecyclerview() {
        binding.rvFruits.adapter = mFruitAdapter
        val layoutManager = GridLayoutManager(this, 1)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }
        binding.rvFruits.layoutManager = layoutManager
    }

    fun onAddFruit(view: View) {

    }
}