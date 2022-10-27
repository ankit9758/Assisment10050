package com.neostardemo.viewmodel

import android.net.Uri
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.neostardemo.R
import com.neostardemo.models.User
import com.neostardemo.repository.MainRepository
import com.neostardemo.utils.AppConstants
import com.neostardemo.utils.SingleLiveEvent
import com.neostardemo.utils.UiText
import com.neostardemo.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val repository: MainRepository
) : ViewModel() {

    var firstName = ObservableField<String>()
    var lastName = ObservableField<String>()
    var phoneNumber = ObservableField<String>()
    var email = ObservableField<String>()
    var password = ObservableField<String>()
    var confirmPassword = ObservableField<String>()
    var address = ObservableField<String>()
    var landmark = ObservableField<String>()
    var city = ObservableField<String>()
    var state = ObservableField<String>()
    var pinCode = ObservableField<String>()
    var qualification = ObservableField<String>()
    var yearOfPassing = ObservableField<String>()
    var grade = ObservableField<String>()
    var experience = ObservableField<String>()
    var designation = ObservableField<String>()
    var domain = ObservableField<String>()
    var imageUri = ObservableField<String>()

    var gender = ObservableField(AppConstants.MALE)


    private val _errorChannel = SingleLiveEvent<UiText>()
    val errorChannel: SingleLiveEvent<UiText> get() = _errorChannel

    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>>
        get() = _userList


    init {
        getAllUserListData()
    }


    /*  this method get all  user data
   */
    private fun getAllUserListData() {
        viewModelScope.launch {
            repository.fetchAllUsers().onEach {
                _userList.postValue(it)
            }.launchIn(viewModelScope)
        }
    }

    /*  this method save user data
   @param User
    */
    private fun saveUserData(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveUserToDb(user)
        }
    }

    //check the validation for register screen
    private fun registerValidation(): Boolean {
        when {
            firstName.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_first_name))
                return false
            }
            firstName.get().toString().length < 4 -> {
                _errorChannel.postValue(UiText.StringResource(R.string.valid_first_name))
                return false
            }
            lastName.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_last_name))
                return false
            }
            lastName.get().toString().length < 4 -> {
                _errorChannel.postValue(UiText.StringResource(R.string.valid_last_name))
                return false
            }
            phoneNumber.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_phone))
                return false
            }
            phoneNumber.get().toString().length < 10 -> {
                _errorChannel.postValue(UiText.StringResource(R.string.valid_phone))
                return false
            }
            email.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_email))
                return false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email.get().toString()).matches() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.valid_email))
                return false
            }
            password.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_password))
                return false
            }
            !isValidPassword(password.get().toString()) -> {
                _errorChannel.postValue(UiText.StringResource(R.string.valid_password))
                return false
            }
            confirmPassword.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_confirm_password))
                return false
            }
            confirmPassword.get().toString() != password.get().toString() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.same_password))
                return false
            }

            imageUri.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_pic))
                return false
            }

            else -> return true
        }
    }

    //check the validation of education screen
    private fun educationValidation(): Boolean {
        when {
            qualification.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_qualification))
                return false
            }
            yearOfPassing.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_year_of_passing))
                return false
            }
            yearOfPassing.get().toString().length < 4 -> {
                _errorChannel.postValue(UiText.StringResource(R.string.invalid_passing_year))
                return false
            }

            grade.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_grade))
                return false
            }

            experience.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_experience))
                return false
            }

            designation.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_designation))
                return false
            }


            domain.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_domain))
                return false
            }


            else -> return true
        }
    }


    //click on next button in register screen
    fun onNextClick(view: View) {
        if (registerValidation()) {
            view.findNavController().navigate(R.id.action_registerFragment_to_yourInfoFragment)
        }

    }

    //click event next button in Your info screen
    fun onInfoNextClick(view: View) {
        if (educationValidation()) {
            view.findNavController().navigate(R.id.action_yourInfoFragment_to_addressInfoFragment)
        }

    }

    // click to submit the user data
    fun onSubmitButtonClick(view: View) {
        if (addressValidation()) {
            saveUserData(
                User(
                    null,
                    imageUri.get().toString(),
                    firstName.get().toString(),
                    lastName.get().toString(),
                    phoneNumber.get().toString(),
                    email.get().toString(),
                    gender.get().toString(),
                    password.get().toString(),
                    address.get().toString(),
                    landmark.get().toString(),
                    city.get().toString(),
                    state.get().toString(),
                    pinCode.get().toString(),
                    qualification.get().toString(),
                    yearOfPassing.get().toString(),
                    grade.get().toString(),
                    experience.get().toString(),
                    designation.get().toString(),
                    domain.get().toString()
                )
            )
            Toast.makeText(view.context, "User data saved successfully.", Toast.LENGTH_SHORT).show()
            view.findNavController().navigate(R.id.action_addressInfoFragment_to_userListFragment)
        }

    }

    //on Previous button click in Address Screen
    fun onInfoPreviousClick(view: View) {
        view.findNavController().popBackStack()
    }


    //check the address validation in
    private fun addressValidation(): Boolean {
        when {
            address.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_address))
                return false
            }

            address.get().toString().length < 4 -> {
                _errorChannel.postValue(UiText.StringResource(R.string.valid_address))
                return false
            }
            landmark.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_landmark))
                return false
            }
            landmark.get().toString().length < 4 -> {
                _errorChannel.postValue(UiText.StringResource(R.string.valid_landmark))
                return false
            }
            city.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_city))
                return false
            }
            city.get().toString().length < 4 -> {
                _errorChannel.postValue(UiText.StringResource(R.string.valid_city))
                return false
            }

            state.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_state))
                return false
            }


            pinCode.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_pin_code))
                return false
            }

            pinCode.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.invalid_pin_code))
                return false
            }


            domain.get().isNullOrEmpty() -> {
                _errorChannel.postValue(UiText.StringResource(R.string.blank_domain))
                return false
            }


            else -> return true
        }
    }

}
