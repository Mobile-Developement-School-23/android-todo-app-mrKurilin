package com.example.todoapp.presentation.login_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentLoginBinding
import com.example.todoapp.di.appComponent
import com.example.todoapp.di.lazyViewModel
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk

class LoginFragment : Fragment() {

    private lateinit var launcher: ActivityResultLauncher<YandexAuthSdk>
    private lateinit var sdk: YandexAuthSdk

    private val loginViewModel: LoginViewModel by lazyViewModel {
        appComponent().loginViewModel()
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        sdk = YandexAuthSdk(requireContext(), YandexAuthOptions(requireContext()))

        launcher = registerForActivityResult(YandexSignInActivityResultContract()) { pair ->
            val token = sdk.extractToken(pair.first, pair.second)
            if (token != null) {
                loginViewModel.putToken(token.value)
                findNavController().navigate(R.id.action_loginFragment_to_toDoListFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    getText(R.string.failed_to_login),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.yandexLoginButton.setOnClickListener {
            launcher.launch(sdk)
        }

        binding.useOfflineButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_toDoListFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}