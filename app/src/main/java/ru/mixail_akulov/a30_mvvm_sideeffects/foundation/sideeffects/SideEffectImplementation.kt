package ru.mixail_akulov.a30_mvvm_sideeffects.foundation.sideeffects

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Базовый класс для реализации побочных эффектов.
 * Реализации отвечают за реальную реализацию побочных эффектов с учетом жизненного цикла активности.
 * Здесь можно спокойно запускать диалоги, осуществлять навигацию, изменять интерфейс.
 * Реализации привязаны к активности.
 */
abstract class SideEffectImplementation {

    private lateinit var activity: AppCompatActivity

    fun requireActivity(): AppCompatActivity = activity

    open fun onCreate(savedInstanceState: Bundle?) {}
    open fun onBackPressed(): Boolean { return false }
    open fun onRequestUpdates() {}
    open fun onSupportNavigateUp(): Boolean? {
        return null
    }
    open fun onSaveInstanceState(outBundle: Bundle) {}
    open fun onActivityResult(requestCode: Int, responseCode: Int, data: Intent?) {}
    open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, granted: IntArray) {}

    internal fun injectActivity(activity: AppCompatActivity) {
        this.activity = activity
    }
}