package com.rose.myfitnesslifestyle

import android.app.Application
import com.rose.myfitnesslifestyle.modelBD.AppDataBase

class App : Application() {

    lateinit var db: AppDataBase

    override fun onCreate() {
        super.onCreate()
        db = AppDataBase.getDataBase(this)
    }
}