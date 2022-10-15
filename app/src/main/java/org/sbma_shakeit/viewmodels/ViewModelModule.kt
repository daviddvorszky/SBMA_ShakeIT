package org.sbma_shakeit.viewmodels

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import org.sbma_shakeit.data.room.ShakeItDB
import org.sbma_shakeit.sensors.SensorModule
import java.io.File

object ViewModelModule {

    private var longShakeViewModel: LongShakeViewModel? = null
    private var quickShakeViewModel: QuickShakeViewModel? = null
    private var violentShakeViewModel: ViolentShakeViewModel? = null
    private var showShakeViewModel: SingleShakeViewModel? = null

    fun provideLongShakeViewModel(app: Application, database: ShakeItDB, outputDirectory: File, fusedLocationClient: FusedLocationProviderClient): LongShakeViewModel{
        if(longShakeViewModel == null){
            val shakeSensor = SensorModule.provideShakeSensor(app)
            longShakeViewModel = LongShakeViewModel(database, outputDirectory, fusedLocationClient, app, shakeSensor)
        }
        return longShakeViewModel!!
    }

    fun provideQuickShakeViewModel(app: Application, database: ShakeItDB, outputDirectory: File, fusedLocationClient: FusedLocationProviderClient): QuickShakeViewModel{
        if(quickShakeViewModel == null){
            val shakeSensor = SensorModule.provideShakeSensor(app)
            quickShakeViewModel = QuickShakeViewModel(database, outputDirectory, fusedLocationClient, app, shakeSensor)
        }
        return quickShakeViewModel!!
    }

    fun provideViolentShakeViewModel(app: Application, database: ShakeItDB, outputDirectory: File, fusedLocationClient: FusedLocationProviderClient): ViolentShakeViewModel{
        if(violentShakeViewModel == null){
            val shakeSensor = SensorModule.provideShakeSensor(app)
            violentShakeViewModel = ViolentShakeViewModel(database, outputDirectory, fusedLocationClient, app, shakeSensor)
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