package com.example.prj_connectauth

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope

import com.google.firebase.auth.FirebaseAuth
import com.example.prj_connectauth.databinding.ActivityRegisterBinding
import com.example.prj_connectauth.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.example.prj_connectauth.utils.GlobalSnackbar.invokeSnackbar
import com.google.firebase.FirebaseNetworkException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.regBttRegister.setOnClickListener{

            it.isEnabled = false

            binding.regPbDefault.visibility = View.VISIBLE

            val email = binding.regEtxtEmail.text.toString().trim()
            val password = binding.regEtxtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()){
                it.isEnabled = true
                invokeSnackbar(binding.root, "Preencha Todos os Campos!")
                binding.regPbDefault.visibility = View.GONE
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                invokeSnackbar(binding.root, "E-mail inválido!")
                it.isEnabled = true
                binding.regPbDefault.visibility = View.GONE
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(this){ task ->
                    binding.regPbDefault.visibility = View.GONE
                    it.isEnabled = true

                    if(task.isSuccessful){
                        invokeSnackbar(binding.root, "Registro bem sucedido!", "G")

                        lifecycleScope.launch {
                            delay(1500)
                            finish()
                        }
                    }
                    else {
                        handleSignUpError(task.exception)
                    }
                }

                .addOnFailureListener { exception ->
                    binding.regPbDefault.visibility = View.GONE
                    it.isEnabled = true

                    handleSignUpError(exception)

                }

        }


    }

    private fun handleSignUpError(exception: Exception?) {
        when (exception) {
            is FirebaseAuthUserCollisionException -> {
                // E-mail já está cadastrado
                invokeSnackbar(binding.root, "Este e-mail já está em uso!", "R")
            }
            is FirebaseAuthWeakPasswordException -> {
                // Senha fraca
                invokeSnackbar(binding.root, "Senha muito fraca!",)
            }
            is FirebaseAuthInvalidCredentialsException -> {
                // Formato de e-mail inválido
                invokeSnackbar(binding.root, "E-mail inválido!")
            }
            is FirebaseNetworkException -> {
                invokeSnackbar(binding.root, "Erro de conexão, Verifique a Internet!", "R")
            }
            else -> {
                // Erro genérico (ex: rede)
                Log.e("REGISTER_ERROR", "Classe da exceção: ${exception?.javaClass?.name}")
                Log.e("REGISTER_ERROR", "Mensagem: ${exception?.message}")

                invokeSnackbar(binding.root, "Erro: ${exception?.message}", "R")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut()
    }

}