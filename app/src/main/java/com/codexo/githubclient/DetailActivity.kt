package com.codexo.githubclient

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codexo.githubclient.databinding.ActivityDetailBinding
import com.codexo.githubclient.home.HomeFragment
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupWebView(savedInstanceState)
        binding!!.swipeRefresh.setOnRefreshListener { refreshPage() }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        val urlExtra = bundle?.getString(CLASS_TAG).toString()
        binding!!.wvDetail.apply {
            webViewClient = MyWVClient()

            if (bundle?.getString(CLASS_TAG) != null) {
                loadUrl(urlExtra)
            } else onBackPressed()

            if (savedInstanceState != null) {
                restoreState(savedInstanceState)
            } else {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    setSupportMultipleWindows(true)
                    cacheMode = WebSettings.LOAD_DEFAULT
                    allowFileAccess = true
                }

                scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        if (newProgress < 80) {
                            binding!!.progressBar.visibility = View.VISIBLE
                            binding!!.wvDetail.visibility = View.GONE
                        }
                        if (newProgress >= 80) {
                            visibility = View.VISIBLE
                            binding!!.progressBar.visibility = View.GONE
                            binding!!.swipeRefresh.isRefreshing = false
                        } else {
                            binding!!.progressBar.visibility = View.VISIBLE
                        }
                    }
                }

            }
        }
    }

    private fun refreshPage() {
        binding!!.wvDetail.apply {
            webViewClient = MyWVClient()
            loadUrl(binding!!.wvDetail.url.toString())
        }
    }

    override fun onBackPressed() {
        if (binding!!.wvDetail.canGoBack()) {
            binding!!.wvDetail.goBack()
        } else super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding!!.wvDetail.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding!!.wvDetail.restoreState(savedInstanceState)
    }

    companion object {
        private val targetUrl =
            listOf("www.github.com", "www.github.com/", "github.com", "github.com/")
        private val CLASS_TAG = HomeFragment::class.java.simpleName.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun reloadActivity(url: String) {
        val i = Intent(this, DetailActivity::class.java)
        i.putExtra(CLASS_TAG, url)
        startActivity(i)
//        finish()
    }

    inner class MyWVClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

            if (Uri.parse(url).host in targetUrl) {
//                binding!!.wvDetail.loadUrl(url)
                reloadActivity(url)
                Toast.makeText(this@DetailActivity, Uri.parse(url).host, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@DetailActivity, url, Toast.LENGTH_LONG).show()
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
            return true
        }
    }
}