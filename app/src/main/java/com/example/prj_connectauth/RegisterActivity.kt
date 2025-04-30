package com.example.prj_connectauth

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.google.firebase.auth.FirebaseAuth
import com.example.prj_connectauth.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar

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
            val email = binding.txtEmail2.text.toString().trim()
            val password = binding.txtPassword2.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()){
                Snackbar.make(binding.root, "Preencha Todos os Campos!", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task ->
                    if(task.isSuccessful){
                        Snackbar.make(binding.root, "Registrto bem sucedido!", Snackbar.LENGTH_SHORT).show()
                    }
                    else {
                        Snackbar.make(binding.root, "ERRO: ${task.exception?.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }

        }

    }

}