package org.sbma_shakeit.viewmodels

import android.app.Activity
import android.app.Application
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.room.ShakeItDB
import org.sbma_shakeit.sensors.SensorModule

object ViewModelModule {

    private var longShakeViewModel: LongShakeViewModel? = null
    private var quickShakeViewModel: QuickShakeViewModel? = null
    private var violentShakeViewModel: ViolentShakeViewModel? = null
    private var showShakeViewModel: SingleShakeViewModel? = null

    fun provideLongShakeViewModel(app: Application, activity: Activity, database: ShakeItDB): LongShakeViewModel{
        if(longShakeViewModel == null){
            val shakeSensor = SensorModule.provideShakeSensor(app)
            longShakeViewModel = LongShakeViewModel(activity, database, shakeSensor)
        }
        return longShakeViewModel!!
    }

    fun provideQuickShakeViewModel(app: Application, activity: Activity, database: ShakeItDB): QuickShakeViewModel{
        if(quickShakeViewModel == null){
            val shakeSensor = SensorModule.provideShakeSensor(app)
            quickShakeViewModel = QuickShakeViewModel(activity, database, shakeSensor)
        }
        return quickShakeViewModel!!
    }

    fun provideViolentShakeViewModel(app: Application, activity: Activity, database: ShakeItDB): ViolentShakeViewModel{
        if(violentShakeViewModel == null){
            val shakeSensor = SensorModule.provideShakeSensor(app)
            violentShakeViewModel = ViolentShakeViewModel(activity, database, shakeSensor)
        }
        return violentShakeViewModel!!
    }

    fun provideShowShakeViewModel(): SingleShakeViewModel{
        if(showShakeViewModel == null){
            showShakeViewModel = SingleShakeViewModel()
        }
        return showShakeViewModel!!
    }
}