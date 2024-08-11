package com.app.mobiletestsuitmedia.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.mobiletestsuitmedia.databinding.ActivitySecondScreenBinding
import com.app.mobiletestsuitmedia.ui.ViewModelFactory
import com.app.mobiletestsuitmedia.ui.viewmodel.SecondScreenViewModel

class SecondScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySecondScreenBinding
    private val viewModel by viewModels<SecondScreenViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.users.observe(this){
            binding.tvName.text = it
        }
        viewModel.usersname.observe(this){
            if (it.isNotEmpty()){
                binding.tvSelectedName.text = it
            }
        }

        binding.apply {
            btnChoose.setOnClickListener {
                val intent = Intent(this@SecondScreen, ThirdScreen::class.java)
                startActivity(intent)
            }
            ivBack.setOnClickListener {
                viewModel.clearAll()
                onBackPressedDispatcher.onBackPressed()
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this){
            viewModel.clearAll()
            val intent = Intent(this@SecondScreen, FirstScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }

}