package ca.antonious.materialdaypicker

import android.widget.TextView
import com.shopify.testify.ScreenshotRule
import com.shopify.testify.annotation.ScreenshotInstrumentation
import com.shopify.testify.annotation.TestifyLayout
import java.util.Locale
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class LocaleTests(private val locale: Locale) {
    @get:Rule
    var rule = ScreenshotRule(TestHarnessActivity::class.java, TestHarnessActivity.CONTENT_VIEW_ID)

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun initParameters(): List<Locale> {
            return listOf(
                Locale.ENGLISH,
                Locale.FRANCE,
                Locale.CHINESE,
                Locale.JAPANESE,
                Locale("fil"),
                Locale("hi"),
                Locale("es")
            )
        }
    }

    @Test
    @ScreenshotInstrumentation
    @TestifyLayout(R.layout.material_day_picker_test)
    fun testLocale() {
        rule.setViewModifications { harnessRoot ->
            val materialDayPicker = harnessRoot.findViewById(R.id.day_picker) as MaterialDayPicker
            val labelTextView = harnessRoot.findViewById<TextView>(R.id.test_label)
            labelTextView.text = "Locale test: ${locale.displayName}"
            materialDayPicker.locale = locale
            materialDayPicker.setSelectedDays(
                MaterialDayPicker.Weekday.MONDAY,
                MaterialDayPicker.Weekday.FRIDAY
            )
        }
        .setEspressoActions {
            Thread.sleep(500)
        }
        .assertSame()
    }
}
