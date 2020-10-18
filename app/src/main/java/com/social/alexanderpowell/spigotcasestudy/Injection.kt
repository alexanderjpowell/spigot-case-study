package com.social.alexanderpowell.spigotcasestudy

import android.content.Context
import com.social.alexanderpowell.spigotcasestudy.persistence.DeviceDao
import com.social.alexanderpowell.spigotcasestudy.persistence.DevicesDatabase
import com.social.alexanderpowell.spigotcasestudy.ui.ViewModelFactory

object Injection {

    fun provideDeviceDataSource(context: Context): DeviceDao {
        val database = DevicesDatabase.getInstance(context)
        return database.deviceDao()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSource = provideDeviceDataSource(context)
        return ViewModelFactory(dataSource)
    }

}