package com.social.alexanderpowell.spigotcasestudy.ui

import androidx.lifecycle.ViewModel
import com.social.alexanderpowell.spigotcasestudy.persistence.Device
import com.social.alexanderpowell.spigotcasestudy.persistence.DeviceDao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class DeviceViewModel(private val dataSource: DeviceDao) : ViewModel() {

    /*fun deviceId(): Flowable<String> {
        return dataSource.getDeviceById(USER_ID)
            .map { device -> device.id }
    }

    fun deviceManufacturer(): Flowable<String> {
        return dataSource.getDeviceById(USER_ID)
                .map { device -> device.manufacturer }
    }

    fun deviceModel(): Flowable<String> {
        return dataSource.getDeviceById(USER_ID)
                .map { device -> device.model }
    }*/

    fun updateDeviceData(deviceId: String, manufacturer: String, model: String, parameters: HashMap<String, String>): Completable {
        val device = Device(USER_ID, deviceId, manufacturer, model, parameters)
        return dataSource.insertDevice(device)
    }

    fun deviceData(): Single<Device> {
        return dataSource.getDeviceById(USER_ID)
    }

    companion object {
        // using a hardcoded value for simplicity
        const val USER_ID = "1"
    }

}