package com.colin.navlab1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.colin.navlab1.databinding.FragmentSocialBinding
import com.colin.navlab1.model.Post

class SocialFragment : Fragment() {
    private var _binding: FragmentSocialBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSocialBinding.inflate(inflater, container, false)

//        binding.friendsRecyclerView.layoutManager = LinearLayoutManager(context)
//        val ca: CustomAdapter = CustomAdapter(object : NewsRowClickListener {
//            override fun onPositionClicked(position: Int) {
//                TODO("Not yet implemented")
//            }
//        })
//        binding.friendsRecyclerView.adapter = ca

        return binding.root
    }
}