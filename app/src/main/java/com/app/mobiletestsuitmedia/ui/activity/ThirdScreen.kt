package com.app.mobiletestsuitmedia.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.mobiletestsuitmedia.data.response.DataItem
import com.app.mobiletestsuitmedia.databinding.ActivityThirdScreenBinding
import com.app.mobiletestsuitmedia.ui.ViewModelFactory
import com.app.mobiletestsuitmedia.ui.adapter.LoadingStateAdapter
import com.app.mobiletestsuitmedia.ui.adapter.OnUserClickListener
import com.app.mobiletestsuitmedia.ui.adapter.UserAdapter
import com.app.mobiletestsuitmedia.ui.viewmodel.ThirdScreenViewModel

class ThirdScreen : AppCompatActivity(), OnUserClickListener {
    private lateinit var binding: ActivityThirdScreenBinding
    private lateinit var userAdapter: UserAdapter
    private val viewModel by viewModels<ThirdScreenViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showUser()

        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }
        onBackPressedDispatcher.addCallback(this){
            val intent = Intent(this@ThirdScreen, SecondScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        setupSwipeRefresh()
    }

    private fun showUser() {
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(this)
        binding.rvUsers.adapter = userAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                userAdapter.retry()
            }
        )

        viewModel.user.observe(this) { pagingData ->
            userAdapter.submitData(lifecycle, pagingData)
        }

        userAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                showLoading(true)
            } else {
                showLoading(false)
            }
        }

        viewModel.isEmpty.observe(this) { isEmpty ->
            if (isEmpty) {
                Toast.makeText(this, "No Data Available", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            userAdapter.refresh()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onUserClick(user: DataItem) {
        val username = "${user.firstName} ${user.lastName}"
        Log.d("Selected User", username)
        viewModel.saveUsername(username)
        val intent = Intent(this, SecondScreen::class.java)
        startActivity(intent)
    }
}