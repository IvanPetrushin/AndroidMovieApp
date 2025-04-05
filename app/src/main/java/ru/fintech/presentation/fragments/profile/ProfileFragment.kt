package ru.fintech.presentation.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.fintech.R
import ru.fintech.databinding.FragmentProfileBinding

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomNavigation.favouritesButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_favouritesFragment)
        }

        binding.bottomNavigation.popularsButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_popularsFragment)
        }
    }

}