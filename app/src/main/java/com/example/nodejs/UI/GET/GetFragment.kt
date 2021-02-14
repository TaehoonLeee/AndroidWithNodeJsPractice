package com.example.nodejs.UI.GET

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nodejs.R
import com.example.nodejs.UI.GET.GetViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.get_fragment.*

@AndroidEntryPoint
class GetFragment : Fragment(R.layout.get_fragment) {

    private val getViewModel by viewModels<GetViewModel>()
    private lateinit var getAdapter: GETAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAdapter = GETAdapter()
        rvGet.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = getAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getViewModel.Users.observe(viewLifecycleOwner, Observer {
            getAdapter.setUsers(it)
        })
    }

}