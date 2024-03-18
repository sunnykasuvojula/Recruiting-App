/*
* https://chat.openai.com/c/00d0d13a-f509-4370-a9da-1502ec0296a4*/
package com.example.recruitingapp

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class CandidateActivity:AppCompatActivity() {

    private var adapter: CandidateAdapter? = null;
    private var defaultuser: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.candidate_layout)

        defaultuser = intent.getStringExtra("defaultuser")?:""
        Log.e(ContentValues.TAG, "received logged default candidate ID from login activity is : ${defaultuser}")


        val query=FirebaseDatabase.getInstance().reference.child("candidates")
        val options=FirebaseRecyclerOptions.Builder<Candidate>().setQuery(query,Candidate::class.java).build()
        adapter= CandidateAdapter(options,defaultuser?:"")
        val rView:RecyclerView=findViewById(R.id.rView)
        rView.layoutManager=LinearLayoutManager(this)
        rView.adapter=adapter
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }


}