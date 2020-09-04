package com.example.eventorganiser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {
    //Firebase
    var mAuth: FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        mAuth = FirebaseAuth.getInstance()

        val emailLogin = emailidmain.text.toString()
        val passwordLogin = passmain.text.toString()

        loginbtn.setOnClickListener {
            val emailId = emailidmain.text.toString()
            val password = passmain.text.toString()
            if(emailId.length==0 || password.length==0){
                Toast.makeText(this, "Either the password or the emailId is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mAuth!!.signInWithEmailAndPassword(emailId, password).addOnCompleteListener {
                if(!it.isSuccessful){return@addOnCompleteListener}
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }

        newcustomer.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    fun skipToMain(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
