package com.example.prj_connectauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.google.firebase.auth.FirebaseAuth
import com.example.prj_connectauth.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

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


        binding.bttLogin.setOnClickListener{

            it.isEnabled = false

            binding.progressBar.visibility = View.VISIBLE

            val email = binding.txtEmail2.text.toString().trim()
            val password = binding.txtPassword2.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()){
                it.isEnabled = true
                Snackbar.make(binding.root, "Preencha Todos os Campos!", Snackbar.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Snackbar.make(binding.root, "E-mail inválido!", Snackbar.LENGTH_SHORT).show()
                it.isEnabled = true
                binding.progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(this){ task ->
                    binding.progressBar.visibility = View.GONE
                    it.isEnabled = true

                    if(task.isSuccessful){
                        Snackbar.make(binding.root, "Registrto bem sucedido!", Snackbar.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                    else {
                        handleSignUpError(task.exception)
                    }
                }

                .addOnFailureListener { exception ->
                    binding.progressBar.visibility = View.GONE
                    it.isEnabled = true

                    handleSignUpError(exception)

                }

        }


    }

    private fun handleSignUpError(exception: Exception?) {
        when (exception) {
            is FirebaseAuthUserCollisionException -> {
                // E-mail já está cadastrado
                Snackbar.make(binding.root, "Este e-mail já está em uso!", Snackbar.LENGTH_SHORT).show()
            }
            is FirebaseAuthWeakPasswordException -> {
                // Senha fraca
                Snackbar.make(binding.root, "Senha muito fraca!", Snackbar.LENGTH_SHORT).show()
            }
            is FirebaseAuthInvalidCredentialsException -> {
                // Formato de e-mail inválido
                Snackbar.make(binding.root, "E-mail inválido!", Snackbar.LENGTH_SHORT).show()
            }
            else -> {
                // Erro genérico (ex: rede)
                Log.e("REGISTER_ERROR", "Classe da exceção: ${exception?.javaClass?.name}")
                Log.e("REGISTER_ERROR", "Mensagem: ${exception?.message}")

                Snackbar.make(binding.root, "Erro: ${exception?.message}", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}