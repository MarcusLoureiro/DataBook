@file:Suppress("DEPRECATION")

package com.example.databook.livro

import android.content.Context
import android.util.Log
import com.example.databook.R
import java.io.File

object Common {

//    fun getAppPath(context: Context, dirName: String, subDir: String): String {
//        var file: File
//        if (subDir != null) {
//            file = File(context.getExternalFilesDir(null), "/$dirName/$subDir")
//            Log.i("FILE IF", file.toString())
//        }else {
//            file = File(context.getExternalFilesDir(null), "/$dirName")
//            Log.i("FILE ELSE", file.toString())
//        }
//
//        if(!file.exists()){
//            file.mkdir()
//            Log.i("FILE EXISTS", file.toString())
//    }
//        Log.i("FILE", file.toString())
//        return file.toString()
//
//}

fun getAppPath(context: Context): String {
    val dir = File(
        android.os.Environment.DIRECTORY_DOCUMENTS.toString()
                + File.separator
                + context.resources.getString(R.string.app_name)
                + File.separator
    )
    if (!dir.exists())
        dir.mkdir()
    Log.i("PDF", dir.mkdir().toString())
    return dir.path + File.separator
}
}