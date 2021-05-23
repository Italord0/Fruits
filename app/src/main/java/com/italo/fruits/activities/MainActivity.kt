package com.italo.fruits.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.italo.fruits.R
import com.italo.fruits.adapter.FruitAdapter
import com.italo.fruits.callback.FruitRecyclerViewCallback
import com.italo.fruits.dao.FruitDao
import com.italo.fruits.database.FruitDatabase
import com.italo.fruits.databinding.ActivityMainBinding
import com.italo.fruits.helper.FruitItemTouchHelperCallback
import com.italo.fruits.model.Fruit
import java.io.File
import java.util.*

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

        println("ORDER = " + dao.order())
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
        }
        fetchResults()
    }

    private fun setupRecyclerview() {
        binding.rvFruits.adapter = mFruitAdapter
        val layoutManager = LinearLayoutManager(this)
        binding.rvFruits.layoutManager = layoutManager

        val fruitTouchHelper = ItemTouchHelper(FruitItemTouchHelperCallback(this,object : FruitRecyclerViewCallback {
            override fun onSwipe(position: Int, view: RecyclerView.ViewHolder) {

                println("Swiping at position $position")
                val removedFruit = fruits[position]
                showDeleteDialog(removedFruit,position,view)

            }
            override fun onMoved(initPosition: Int, targetPosition: Int) {
                println("Moving from position $initPosition to position $targetPosition")
                val fruitMoved = fruits[initPosition]
                val fruitReplaced = fruits[targetPosition]

                fruitMoved.order = targetPosition + 1

                fruitReplaced.order = initPosition + 1

                dao.update(fruitMoved)
                dao.update(fruitReplaced)

                Collections.swap(fruits, initPosition, targetPosition);
                mFruitAdapter.notifyItemMoved(initPosition, targetPosition);
            }
        }
        ))

        fruitTouchHelper.attachToRecyclerView(binding.rvFruits)

    }

    fun onAddFruit(view: View) {
        startActivityForResult(Intent(this, CreateFruitActivity::class.java), 1)
    }

    fun checkEmptyList(){
        if (fruits.isEmpty()) {
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.emptyView.visibility = View.GONE
        }
    }

    private fun showDeleteDialog(fruit: Fruit , position: Int ,view: RecyclerView.ViewHolder) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.atention))
        builder.setMessage(getString(R.string.would_you_like_to_remove_the_fruit))

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            File(fruit.image!!).delete()
            dao.delete(fruit)
            fruits.removeAt(position)
            mFruitAdapter.notifyItemRemoved(position)
            checkEmptyList()

            Snackbar.make(view.itemView, R.string.fruit_deleted, Snackbar.LENGTH_LONG).setAction(R.string.undo){
                fruits.add(position,fruit)
                dao.insert(fruit)
                mFruitAdapter.notifyItemInserted(position)
                checkEmptyList()
            }.show()
        }

        builder.setNegativeButton(getString(R.string.no)) { _, _ ->
            mFruitAdapter.notifyItemChanged(view.adapterPosition);
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}