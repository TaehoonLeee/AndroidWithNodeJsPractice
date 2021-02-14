package com.example.nodejs.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.nodejs.R
import kotlinx.android.synthetic.main.fragment_entry.*

class EntryFragment : Fragment(R.layout.fragment_entry) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Login.setOnClickListener {
            val name = editName.text.toString()
            val direction =
                    EntryFragmentDirections.actionEntryFragmentToChatRoomFragment(name)
            view.findNavController().navigate(direction)
        }
    }
}