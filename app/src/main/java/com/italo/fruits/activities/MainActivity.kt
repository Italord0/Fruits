package com.italo.fruits.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.italo.fruits.adapter.FruitAdapter
import com.italo.fruits.dao.FruitDao
import com.italo.fruits.database.FruitDatabase
import com.italo.fruits.databinding.ActivityMainBinding
import com.italo.fruits.model.Fruit
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: FruitDao

    lateinit var fruits: MutableList<Fruit>

    lateinit var mFruitAdapter: FruitAdapter

    private fun onFruitClickListener(fruit: Fruit) {
        val intent = Intent(this, DetailFruitActivity::class.java)
        intent.putExtra(Fruit::class.java.simpleName, fruit)
        startActivityForResult(intent, 2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dao = FruitDatabase.getInstance(this).fruitDao()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.animationView.setAnimation("animations/watermelon.json")
        fruits = dao.getAll().toMutableList()

        mFruitAdapter = FruitAdapter(this, fruits, this::onFruitClickListener)

        setupRecyclerview()
    }

    override fun onResume() {
        super.onResume()

        if (fruits.isEmpty()) {
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.emptyView.visibility = View.GONE
        }
    }

    private fun fetchResults() {
        fruits.clear()
        fruits.addAll(dao.getAll().toMutableList())
        mFruitAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val fruit = data?.getParcelableExtra<Fruit>(Fruit::class.java.simpleName)
            dao.insert(fruit!!)
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            val fruit = data?.getParcelableExtra<Fruit>(Fruit::class.java.simpleName)
            //DELETING IMAGE FILE FROM PRIVATE STORAGE
            File(fruit?.image!!).delete()
            dao.delete(fruit)
        }
        fetchResults()
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
        startActivityForResult(Intent(this, CreateFruitActivity::class.java), 1)
    }
}