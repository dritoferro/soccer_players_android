package br.com.tagliaferro.soccerplayers.screens.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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

        viewModel.isPressed.observe(viewLifecycleOwner, Observer { clicked ->
            if (clicked) {
                Toast.makeText(root.context, "Criando o usuário...", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.createdUser.observe(viewLifecycleOwner, Observer { success ->
            Toast.makeText(root.context, success.message, Toast.LENGTH_SHORT).show()
            root.findNavController()
                .navigate(CreateUserFragmentDirections.actionNavCreateUserToNavLogin())
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            Toast.makeText(
                root.context,
                "${error.status}:${error.message}\n" +
                        "${error.subErrors?.map { it.toString() }}",
                Toast.LENGTH_SHORT
            ).show()
        })
        //TODO resolver o problema da ação do botão voltar, quando criado um usuário com sucesso,
        // vai pra tela de login e depois se clicar em voltar está exibindo a mensagem de erro...

        return root
    }
}