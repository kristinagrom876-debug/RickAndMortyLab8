package com.example.rickandmorty

import android.app.Application
import com.example.rickandmorty.data.AppContainer
import com.example.rickandmorty.data.DefaultAppContainer

/** Листинг 8.84: контейнер зависимостей создаётся при запуске приложения. */
class RickAndMortyApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
