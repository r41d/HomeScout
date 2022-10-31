package android.example.homescout

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment

class PermissionAppIntro : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        isIndicatorEnabled = true
        isWizardMode = true
        setImmersiveMode()
        setTheme(R.style.Theme_HomeScout)
        addSlide(
            AppIntroFragment.createInstance(
                title = "Location Permissions",
                description = "Ask for the locations",
                titleTypefaceFontRes = R.font.roboto_bold,
                descriptionTypefaceFontRes = R.font.roboto,
                imageDrawable = R.drawable.ic_location_on_onboarding,
                backgroundColorRes = R.color.purple_500
            ))
        addSlide(AppIntroFragment.createInstance(
            title = "Bluetooth Permissions",
            description = "Ask for Bluetooth",
            titleTypefaceFontRes = R.font.roboto_bold,
            descriptionTypefaceFontRes = R.font.roboto,
            imageDrawable = R.drawable.ic_bluetooth_onboarding,
            backgroundColorRes = R.color.purple_500
        ))
        addSlide(AppIntroFragment.createInstance(
            title = "Thanks.",
            description = "I hope this app suits you :-)",
            titleTypefaceFontRes = R.font.roboto_bold,
            descriptionTypefaceFontRes = R.font.roboto,
            imageDrawable = R.drawable.ic_thumb_up_onboarding,
            backgroundColorRes = R.color.purple_500
        ))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        finish()
    }

}