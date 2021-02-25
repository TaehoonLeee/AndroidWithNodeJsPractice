package com.example.nodejs.UI.entry

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import com.example.nodejs.MainActivity
import com.example.nodejs.R
import com.example.nodejs.Repository.NodeRepository
import com.example.nodejs.Repository.NodeRepository_Factory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_sign_up.*
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private val signUpViewModel : SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnDone.setOnClickListener {
            signUp()
        }

        btnSignUp.setOnClickListener {
            confirmSignUp()
        }

    }

    private fun confirmSignUp() {
        val id = etID.text.toString()
        val code = etAuthCode.text.toString()

        Amplify.Auth.confirmSignUp(
            id,
            code,
            { result ->
                Log.e("Confirm Sign Up", if (result.isSignUpComplete) "Confirm signUp Success" else "Not Complete")
                if(result.isSignUpComplete) {
                    signUpViewModel.onCreateMember(id)
                    val direction =
                        SignUpFragmentDirections.actionSignUpFragmentToFriendsFragment(id)
                    (activity as MainActivity).setUserName(id)
                    findNavController().navigate(direction)
                }
            },
            { error -> Log.e("Confirm Error", error.message!!) }
        )
    }

    private fun signUp() {
        val id = etID.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        Amplify.Auth.signUp(
            id,
            password,
            AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), email).build(),
            { result -> Log.e("SignUp", "Result : $result") },
            { error -> Log.e("SignUp", "Error : $error") }
        )

        AuthCodeText.visibility = View.VISIBLE
        etAuthCode.visibility = View.VISIBLE
        authlayout.visibility = View.VISIBLE
    }
}