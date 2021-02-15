package com.example.nodejs.UI.addchatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nodejs.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_chat_room.*


@AndroidEntryPoint
class CreateChatRoomFragment : BottomSheetDialogFragment() {

    private val createChatRoomViewModel : CreateChatRoomViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_chat_room, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createBtn.setOnClickListener {
            val roomName = createRoomEdit.text.toString()
            createChatRoomViewModel.createRoom(roomName, findNavController())
        }
    }
}