package br.com.commons

import android.content.Intent

object SampleNavigation: DynamicFeature<Intent> {

    const val HOME_ACTIVITY = "br.com.main.modules.home.activities.HomeActivity"
    const val MAIN_ACTIVITY = "br.com.main.modules.main.activities.MainActivity"
    const val CREATE_CAMP_ACTIVITY = "br.com.main.modules.main.activities.CreateCampActivity"

    override val dynamicStart: Intent?
        get() = HOME_ACTIVITY.loadIntentOrNull()

    fun navigateActivity(destination: String): Intent? =
            destination.loadIntentOrNull()

    fun navigateActivityClearTask(destination: String): Intent? =
            destination.loadIntentOrNull()?.apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
}