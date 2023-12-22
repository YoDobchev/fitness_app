package bg.zahov.app.editProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bg.zahov.fitness.app.R
import bg.zahov.fitness.app.databinding.FragmentEditProfileBinding

class FragmentEditProfile: Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val editProfileViewModel: EditProfileViewModel by viewModels({requireActivity()})
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            resetPassword.setOnClickListener {
                editProfileViewModel.sendPasswordResetLink { isSuccess, message ->
                    if(isSuccess){
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            editProfileViewModel.isUnlocked.observe(viewLifecycleOwner) {
                if(it){
                    lock.setImageResource(R.drawable.ic_open_lock)
                }
                emailField.isEnabled = it
                passwordField.isEnabled = it
            }

            editProfileViewModel.userName.observe(viewLifecycleOwner){
                usernameFieldText.apply {
                    setText(it)
                }
            }

            editProfileViewModel.userEmail.observe(viewLifecycleOwner){
                emailFieldText.apply {
                    setText(it)
                }
            }

            lock.setOnClickListener {
                AuthenticationDialog().show(childFragmentManager, AuthenticationDialog.TAG)
            }

            saveChanges.setOnClickListener {
                editProfileViewModel.isUnlocked.observe(viewLifecycleOwner) {
                    val username = usernameFieldText.text.toString()
                    val email = emailFieldText.text.toString()
                    val password = passwordFieldText.text.toString()
                    if(it){
                        editProfileViewModel.updateEmail(email) { isSuccess, message ->
                            if (isSuccess) {
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                            }
                        }

                        editProfileViewModel.updatePassword(password) { isSuccess, message ->
                            if (isSuccess) {
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    editProfileViewModel.updateUsername(username){ isSuccess, message ->
                        if(isSuccess){
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}