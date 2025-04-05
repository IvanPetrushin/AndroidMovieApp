package ru.fintech

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ru.fintech.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private var binding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())

        binding!!.loginBtn.setOnClickListener {
            if (binding!!.emailEt.text.toString().isEmpty() || binding!!.passwordEt.text.toString().isEmpty()) {
                Toast.makeText(applicationContext, "Логин и пароль не могут быть пустые", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding!!.emailEt.text.toString(),
                    binding!!.passwordEt.text.toString()
                ).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        } else {
                            Toast.makeText(applicationContext, "Неправильные логин и пароль", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        binding!!.goToRegisterActivityTv.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }
}