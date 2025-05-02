package com.example.prj_connectauth

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prj_connectauth.databinding.ActivityDialogResetPasswordBinding

import com.google.firebase.auth.FirebaseAuth
import com.example.prj_connectauth.databinding.ActivityLoginBinding
import com.example.prj_connectauth.utils.GlobalSnackbar.invokeSnackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

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
                invokeSnackbar(binding.root, "Preencha Todos os Campos!", anchor = binding.loginEtxtEmail)
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task ->
                    if (task.isSuccessful){
                        invokeSnackbar(binding.root, "Login Bem Sucedido!", "G")

                        lifecycleScope.launch {
                            delay(1000)
                        }

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    else{
                        invokeSnackbar(binding.root, "Erro: ${task.exception?.message}", "R")
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

        val dialog = AlertDialog.Builder(this, R.style.MY_DialogStyle)
            .setView(dialogbinding.root)
            //.setNegativeButton ("Cancelar"){ dialog, _ -> dialog.dismiss() }
            .create()

//        dialog.setOnShowListener {
//            val window = dialog.window
//
//            val screenWidth = resources.displayMetrics.widthPixels
//            val dialogWidth = (screenWidth * 0.90).toInt()
//            val maxWidth = 600.dpToPx()
//
//            val finalWidth = if(screenWidth > maxWidth) maxWidth else dialogWidth
//
//            window?.setLayout(finalWidth, WindowManager.LayoutParams.WRAP_CONTENT)
//
//
//
//        }

        dialogbinding.drpBttEnviar.setOnClickListener {

            val email = dialogbinding.drpEtxtEmail.text.toString().trim()

            if (email.isNotEmpty()){
                dialogbinding.drpTvError.text = ""
                sendPasswordRecoverEmail(email)
                dialog.dismiss()
            }
            else{
                dialogbinding.drpTvError.text = "Preencha o Email!"
            }

        }

        dialogbinding.drpBttCancelar.setOnClickListener {

            dialog.dismiss()

        }

//        dialogbinding.main.visibility = View.GONE
        dialog.show()

    }

    private fun sendPasswordRecoverEmail(email: String){

        var cor: String
        var message: String

        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->

                if (task.isSuccessful){
                    message = "Email enviado para $email"
                    cor = "G"
                }
                else{
                    message = "Erro: ${task.exception?.message}"
                    cor = "R"
                }

                invokeSnackbar(binding.root, message, cor)

            }


    }


    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut()
    }
}