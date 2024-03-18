/*
* Reference for Glide :https://chat.openai.com/c/24597f04-3946-4858-ac85-8b0663f3bdff
* Reference for user Connections : https://chat.openai.com/c/06bbc086-b07d-4306-ba0f-7ca66a9522b5
* https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application*/
package com.example.recruitingapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CandidateDetailActivity:AppCompatActivity() {
    private lateinit var candidateId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.candidate_detail_layout)

        val name = intent.getStringExtra("name")
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val photo = intent.getStringExtra("photo")
        val connectNowButton=findViewById<Button>(R.id.connectBtn)
        candidateId = intent.getStringExtra("candidateId") ?: ""
        val defaultCandidateId = intent.getStringExtra("defaultuser")
        Log.e(TAG, "Default candidate Id is : ${defaultCandidateId}")
        Log.e(TAG, "Cliked candidate Id is : ${candidateId}")

        findViewById<TextView>(R.id.txtName).text=name;
        findViewById<TextView>(R.id.txtTitle).text=title;
        findViewById<TextView>(R.id.txtDescription).text=description;
        if (!photo.isNullOrEmpty()) {
            Glide.with(this).load(photo).into(findViewById(R.id.imgPhoto))
        }

        connectNowButton.setOnClickListener {
            val candidateName = name ?: return@setOnClickListener
            val connectionsRef = FirebaseDatabase.getInstance().reference.child("connections")

            defaultCandidateId?.let { uid ->
                connectionsRef.child(uid).child(candidateName).get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
//                        connectNowButton.text = "Connected"
                        Toast.makeText(this, "Already connected", Toast.LENGTH_SHORT).show()

                    } else {
                        connectionsRef.child(uid).child(candidateName).setValue(true)
//                        connectNowButton.text = "not Connected"
                        Toast.makeText(this, "You made a connection", Toast.LENGTH_SHORT).show()
                    }

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("candidateId", candidateId)
                    startActivity(intent)
                }.addOnFailureListener { exception ->
                    Log.e(TAG, "Failed to retrieve data: ${exception.message}")
                    Toast.makeText(this, "Failed to retrieve data: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Log.e(TAG, "Current user is null")
                Toast.makeText(this, "Current user is null", Toast.LENGTH_SHORT).show()
            }

        }

    }
}