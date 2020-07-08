package com.fair.myfirstlibrary

import android.content.Context
import android.widget.Toast

class testphase1 {

    fun Context.toast(message: String){

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()


    }
}