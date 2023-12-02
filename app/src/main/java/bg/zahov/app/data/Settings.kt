package bg.zahov.app.data


data class Settings(
    val language: Language = Language.English,
    val weight: Units = Units.Normal,
    val distance: Units = Units.Normal,
    val soundEffects: Boolean = true,
    val theme: Theme = Theme.Dark,
    val restTimer: Int = 30,
    val vibration: Boolean = true,
    val soundSettings: Sound = Sound.SOUND_1,
    val updateTemplate: Boolean = true,
    val sync: Boolean = true,
    val fit: Boolean = false
)