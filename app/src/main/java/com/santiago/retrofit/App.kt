package com.santiago.retrofit

import android.app.Application
import com.santiago.retrofit.data.local.ProductDatabase

class App : Application() {
    val database: ProductDatabase by lazy { ProductDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
    }
} 