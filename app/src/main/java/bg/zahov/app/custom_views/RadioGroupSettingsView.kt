package bg.zahov.app.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import bg.zahov.app.data.Language
import bg.zahov.app.data.Settings
import bg.zahov.app.data.Sound
import bg.zahov.app.data.Theme
import bg.zahov.app.data.Units
import bg.zahov.app.settings.SettingsViewModel
import bg.zahov.fitness.app.R
import com.google.android.material.textview.MaterialTextView


class RadioGroupSettingsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RelativeLayout(context, attrs, defStyle) {
    init {
        inflate(context, R.layout.radio_group_settings_view, this)
    }
    fun initViewInformation(title: String, radioOptions: List<String>, settings: Settings, settingsVM: SettingsViewModel) {
        val titleTextView: MaterialTextView = findViewById(R.id.titleTextView)
        val subtitleTextView: MaterialTextView = findViewById(R.id.subtitleTextView)

        when (title) {
            "Language" -> subtitleTextView.text = settings.language.name
            "Weight" -> subtitleTextView.text = settings.weight.name
            "Distance" -> subtitleTextView.text = settings.distance.name
            "Timer increment value" -> subtitleTextView.text = settings.restTimer.toString()
            "Sound" -> subtitleTextView.text = settings.soundSettings.name
            "Theme" -> subtitleTextView.text = settings.theme.name

        }

        titleTextView.text = title

        setOnClickListener {
            showPopupWindow(title, radioOptions, settings, settingsVM, subtitleTextView)
        }
    }
    private fun showPopupWindow(title: String, radioOptions: List<String>, settings: Settings, settingsVM: SettingsViewModel, subtitleTextView: MaterialTextView) {
        val popupView: View = LayoutInflater.from(context).inflate(R.layout.settings_popup, null)
        val popupTitleTextView: MaterialTextView = popupView.findViewById(R.id.popupTitleTextView)
        val radioGroup: RadioGroup = popupView.findViewById(R.id.radioGroup)

        popupTitleTextView.text = title

        radioOptions.forEachIndexed { index, item ->
            val radioButton = RadioButton(context)
            val currOption = radioOptions[index]
            radioButton.text = currOption
            radioButton.layoutParams = RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT)
            radioGroup.addView(radioButton)

            if (currOption == getSelectedOption(title, settings)) {
                radioButton.isChecked = true
            }
        }
        radioGroup.setOnCheckedChangeListener { _, index ->
            val radioButton = radioGroup.findViewById<RadioButton>(index)
            val selectedOption = radioButton.text.toString()

            when (title) {
                "Language" -> Language.valueOf(selectedOption)
                "Weight", "Distance" -> Units.valueOf(selectedOption)
                "Timer increment value" -> selectedOption.split(" ").first().toInt()
                "Sound" -> Sound.valueOf(selectedOption)
                "Theme" -> Theme.valueOf(selectedOption)
                else -> null
            } ?.let {
                settingsVM.writeNewSetting(title, it)
                settingsVM.refreshSettings()
                subtitleTextView.text = when (title) {
                    "Language" -> (it as Language).name
                    "Weight", "Distance" -> (it as Units).name
                    "Timer increment value" -> "$it s"
                    "Sound" -> (it as Sound).name
                    "Theme" -> (it as Theme).name
                    else -> null
                }
            }
        }

        val popupWindow = PopupWindow(popupView, resources.getDimension(R.dimen.popup_width).toInt(), resources.getDimension(R.dimen.popup_height).toInt(), true)
        popupWindow.showAtLocation(this, Gravity.CENTER, 0, 0)
    }
    private fun getSelectedOption(title: String, settings: Settings): String? {
        return when (title) {
            "Language" -> settings.language.name
            "Weight" -> settings.weight.name
            "Distance" -> settings.distance.name
            "Timer increment value" -> "${settings.restTimer} s"
            "Sound" -> settings.soundSettings.name
            "Theme" -> settings.theme.name
            else -> null
        }
    }
}

