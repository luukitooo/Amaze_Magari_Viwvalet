package com.lukabaia.classwork_6.fragments

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lukabaia.classwork_6.BaseFragment
import com.lukabaia.classwork_6.databinding.FragmentHomeBinding
import com.lukabaia.classwork_6.utils.PreferenceKeys
import com.lukabaia.classwork_6.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val args: HomeFragmentArgs by navArgs()

    private val viewModel: HomeViewModel by viewModels()

    override fun viewCreated() {

        init()

        onClickListeners()

        observers()

    }

    private fun init() {
        binding.tvToken.text = args.token
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPreferences().collect {
                if (it.contains(stringPreferencesKey(PreferenceKeys.TOKEN))){
                    binding.tvToken.text = it[stringPreferencesKey(PreferenceKeys.TOKEN)]
                }
            }
        }
    }

    private fun onClickListeners() {
        binding.btnLogOut.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.clear()
                // With Remove:
                // viewModel.remove(PreferenceKeys.TOKEN)
            }.invokeOnCompletion {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
            }
        }
    }
}