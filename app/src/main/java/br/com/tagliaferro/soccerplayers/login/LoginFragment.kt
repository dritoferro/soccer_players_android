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

class LoginFragment : Fragment() {

    private val TAG = LoginFragment::class.java.simpleName

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
            viewModel.isPressedComplete()
        })

        return binding.root
    }

    private fun pressed(isPressed: Boolean) {
        if (isPressed) {
            Toast.makeText(context, "Fazendo o login...", Toast.LENGTH_SHORT).show()
        }
    }
}