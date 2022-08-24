package com.lukabaia.classwork_6.fragments

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lukabaia.classwork_6.BaseFragment
import com.lukabaia.classwork_6.databinding.FragmentLoginBinding
import com.lukabaia.classwork_6.models.LoginForm
import com.lukabaia.classwork_6.models.UserInfo
import com.lukabaia.classwork_6.utils.FragmentResUtils
import com.lukabaia.classwork_6.utils.PreferenceKeys
import com.lukabaia.classwork_6.utils.ResponseHandler
import com.lukabaia.classwork_6.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()

    override fun viewCreated() {

        checkSession()

        fragmentResultListener()

        onClickListeners()

    }

    private fun onClickListeners() {
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
        binding.btnLogin.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getLoginFlow(getUserInfo()).collect {
                    when(it) {
                        is ResponseHandler.Success<*> -> {
                            if (binding.cbRemember.isChecked) {
                                viewModel.save(PreferenceKeys.TOKEN, (it.result as LoginForm).token ?: "")
                            } else {
                                findNavController().navigate(
                                    LoginFragmentDirections.actionLoginFragmentToHomeFragment(
                                        token = (it.result as LoginForm).token ?: ""
                                    )
                                )
                            }
                        }
                        is ResponseHandler.Error -> {
                            Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                        }
                        is ResponseHandler.Loader -> {
                            binding.progressBar.isVisible = it.isLoading
                        }
                    }
                }
            }
        }
    }

    private fun checkSession() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPreferences().collect {
                if (it.contains(stringPreferencesKey(PreferenceKeys.TOKEN))) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment(
                        token = it[stringPreferencesKey(PreferenceKeys.TOKEN)] ?: "No Data"
                    ))
                }
            }
        }
    }

    private fun fragmentResultListener() {
        setFragmentResultListener(FragmentResUtils.AUTH_KEY) { _, bundle ->
            binding.etEmail.setText(bundle.getString(FragmentResUtils.EMAIL, "No Value"))
            binding.etPassword.setText(bundle.getString(FragmentResUtils.PASSWORD, "No Value"))
        }
    }

    private fun getUserInfo() = UserInfo(
        binding.etEmail.text.toString(),
        binding.etPassword.text.toString()
    )

}