package com.example.prj_connectauth

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prj_connectauth.databinding.ActivityDialogResetPasswordBinding

import com.google.firebase.auth.FirebaseAuth
import com.example.prj_connectauth.databinding.ActivityLoginBinding
import com.example.prj_connectauth.utils.GlobalSnackbar.invokeSnackbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import androidx.core.graphics.drawable.toDrawable


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

        binding.loginBttLogin.setOnClickListener{
            val email = binding.loginEtxtEmail.text.toString().trim()
            val password = binding.loginEtxtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()){
                invokeSnackbar(binding.root, "Preencha Todos os Campos!")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task ->
                    if (task.isSuccessful){
                        invokeSnackbar(binding.root, "Login Bem Sucedido!")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    else{
                        invokeSnackbar(binding.root, "Erro: ${task.exception?.message}")
                    }
                }

        }

        binding.loginBttRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.loginBttFgtPassword.setOnClickListener {
            showResetPasswordDialog()
        }

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun showResetPasswordDialog(){

        val dialogbinding = ActivityDialogResetPasswordBinding.inflate(LayoutInflater.from(this))

        val dialog = AlertDialog.Builder(this)
            .setView(dialogbinding.root)
            .setNegativeButton ("Cancelar"){ dialog, _ -> dialog.dismiss() }
            .create()


        dialogbinding.drpBttEnviar.setOnClickListener {

            val email = dialogbinding.drpEtxtEmail.text.toString().trim()

            if (email.isNotEmpty()){
                sendPasswordRecoverEmail(email)
                dialog.dismiss()
            }
            else{
                invokeSnackbar(dialogbinding.root, "Preencha o Email!")
            }

        }

        dialog.show()
    }

    private fun sendPasswordRecoverEmail(email: String){

        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                val message = if (task.isSuccessful){
                    "Email enviado para $email"
                }
                else{
                    "Erro: ${task.exception?.message ?: "Falha desconhecida"}"
                }

                invokeSnackbar(binding.root, message)

            }


    }


    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut()
    }
}