package com.app.mobiletestsuitmedia.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.mobiletestsuitmedia.databinding.ActivityFirstScreenBinding
import com.app.mobiletestsuitmedia.ui.ViewModelFactory
import com.app.mobiletestsuitmedia.ui.viewmodel.FirstScreenViewModel

class FirstScreen : AppCompatActivity() {
    private lateinit var binding: ActivityFirstScreenBinding
    private val viewModel by viewModels<FirstScreenViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnCheck.setOnClickListener {
                checkPalindrome()
            }

            btnNext.setOnClickListener {
                checkName()
            }
        }

    }

    private fun checkPalindrome(){
        binding.apply {
            val word = etPalindrome.text.toString()
            val wordOri = word.split("").toTypedArray()
            val wordReverse = wordOri.reversedArray()
            val reversedWord = concatReversedWord(wordReverse)

            if (word.isEmpty()) {
                Toast.makeText(this@FirstScreen, "It's Empty", Toast.LENGTH_SHORT).show()
            }else if (reversedWord == word){
                Toast.makeText(this@FirstScreen, "It's Palindrome", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this@FirstScreen, "No Palindrome", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkName(){
        binding.apply {
            val name = etName.text.toString()
            if(name.isEmpty()) {
                Toast.makeText(this@FirstScreen, "Fill Name First", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.saveUser(name)
                val intent = Intent(this@FirstScreen, SecondScreen::class.java)
                startActivity(intent)
            }
        }

    }

    private fun concatReversedWord(arr: Array<String>): String {
        var index = 0
        val size  = arr.size
        var reversedWord = ""
        while (index < size) {
            reversedWord += arr[index]
            index++
        }
        return reversedWord
    }
}