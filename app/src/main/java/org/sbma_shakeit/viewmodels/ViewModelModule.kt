package org.sbma_shakeit.viewmodels

import android.app.Activity
import android.app.Application
import org.sbma_shakeit.data.room.ShakeItDB
import org.sbma_shakeit.sensors.SensorModule

object ViewModelModule {

    private var longShakeViewModel: LongShakeViewModel? = null

    fun provideLongShakeViewModel(app: Application, activity: Activity, database: ShakeItDB): LongShakeViewModel{
        if(longShakeViewModel == null){
            val shakeSensor = SensorModule.provideShakeSensor(app)
            longShakeViewModel = LongShakeViewModel(activity, database, shakeSensor)
        }
        return longShakeViewModel!!
    }
}