package com.colin.navlab1.activities

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.colin.navlab1.databinding.ActivityMain2Binding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity2 : AppCompatActivity() {
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMain2Binding
    private val TAG = "AUTH"
    private var showOneTapUI = true

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            val int = Intent(this, MainActivity3::class.java)
            int.putExtra("name", currentUser.uid)
            startActivity(int)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()

                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId("900294787082-gmhm28qgls7m6i16qrvpi0r9o3m4lq31.apps.googleusercontent.com")

                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        val loginResultHandler = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result: ActivityResult ->
            // handle intent result here
            if (result.resultCode == RESULT_OK) Log.d(
                TAG,
                "RESULT_OK."
            )
            if (result.resultCode == RESULT_CANCELED) Log.d(
                TAG,
                "RESULT_CANCELED."
            )
            if (result.resultCode == RESULT_FIRST_USER) Log.d(
                TAG,
                "RESULT_FIRST_USER."
            )
            try {
                val credential =
                    oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
//                val username = credential.id
//                val password = credential.password

                firebaseAuthWithGoogle(idToken)


                val int = Intent(this, MainActivity3::class.java)
                int.putExtra("name", credential.id)
                startActivity(int)

            } catch (e: ApiException) {
                when (e.statusCode) {
                    CommonStatusCodes.CANCELED -> {
                        Log.d(TAG, "One-tap dialog was closed.")
                        // Don't re-prompt the user.
                        showOneTapUI = false
                    }
                    CommonStatusCodes.NETWORK_ERROR -> Log.d(
                        TAG,
                        "One-tap encountered a network error."
                    )
                    else -> Log.d(
                        TAG, "Couldn't get credential from result."
                                + e.localizedMessage
                    )
                }
            }
        }

        binding.button4.setOnClickListener {
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this) { result ->
                    try {
                        loginResultHandler.launch(
                            IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener(this) { e ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    Log.d(TAG, e.localizedMessage!!.toString())
                }
        }

        binding.buttonLogin.setOnClickListener {
            auth.signInWithEmailAndPassword(binding.editTextLogin.text.toString(), binding.editTextTextPassword2.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        val int = Intent(this, MainActivity3::class.java)
                        int.putExtra("name", user?.uid)
                        startActivity(int)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }
}