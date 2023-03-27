package com.example.mywebview

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.SearchView
import com.example.mywebview.databinding.ActivityMainBinding

//Prueba
class MainActivity : AppCompatActivity() {

    private val BASE_URL = "https://www.google.com"
    private lateinit var binding: ActivityMainBinding
    private val SEARCH_PATH = "/search?q="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //REFRESH

        binding.swipeRefresh.setOnRefreshListener {
            binding.webView.reload()
        }

        //Search

        binding.searchView.setOnQueryTextListener( object :  SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {

                p0?.let {
                    if (URLUtil.isValidUrl(it)){
                        print("hola")
                        binding.webView.loadUrl(it)
                    }else{
                        binding.webView.loadUrl("$BASE_URL$SEARCH_PATH$it")
                    }
                } ?: run {
                    print("adios")
                }

                return false
            }
        })




        // WebView

        binding.webView.webChromeClient = object : WebChromeClient() {
            
        }
        binding.webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                binding.searchView.setQuery(url,false)
                binding.swipeRefresh.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                binding.swipeRefresh.isRefreshing = false
            }

        }

        val settings = binding.webView.settings
        settings.javaScriptEnabled = true

        binding.webView.loadUrl(BASE_URL)


    }

    override fun onBackPressed() {
        if(binding.webView.canGoBack()){
            binding.webView.goBack()
        }else {
            super.onBackPressed()        }
    }
}