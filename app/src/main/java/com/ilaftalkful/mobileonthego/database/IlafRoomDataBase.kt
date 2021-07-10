package com.ilaftalkful.mobileonthego.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class IlafRoomDataBase : RoomDatabase() {


    companion object {

        @Volatile
        private var INSTANCE: IlafRoomDataBase? = null

        fun getDatabase(context: Context): IlafRoomDataBase? {
            if (INSTANCE == null) {
                synchronized(IlafRoomDataBase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            IlafRoomDataBase::class.java, "ilaf_db"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}