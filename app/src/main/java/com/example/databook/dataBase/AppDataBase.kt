package com.example.dataBase

import android.content.Context
import androidx.room.*
import com.example.databook.dataBase.Converters
import com.example.databook.dataBase.Favoritos.FavoritosDAO
import com.example.databook.dataBase.Favoritos.FavoritosEntity
import com.example.databook.dataBase.Perfil.PerfilDAO
import com.example.databook.dataBase.Perfil.PerfilEntity


@Database(
    entities = [FavoritosEntity::class, PerfilEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun favoritosDAO(): FavoritosDAO
    abstract fun perfisDAO(): PerfilDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDataBase(context: Context): AppDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "DataBookDataBase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}