package br.com.tagliaferro.soccerplayers.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import br.com.tagliaferro.soccerplayers.R
import br.com.tagliaferro.soccerplayers.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        val root = binding.root

        binding.btnLogin.setOnClickListener {
            root.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNavLogin())
        }

        binding.btnCreateAccount.setOnClickListener {
            root.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToNavCreateUser())
        }

        return root
    }
}