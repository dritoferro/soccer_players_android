package br.com.tagliaferro.soccerplayers.screens.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import br.com.tagliaferro.soccerplayers.R
import br.com.tagliaferro.soccerplayers.databinding.FragmentCreateUserBinding
import br.com.tagliaferro.soccerplayers.entities.CreateUserDTO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateUserFragment : Fragment() {

    private val viewModel: CreateUserViewModel by viewModels()

    private lateinit var binding: FragmentCreateUserBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_user,
            container,
            false
        )

        val root = binding.root

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.data = CreateUserDTO()

        binding.btnCancel.setOnClickListener {
            root.findNavController()
                .navigate(CreateUserFragmentDirections.actionNavCreateUserToHomeFragment2())
        }

        return root
    }
}