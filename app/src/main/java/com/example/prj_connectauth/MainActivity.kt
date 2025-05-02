package com.example.prj_connectauth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.google.firebase.auth.FirebaseAuth
import com.example.prj_connectauth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentUser = auth.currentUser

        currentUser?.email?.let { email ->
            binding.mainTvEmailShower.text = "Seu Email: ${email}"
        } ?: run {
            binding.mainTvEmailShower.text = "Nenhum Usuário Logado"
        }

        val email = currentUser?.email?.uppercase()

        if (email == "ILLEE.SILVALB@GMAIL.COM" || email == "email2"){
            //implementar depois
        }

    }
}