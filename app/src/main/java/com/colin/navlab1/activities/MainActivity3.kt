package com.colin.navlab1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.colin.navlab1.R
import com.colin.navlab1.adapters.NotesAdapter
import com.colin.navlab1.databinding.ActivityMain3Binding
import com.colin.navlab1.interfaces.DeleteNoteClickListener
import com.colin.navlab1.model.Note
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class MainActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityMain3Binding
    private lateinit var adapter: NotesAdapter
    private var useFireStore = false

    private var snapShotListener: ListenerRegistration? = null
    private var postsRef: DatabaseReference? = null
    private var postListener: ValueEventListener? = null

    private lateinit var realTimeDBRef: DatabaseReference
    private lateinit var fireStoreDB: FirebaseFirestore

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ext_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_logout) {
            Firebase.auth.signOut()
            val int = Intent(this, MainActivity2::class.java)
            startActivity(int)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        realTimeDBRef = Firebase.database("https://acquired-talent-349123-default-rtdb.europe-west1.firebasedatabase.app/").reference
        fireStoreDB = Firebase.firestore

        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.switchDb.setOnCheckedChangeListener { _, isChecked ->
            useFireStore = isChecked
            setDataListener(useFireStore)
            (binding.recyclerViewNotes.adapter as NotesAdapter).setData(mutableListOf())
        }
        binding.buttonSave.setOnClickListener {
            val note = Note()
            note.id = Random.nextInt().toString()
            note.body = binding.editTextNote.text.toString()

            if(!useFireStore) {
                realTimeDBRef.child("Notes").child(note.id!!).setValue(note)
            } else {
                fireStoreDB.collection("Notes")
                    .add(note)
                    .addOnSuccessListener { documentReference ->
                        Log.d("DB", "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("DB", "Error adding document", e)
                    }
            }

        }

        adapter = NotesAdapter(object: DeleteNoteClickListener {
            override fun onPositionClicked(pos: Int, view: View) {
                val item = (binding.recyclerViewNotes.adapter as NotesAdapter).getNote(pos)
                if(!useFireStore) {
                    realTimeDBRef.child("Notes").child(item.id.toString()).setValue(null)
                } else {
                    fireStoreDB.collection("Notes").document(item.id!!).delete()
                }
            }
        })

        binding.recyclerViewNotes.layoutManager = LinearLayoutManager(this@MainActivity3)
        binding.recyclerViewNotes.adapter = adapter

        setDataListener(useFireStore)
        Log.d("NAME",intent.extras?.getString("name").toString())
    }

    private fun setDataListener(useFireStore: Boolean) {
        if(!useFireStore) {
            snapShotListener?.remove()
            postsRef =
                Firebase.database("https://acquired-talent-349123-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("Notes")
            postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val noteList: MutableList<Note> = ArrayList()
                    for (ds in dataSnapshot.children) {
                        val note: Note? = ds.getValue(Note::class.java)
                        Log.d("DB", note?.body!!)
                        noteList.add(note)
                    }
                    Log.d("DB SIZE", noteList.size.toString())
                    (binding.recyclerViewNotes.adapter as NotesAdapter).setData(noteList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("DATABASE", "loadPost:onCancelled", databaseError.toException())
                }
            }
            postsRef!!.addValueEventListener(postListener as ValueEventListener)
        } else {
            postsRef?.removeEventListener(postListener as ValueEventListener)
            val docRef = fireStoreDB.collection("Notes")
            val listener = docRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("DB", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val noteList: MutableList<Note> = ArrayList()

                    for (ds in snapshot.documents) {
                        val note: Note? = ds.toObject<Note>()
                        note!!.id = ds.id
                        noteList.add(note)
                    }
                    (binding.recyclerViewNotes.adapter as NotesAdapter).setData(noteList)
                } else {
                    Log.d("DB", "Current data: null")
                }
            }
        }
    }
}