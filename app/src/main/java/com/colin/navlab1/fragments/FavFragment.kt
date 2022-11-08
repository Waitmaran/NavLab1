package com.colin.navlab1.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.colin.navlab1.R
import com.colin.navlab1.adapters.CustomAdapter
import com.colin.navlab1.adapters.LikedNewsAdapter
import com.colin.navlab1.databinding.FragmentFavBinding
import com.colin.navlab1.databinding.FragmentHomeBinding
import com.colin.navlab1.interfaces.ModelViewAdapter
import com.colin.navlab1.interfaces.NewsRowClickListener
import com.colin.navlab1.model.Post
import com.colin.navlab1.mvvm.NewsViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FavFragment : Fragment() {
    private var _binding: FragmentFavBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by activityViewModels()
    private  lateinit var adapter: LikedNewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavBinding.inflate(inflater, container, false)

        binding.likedNewsRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter = LikedNewsAdapter(object : NewsRowClickListener {
            override fun onPositionClicked(position: Int, p0: View) {
                if(p0.id == R.id.imageButtonLike) {
                    likeNews(position)
                }
            }
        })

        binding.likedNewsRecyclerView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.news.observe(viewLifecycleOwner) { news ->
            (binding.likedNewsRecyclerView.adapter as ModelViewAdapter).setDataset(news)
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val postsRef = Firebase.database("https://acquired-talent-349123-default-rtdb.europe-west1.firebasedatabase.app/").getReference("likedPosts")
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Get Post object and use the values to update the UI
//                val postsList: MutableList<Post> = ArrayList()
//                for (ds in dataSnapshot.children) {
//                    val post: Post? = ds.getValue(Post::class.java)
//                    postsList.add(post!!)
//                }
//                (binding.likedNewsRecyclerView.adapter as ModelViewAdapter).setDataset(postsList)
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//                Log.w("DATABASE", "loadPost:onCancelled", databaseError.toException())
//            }
//        }
//        postsRef.addValueEventListener(postListener)
//    }

    private fun likeNews(pos: Int) {
        val item = viewModel.likeNews(pos)
        adapter.updateItem(item, pos)
    }
}