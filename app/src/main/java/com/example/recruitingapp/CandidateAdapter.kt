package com.example.recruitingapp

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CandidateAdapter(options: FirebaseRecyclerOptions<Candidate>, private val defaultuser: String):
    FirebaseRecyclerAdapter<Candidate, CandidateAdapter.MyViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Candidate) {
        if (model.photo.isNotEmpty()) {
            val storRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.photo)
            Glide.with(holder.candidateImageView.context).load(storRef).into(holder.candidateImageView)
        }

        holder.nameTextView.text = model.name
        holder.titleTextView.text = model.title
        var candidateId:String?=null;

        holder.candidateImageView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CandidateDetailActivity::class.java)
            candidateId=snapshots.getSnapshot(position).key
            intent.putExtra("name", model.name)
            intent.putExtra("title", model.title)
            intent.putExtra("description", model.description)
            intent.putExtra("photo",model.photo)
            intent.putExtra("defaultuser", defaultuser)
            Log.e(ContentValues.TAG, "sent logged default candidate ID to candidate detail activity is : ${defaultuser}")
            intent.putExtra("candidateId", candidateId)
            holder.itemView.context.startActivity(intent)
        }
    }


    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.candidate_row_layout, parent, false)) {

        val nameTextView: TextView = itemView.findViewById(R.id.txtName)
        val titleTextView: TextView = itemView.findViewById(R.id.txtTitle)
        val candidateImageView: ImageView = itemView.findViewById(R.id.imgPhoto)

    }

}