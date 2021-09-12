package com.mobline.presentation.flow.main.content.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobline.domain.model.Recipe
import com.mobline.marleyspoon.presentation.R
import com.mobline.marleyspoon.presentation.databinding.RecipeItemBinding
import java.util.*

class RecipeAdapter(
    private val dataList: ArrayList<Recipe>?,
    private val onItemClicked: (recipe: Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecipeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: kotlin.run { 0 }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataList?.get(position)?.let { item ->
            holder.loadData(item)

            with(holder.binding) {
                this.root.setOnClickListener { onItemClicked(item) }
            }
        }
    }

    class ViewHolder(
        val binding: RecipeItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun loadData(recipe: Recipe?) = with(binding) {
            tvTitle.text = recipe?.title
            Glide.with(imageView)
                .load(recipe?.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_image_place_holder)
                .into(imageView);
        }
    }

    fun setData(records: List<Recipe>) {
        dataList?.clear()
        dataList?.addAll(records)
        notifyDataSetChanged()
    }
}
