package com.example.recetasapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class RecipeAdapter(private val context: Context, private val recipes: List<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_card, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetallesRecetaActivity::class.java).apply {
                putExtra("recipe_id", recipe.id)
                putExtra("recipe_image", recipe.image)
                putExtra("recipe_title", recipe.title)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = recipes.size

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ShapeableImageView = itemView.findViewById(R.id.recipe_image)
        private val textView: TextView = itemView.findViewById(R.id.recipe_title)
        private val rectangleView: View = itemView.findViewById(R.id.rectangle_view)

        fun bind(recipe: Recipe) {
            textView.text = recipe.title
            Picasso.get().load(recipe.image).into(imageView)
            rectangleView.setBackgroundColor(Color.parseColor("#B0F0A5"))
        }
    }
}
