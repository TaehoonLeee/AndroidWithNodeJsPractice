package com.example.nodejs.UI.entry

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.amplifyframework.core.Amplify
import com.example.nodejs.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_entry.*

@AndroidEntryPoint
class EntryFragment : Fragment(R.layout.fragment_entry) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Login.setOnClickListener {
            val name = editEmail.text.toString()
            val direction =
                EntryFragmentDirections.actionEntryFragmentToChatRoomFragment(name)
            findNavController().navigate(direction)
        }

        signup.setOnClickListener {
            val direction =
                EntryFragmentDirections.actionEntryFragmentToSignUpFragment()
            findNavController().navigate(direction)
        }

        Amplify.Auth.fetchAuthSession(
            { result -> Log.e("EntryFragment", result.toString()) },
            { error -> Log.e("EntryFragment, Error : ", error.toString()) }
        )
    }
}