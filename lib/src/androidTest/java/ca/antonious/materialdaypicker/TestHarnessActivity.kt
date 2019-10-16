package ca.antonious.materialdaypicker

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.test.rule.ActivityTestRule
import com.shopify.testify.ScreenshotRule

/**
 * The TestHarnessActivity is used as scaffolding for testing arbitrary views.
 * Testify's [ScreenshotRule] is a subclass of [ActivityTestRule] which means that an Activity
 * is required to "host" an UI that you wish to capture in your screenshot.
 * This empty activity can be used as a generic container for testing your custom [View] classes.
 */
class TestHarnessActivity : AppCompatActivity() {

    companion object {
        const val CONTENT_VIEW_ID = 12232312
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setBackgroundColor(Color.WHITE)
            id = CONTENT_VIEW_ID
        })
    }
}
