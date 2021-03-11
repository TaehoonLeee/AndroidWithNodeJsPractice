package com.example.nodejs.UI.chat

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nodejs.MainActivity
import com.example.nodejs.R
import com.example.nodejs.UI.profile.ProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.chat_fragment.*
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.StringBuilder

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.chat_fragment) {

    private val chatViewModel : ChatViewModel by viewModels<ChatViewModel>()
    private lateinit var chatAdapter : ChatAdapter
    private val args : ChatFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userName = args.userName
        val roomName = args.roomName

        chatAdapter = ChatAdapter(userName, {friend ->
            val isFriend = chatViewModel.friendList.value!!.contains(friend)
            val direction =
                ProfileFragmentDirections.actionGlobalProfileFragment(friend.name, isFriend)
            findNavController().navigate(direction)
        })

        rvChat.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        btnSend.setOnClickListener {
            val currentTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("h:mm a")
            val formatted = currentTime.format(formatter)
            val token = (activity as MainActivity).getToken()

            chatViewModel.addMessage(userName, msgToSend.text.toString(), formatted, roomName, chatScrollView, token)
            msgToSend.text = null
        }

        chatToolbar.title = roomName.split("-").filter { it != userName }.joinToString()

        focusDownfab.setOnClickListener {
            focusDown(false)
        }

        chatToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.infoBtn -> {
                    val direction =
                        ChatFragmentDirections.actionChatFragmentToMemberInfoFragment(roomName)
                    findNavController().navigate(direction)

                    true
                }
                else -> false
            }
        }

        pickImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "pick image"), 123)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        chatViewModel.messages.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()) {
                topText.visibility = View.GONE
            }
            chatAdapter.setMessages(it)
        })

        chatViewModel.newMessage.observe(viewLifecycleOwner, Observer {
            chatViewModel.onGetMessages(args.roomName, args.userName)
            focusDown(true)
        })

        focusDown(true)
        chatScrollView?.viewTreeObserver?.addOnScrollChangedListener {
            if (chatScrollView != null) {
                val scrollY = chatScrollView.scrollY
                val totalHeight = chatScrollView.getChildAt(0).height - chatScrollView.height
                if (totalHeight - scrollY > 200) {
                    focusDownfab.show()
                }
                else {
                    focusDownfab.hide()
                }
            }
        }

        msgToSend.setOnFocusChangeListener { _, selected ->
            if(selected) {
                pickImage.visibility = View.GONE
                captureImage.visibility = View.GONE
            }
            else {
                pickImage.visibility = View.VISIBLE
                captureImage.visibility = View.VISIBLE
            }
        }

        (activity as MainActivity).isShowBar(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        chatViewModel.closeSocket()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 123 && resultCode == Activity.RESULT_OK && data!!.data != null) {
            val fileUri = activity?.contentResolver?.openInputStream(data.data!!)

            val bm = BitmapFactory.decodeStream(fileUri)

            val filesDir = activity?.applicationContext?.filesDir
            val file = File(filesDir, filesDir?.name + ".png")
            val bos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.PNG, 0, bos)
            val bitMapData = bos.toByteArray()

            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()

            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("img", file.name, reqFile)
            val name = "img".toRequestBody("text/plain".toMediaTypeOrNull())
            val sender = args.userName.toRequestBody("text/plain".toMediaTypeOrNull())
            val roomName = args.roomName.toRequestBody("text/plain".toMediaTypeOrNull())

            val currentTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("h:mm a")
            val formatted = currentTime.format(formatter).toRequestBody("text/plain".toMediaTypeOrNull())
            chatViewModel.uploadeImage(body, name, sender, roomName, formatted, chatScrollView)
        }
    }

    private fun focusDown(isStart : Boolean) {
        // 채팅 뷰 하단으로 내리기
        when(isStart) {
            true -> {
                chatScrollView?.let {
                    it.postDelayed({
                        it.fullScroll(View.FOCUS_DOWN)
                    }, 1000)
                }
            }
            false -> {
                chatScrollView?.let {
                    it.fullScroll(View.FOCUS_DOWN)
                }
            }
        }
    }

}