package pl.szkolaandroida.logintest

import android.content.Context
import androidx.annotation.StringRes

interface StringProvider {
    fun getString(@StringRes stringId: Int): String
}

class AndroidStringProvider(private val context: Context) : StringProvider {
    override fun getString(stringId: Int) = context.getString(stringId)
}
