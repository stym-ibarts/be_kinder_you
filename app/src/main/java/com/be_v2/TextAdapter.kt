package com.be_v2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections
import kotlin.random.Random

class TextAdapterMain(private val data: Array<String>) :
    RecyclerView.Adapter<TextAdapterMain.TextViewHolder>() {

//     var data1 = Collections.shuffle(data.toMutableList())


    class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.quote_item, parent, false)
        return TextViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        val randomQuoteIndex = Random.nextInt(0, itemCount)
        val affirmation = data[randomQuoteIndex]
        holder.textView.text = affirmation
    }

    override fun getItemCount(): Int {
        return data.size
    }
}