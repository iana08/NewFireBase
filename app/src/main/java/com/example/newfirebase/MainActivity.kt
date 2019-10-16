package com.example.newfirebase

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var mAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(String::class.java)
                Log.d("info", "Value is: " + value!!)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("info", "Failed to read value.", error.toException())
            }
        })

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
//         Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
//            status.text = getString(R.string.emailpassword_status_fmt,
//                user.email, user.isEmailVerified)
//            detail.text = getString(R.string.firebase_status_fmt, user.uid)
//
//            emailPasswordButtons.visibility = View.GONE
//            emailPasswordFields.visibility = View.GONE
//            signedInButtons.visibility = View.VISIBLE
//
//            verifyEmailButton.isEnabled = !user.isEmailVerified
            Toast.makeText(this, "User is found", Toast.LENGTH_SHORT).show()
        } else {
//            status.setText(R.string.signed_out)
//            detail.text = null
//
//            emailPasswordButtons.visibility = View.VISIBLE
//            emailPasswordFields.visibility = View.VISIBLE
//            signedInButtons.visibility = View.GONE
            Toast.makeText(this, "User is Unknown", Toast.LENGTH_SHORT).show()
        }
    }

    fun createAccount(email: String, password: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("info", "createUserWithEmail:success")
                    val user = mAuth!!.getCurrentUser()
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("info", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this@MainActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
}
