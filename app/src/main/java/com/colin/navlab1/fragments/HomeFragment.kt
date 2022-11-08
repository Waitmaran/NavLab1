package com.colin.navlab1.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.colin.navlab1.mvvm.NewsViewModel
import com.colin.navlab1.R
import com.colin.navlab1.adapters.CustomAdapter
import com.colin.navlab1.databinding.FragmentHomeBinding
import com.colin.navlab1.interfaces.ModelViewAdapter
import com.colin.navlab1.interfaces.NewsRowClickListener
import com.colin.navlab1.repositories.NewsRepository


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by activityViewModels()
    private  lateinit var adapter: CustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.newsRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter = CustomAdapter(object : NewsRowClickListener {
            override fun onPositionClicked(position: Int, view: View) {
                if(view.id == R.id.imageButtonLike) {
                    likeNews(position)
                }
            }
        })

        binding.newsRecyclerView.adapter = adapter
        return binding.root
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        NewsRepository.loading.observe(viewLifecycleOwner) { loading ->
            if(loading) {
                binding.progressBarLoading.visibility = View.VISIBLE
            } else {
                binding.progressBarLoading.visibility = View.GONE
            }
        }
        viewModel.news.observe(viewLifecycleOwner) { news ->
            (binding.newsRecyclerView.adapter as ModelViewAdapter).setDataset(news)
        }
    }

    private fun likeNews(pos: Int) {
        val item = viewModel.likeNews(pos)
        adapter.updateItem(item, pos)
    }
}