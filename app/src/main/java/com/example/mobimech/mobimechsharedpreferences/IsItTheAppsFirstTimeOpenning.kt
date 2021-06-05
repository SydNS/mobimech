package com.example.mobimech.mobimechsharedpreferences

import android.content.Context

class IsItTheAppsFirstTimeOpenning(val context: Context) {
    val sharedPreferences =
        context.getSharedPreferences("PREF_FILE", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()


    fun writeInstalled() {
        val editor = sharedPreferences!!.edit()
        editor.putString("ALREADY_INSTALLED", "YES")
        editor.apply()
    }

    fun checkingInstalled(): Boolean {
        val status = sharedPreferences!!.getString("ALREADY_INSTALLED", "null") != "null"
        return status
    }

    fun eraseInstalled() {
        val editor = sharedPreferences!!.edit()
        editor.clear()
        editor.apply()
    }


}