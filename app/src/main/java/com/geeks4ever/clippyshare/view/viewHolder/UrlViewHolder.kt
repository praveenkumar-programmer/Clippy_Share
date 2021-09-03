package com.geeks4ever.clippyshare.view.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.geeks4ever.clippyshare.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

class UrlViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var urlText: MaterialTextView = itemView.findViewById(R.id.url_text)
    val dateAndTimeText : MaterialTextView = itemView.findViewById(R.id.date_and_time_text)
    val root: MaterialCardView = itemView.findViewById(R.id.url_item_root)

}