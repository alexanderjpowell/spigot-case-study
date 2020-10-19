package com.social.alexanderpowell.spigotcasestudy.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DeviceDao {

    @Query("SELECT * FROM Devices WHERE id = :id")
    fun getDeviceById(id: String): Single<Device>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDevice(user: Device): Completable

    @Query("DELETE FROM Devices")
    fun deleteAllDevices()

}