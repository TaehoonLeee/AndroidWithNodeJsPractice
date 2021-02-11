package com.example.nodejs.UI.POST

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nodejs.Model.User
import com.example.nodejs.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post.*

@AndroidEntryPoint
class PostFragment : Fragment(R.layout.fragment_post) {

    private val postViewModel by viewModels<PostViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSet.setOnClickListener {
            setData()
        }
    }

    fun setData() {
        val id = editID.text.toString()
        val password = editPW.text.toString()

        postViewModel.setData(id, password)
    }
}