package br.com.tagliaferro.soccerplayers.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.tagliaferro.soccerplayers.R
import br.com.tagliaferro.soccerplayers.databinding.LoginFragmentBinding
import br.com.tagliaferro.soccerplayers.entities.LoginDTO
import br.com.tagliaferro.soccerplayers.exceptions.ErrorDTO

class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.loginViewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner

        binding.credentials = LoginDTO()

        viewModel.isPressed.observe(viewLifecycleOwner, Observer { isPressed ->
            pressed(isPressed)
        })

        viewModel.success.observe(viewLifecycleOwner, Observer { name ->
            success(name)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            showError(error)
        })

        return binding.root
    }

    private fun pressed(isPressed: Boolean) {
        if (isPressed) {
            Toast.makeText(context, "Fazendo o login...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun success(nome: String) {
        Toast.makeText(context, "Bem vindo $nome", Toast.LENGTH_SHORT).show()
        clearFields()
    }

    private fun showError(error: ErrorDTO) {
        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        clearFields()
    }

    private fun clearFields() {
        binding.edtUser.text.clear()
        binding.edtPassword.text.clear()
        binding.edtUser.requestFocus()
    }
}