package com.example.eventorganiser

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.eventorganiser.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Exception
import java.util.*
import java.util.jar.Manifest
import javax.security.auth.login.LoginException

class RegisterActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    var selectedphotouri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()

        register_registration.setOnClickListener {
            performregistration()
        }
        already_registered.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

        circle_image_regsi.setOnClickListener {
            if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 2)
            }
            else {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                intent.type = "image/*"
                startActivityForResult(intent, 0)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==2){
            if(grantResults.size>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                intent.type = "image/*"
                startActivityForResult(intent, 0)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==0 && resultCode== Activity.RESULT_OK && data!=null){
            selectedphotouri = data.data
            try{
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedphotouri)
                circle_image_regsi.setImageBitmap(bitmap)

//                circle_image_regsi.alpha = 0f
            }
            catch (e:Exception){
                e.printStackTrace()
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun performregistration(){
        val email = email_registration.text.toString()
        val password = password_registration.text.toString()
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Either email or password is empty", Toast.LENGTH_LONG).show()
            return
        }
        Log.d("Main Activity","Email is $email")
        Log.d("Main Activity","Passowoed is $password")
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(!it.isSuccessful){
                return@addOnCompleteListener
            }
            //else is sucessfull
            //in the ready application we will try to show a loading spinner and also make intent to move
            //to login activity
            uploadimagetofirebasestorage()
            Log.d("Main Activity","Created User with UID ${it.result!!.user!!.uid}")
        }.addOnFailureListener {
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
        }

    }
    private fun uploadimagetofirebasestorage(){
        if(selectedphotouri==null){
            return
        }
        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")
        ref.putFile(selectedphotouri!!).addOnSuccessListener {
            Log.d("Register Activity", "Sucessfully Image Uploaded ${it.metadata?.path}")
            ref.downloadUrl.addOnSuccessListener {
                Log.d("Register Activity", "File Location $it")
                saveusertodatabase(it.toString())
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun saveusertodatabase(profileImageURL: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/user/$uid")
        val user = User(uid, username_registration.text.toString(), profileImageURL)
        ref.setValue(user).addOnSuccessListener {
            Log.d("Register Activity","User Saved to firebase database")
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

}