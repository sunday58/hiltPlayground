package com.app.hiltplayground.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.app.hiltplayground.R
import com.app.hiltplayground.databinding.ActivityMainBinding
import com.app.hiltplayground.model.Blog
import com.app.hiltplayground.utils.DataState
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG: String = "AppDebug"
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.GetBlogEvents)
    }




    private fun subscribeObservers(){
        viewModel.dataState.observe(this, { dataState ->
            run {
                when (dataState) {
                    is DataState.success<List<Blog>> -> {
                        displayProgressBar(false)
                        appendBlogTitle(dataState.data)
                    }
                    is DataState.Error -> {
                        displayProgressBar(false)
                        displayError(dataState.exception.message)
                    }

                    is DataState.Loading -> {
                        displayProgressBar(true)
                    }
                }
            }
        })
    }

    private fun displayError(message: String?){
        if (message != null){
           binding.textView.text = message
        }else{
            binding.textView.text = "Unknown error"
        }
    }

    private fun displayProgressBar(isDisplay: Boolean){
         binding.progressBar.visibility = if (isDisplay) View.VISIBLE else View.GONE
    }

    private fun appendBlogTitle(blogs: List<Blog>){
        val sb = StringBuilder()
        for (blog in blogs){
            sb.append(blog.title + "\n")
        }
        binding.textView.text = sb.toString()
    }
}