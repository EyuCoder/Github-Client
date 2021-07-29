package com.codexo.githubclient.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.codexo.githubclient.DetailActivity
import com.codexo.githubclient.R
import com.codexo.githubclient.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        setupWebView(savedInstanceState)
        binding!!.swipeRefresh.setOnRefreshListener { refreshPage() }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(savedInstanceState: Bundle?) {
        binding!!.wvHome.webViewClient = MyWVClient()
        binding!!.wvHome.loadUrl(mainUrl)
        if (savedInstanceState != null) {
            binding!!.wvHome.restoreState(savedInstanceState)
        } else {
            binding!!.wvHome.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                setSupportMultipleWindows(true)
                cacheMode = WebSettings.LOAD_DEFAULT
                allowFileAccess = true
            }

            binding!!.wvHome.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            binding!!.wvHome.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    if (newProgress < 80) {
                        binding!!.progressBar.visibility = View.VISIBLE
                        binding!!.wvHome.visibility = View.GONE
                    }
                    if (newProgress >= 80) {
                        binding!!.wvHome.visibility = View.VISIBLE
                        binding!!.progressBar.visibility = View.GONE
                        binding!!.swipeRefresh.isRefreshing = false
                    } else {
                        binding!!.progressBar.visibility = View.VISIBLE
                    }
                }
            }

        }
    }

    private fun refreshPage() {
        binding!!.wvHome.apply {
            webViewClient = MyWVClient()
            loadUrl(binding!!.wvHome.url.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding!!.wvHome.saveState(outState)
    }

    companion object {
        private const val mainUrl = "https://github.com/login"
        private val targetUrl =
            listOf("www.github.com", "www.github.com/", "github.com", "github.com/")
        private val CLASS_TAG = HomeFragment::class.java.simpleName.toString()
    }

    private fun openDetailActivity(url: String) {
        val i = Intent(activity, DetailActivity::class.java)
        i.putExtra(CLASS_TAG, url)
        startActivity(i)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    inner class MyWVClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

            if (Uri.parse(url).host in targetUrl) {
                binding!!.wvHome.loadUrl(url)
//                openDetailActivity(url)
//                Toast.makeText(context, Uri.parse(url).host, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, url, Toast.LENGTH_LONG).show()
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
            return true
        }
    }
}