package com.italo.fruits.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.italo.fruits.R
import com.italo.fruits.adapter.FruitAdapter
import com.italo.fruits.databinding.ActivityMainBinding
import com.italo.fruits.model.Fruit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var fruits = mutableListOf<Fruit>()

    private val mFruitAdapter = FruitAdapter(this, fruits, this::onFruitClickListener)

    private fun onFruitClickListener(fruit: Fruit) {
        val intent = Intent(this,DetailFruitActivity::class.java)
        intent.putExtra(Fruit::class.java.simpleName,fruit)
        startActivityForResult(intent,2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupRecyclerview()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK){
            val fruit = data?.getParcelableExtra<Fruit>(Fruit::class.java.simpleName)
            fruits.add(fruit!!)
            println(fruits.size)
            mFruitAdapter.notifyDataSetChanged()
        } else if (requestCode == 2) {
            val fruit = data?.getParcelableExtra<Fruit>(Fruit::class.java.simpleName)
            fruits.remove(fruit)
            mFruitAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(this, getString(R.string.operation_canceled), Toast.LENGTH_SHORT).show()
        }
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
        startActivityForResult(Intent(this,CreateFruitActivity::class.java),1)
    }
}