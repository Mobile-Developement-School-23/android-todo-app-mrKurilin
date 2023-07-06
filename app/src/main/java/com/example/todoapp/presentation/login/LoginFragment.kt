package com.example.todoapp.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentLoginBinding
import com.example.todoapp.di.appComponent
import com.example.todoapp.di.lazyViewModel
import com.example.todoapp.presentation.util.showLongToast
import com.yandex.authsdk.YandexAuthException
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

        if (loginViewModel.isAuthorized()) {
            findNavController().navigate(R.id.action_loginFragment_to_toDoListFragment)
        }

        sdk = YandexAuthSdk(requireContext(), YandexAuthOptions(requireContext()))

        launcher = registerForActivityResult(YandexSignInActivityResultContract()) { pair ->
            if (pair == null) return@registerForActivityResult
            handleYandexSignInActivityResult(pair)
        }

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun handleYandexSignInActivityResult(pair: Pair<Int, Intent?>) = try {
        val token = sdk.extractToken(pair.first, pair.second)
        if (token != null) {
            loginViewModel.putToken(token.value)
            findNavController().navigate(R.id.action_loginFragment_to_toDoListFragment)
        } else {
            showLongToast(R.string.failed_to_login)
        }
    } catch (yandexAuthException: YandexAuthException) {
        showLongToast(R.string.failed_to_login)
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