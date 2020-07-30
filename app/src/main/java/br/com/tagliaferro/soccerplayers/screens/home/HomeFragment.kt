package br.com.tagliaferro.soccerplayers.screens.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import br.com.tagliaferro.soccerplayers.R
import br.com.tagliaferro.soccerplayers.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.homeViewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner

        val root = binding.root

        viewModel.preferencesKey = getString(R.string.preferences_key)

        viewModel.preferences =
            context?.getSharedPreferences(viewModel.preferencesKey, Context.MODE_PRIVATE)!!

        viewModel.isAlreadyLogged.observe(viewLifecycleOwner, Observer { userLogged ->
            //TODO refatorar quando tiver a próxima tela pronta
            if (userLogged) {
                root.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToNavLogin())
            }
        })

        binding.btnLogin.setOnClickListener {
            root.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNavLogin())
        }

        binding.btnCreateAccount.setOnClickListener {
            root.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToNavCreateUser())
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkCache()
    }
}