package com.example.databook.database

import android.content.Context
import androidx.room.*
import com.example.databook.database.favoritos.FavoritosDAO
import com.example.databook.database.favoritos.FavoritosEntity
import com.example.databook.database.perfil.PerfilDAO
import com.example.databook.database.perfil.PerfilEntity


@Database(
    entities = [FavoritosEntity::class, PerfilEntity::class],
    version = 1,
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
                ).fallbackToDestructiveMigration ().build ()
                INSTANCE = instance
                return instance
            }
        }
    }
}