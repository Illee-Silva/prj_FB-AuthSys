package com.example.prj_connectauth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.example.prj_connectauth.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.bttLogin.setOnClickListener(){
            val email = binding.txtEmail.text.toString().trim()
            val password = binding.txtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Preencha todos os Campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

        }

        binding.textView4.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.bttForgotPassword.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut()
    }
}