package com.italo.fruits.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.italo.fruits.R
import com.italo.fruits.databinding.ItemFruitBinding
import com.italo.fruits.model.Fruit
import java.io.File

class FruitAdapter(private val context: Context,
                    private val fruits: List<Fruit>,
                    private val callback: (Fruit) -> Unit) : RecyclerView.Adapter<FruitAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fruit, parent, false)
        val binding = ItemFruitBinding.bind(view)
        val vh = ViewHolder(binding)
        vh.itemView.setOnClickListener {
            val fruit = fruits[vh.adapterPosition]
            callback(fruit)
        }
        return vh
    }

    override fun getItemCount() = fruits.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (name, benefits , image) = fruits[position]
        //TODO : COLOCAR IMAGEM DA FRUTA PASSANDO O PATH DA IMAGEM
        holder.tvFruitName.text = name
        holder.tvFruitBenefits.text = benefits

        Glide.with(context).load(File(image!!)).into(holder.ivFruit);

    }

    class ViewHolder(itemView: ItemFruitBinding): RecyclerView.ViewHolder(itemView.root) {
        val ivFruit : ImageView = itemView.ivFruit
        val tvFruitName: TextView = itemView.tvFruitName
        val tvFruitBenefits: TextView = itemView.tvFruitBenefits
    }
}