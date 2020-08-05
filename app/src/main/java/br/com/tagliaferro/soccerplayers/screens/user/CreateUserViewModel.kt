package br.com.tagliaferro.soccerplayers.screens.user

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tagliaferro.soccerplayers.entities.CreateUserDTO
import br.com.tagliaferro.soccerplayers.entities.SimpleResponse
import br.com.tagliaferro.soccerplayers.exceptions.ErrorDTO
import br.com.tagliaferro.soccerplayers.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateUserViewModel @ViewModelInject
constructor(private val repository: UserRepository) : ViewModel() {

    private val _createdUser = MutableLiveData<SimpleResponse>()

    private val _isPressed = MutableLiveData<Boolean>()

    val isPressed: LiveData<Boolean>
        get() = _isPressed

    val createdUser: LiveData<SimpleResponse>
        get() = _createdUser

    private val _error = MutableLiveData<ErrorDTO>()

    val error: LiveData<ErrorDTO>
        get() = _error

    init {
        _isPressed.value = false
    }

    fun save(user: CreateUserDTO) {
        _isPressed.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val createResult = repository.createUser(user)

                if (createResult.body != null && createResult.body is SimpleResponse) {
                    withContext(Dispatchers.Main) {
                        onSuccess(createResult.body)
                    }

                } else if (createResult.error != null) {
                    withContext(Dispatchers.Main) {
                        onError(createResult.error)
                    }
                }
            } catch (ex: Exception) {
                onError(ErrorDTO(status = "Unmapped Error", message = ex.message))
            }
        }
    }

    private fun onSuccess(createdUser: SimpleResponse) {
        _createdUser.value = createdUser
    }

    private fun onError(errorDTO: ErrorDTO) {
        _error.value = errorDTO
    }
}
