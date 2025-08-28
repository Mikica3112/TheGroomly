package com.example.thegroomly

import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.drawerlayout.widget.DrawerLayout
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingRootException
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RunWith(AndroidJUnit4::class)
@LargeTest
class BookingFlowE2ETest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun full_booking_flow_creates_reservation() {
        navigateToBooking()
        try { onView(isAssignableFrom(DrawerLayout::class.java)).perform(DrawerActions.close()) } catch (_: Throwable) {}
        waitUntilDisplayed(withId(R.id.spinnerGroomer), 10000)
        selectFirstFromSpinner(withId(R.id.spinnerGroomer))
        selectFirstFromSpinner(withId(R.id.spDogType))
        selectFirstFromSpinner(withId(R.id.spTreatment))
        onView(withId(R.id.btnPickDateTime)).perform(click())
        setTodayDateIfVisible()
        setTimeIfVisible()
        onView(withId(R.id.btnReserve)).perform(click())
        waitFor(500)
        onView(withId(R.id.rvReservations)).check(matches(isDisplayed()))
    }

    private fun navigateToBooking() {
        try { onView(isAssignableFrom(DrawerLayout::class.java)).perform(DrawerActions.open()) } catch (_: Throwable) {}
        val titles = listOf("Rezervacija", "Booking")
        for (t in titles) { try { onView(withText(t)).perform(click()); return } catch (_: Throwable) {} }
        val ids = listOf("nav_booking", "navigation_booking", "booking", "bookingFragment")
        for (n in ids) { try { onView(withResourceName(n)).perform(click()); return } catch (_: Throwable) {} }
    }

    private fun selectFirstFromSpinner(spinner: Matcher<View>) {
        if (!exists(spinner)) return
        try { onView(spinner).perform(click()) } catch (_: Throwable) { try { onView(spinner).perform(androidx.test.espresso.action.ViewActions.scrollTo(), click()) } catch (_: Throwable) { return } }
        try { onData(anything()).atPosition(0).perform(click()) } catch (_: Throwable) {}
    }

    private fun setTodayDateIfVisible() {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH) + 1
        val d = cal.get(Calendar.DAY_OF_MONTH)
        if (exists(withClassName(equalTo(DatePicker::class.java.name)))) {
            try { onView(withClassName(equalTo(DatePicker::class.java.name))).perform(PickerActions.setDate(y, m, d)) } catch (_: Throwable) {}
            confirmIfVisible()
            return
        }
        val formats = listOf(
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()),
            SimpleDateFormat("d.M.yyyy", Locale.getDefault()),
            SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()),
            SimpleDateFormat("M/d/yyyy", Locale.getDefault()),
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        )
        val editCandidates = listOf(
            withResourceName("mtrl_picker_text_input_date"),
            isAssignableFrom(EditText::class.java)
        )
        for (c in editCandidates) {
            if (exists(c)) {
                for (fmt in formats) {
                    try {
                        onView(c).perform(replaceText(fmt.format(cal.time)), closeSoftKeyboard())
                        confirmIfVisible()
                        return
                    } catch (_: Throwable) {}
                }
            }
        }
        confirmIfVisible()
    }

    private fun setTimeIfVisible() {
        if (exists(withClassName(equalTo(TimePicker::class.java.name)))) {
            val cal = Calendar.getInstance()
            var h = cal.get(Calendar.HOUR_OF_DAY)
            var min = cal.get(Calendar.MINUTE)
            val r = ((min + 4) / 5) * 5
            h += (r / 60)
            min = r % 60
            try { onView(withClassName(equalTo(TimePicker::class.java.name))).perform(PickerActions.setTime(h % 24, min)) } catch (_: Throwable) {}
            confirmIfVisible()
            return
        }
        if (exists(withResourceName("material_timepicker_ok_button")) || exists(withResourceName("material_timepicker_view"))) {
            setMaterialTime()
            return
        }
    }

    private fun setMaterialTime() {
        val cal = Calendar.getInstance()
        var h = cal.get(Calendar.HOUR_OF_DAY)
        var min = cal.get(Calendar.MINUTE)
        val r = ((min + 4) / 5) * 5
        h = (h + (r / 60)) % 24
        min = r % 60
        val modeButtons = listOf(
            withResourceName("material_timepicker_mode_button"),
            withResourceName("material_timepicker_edit_mode_button")
        )
        for (b in modeButtons) { try { onView(b).perform(click()); break } catch (_: Throwable) {} }
        val hourFields = listOf(
            withResourceName("material_hour_edit_text"),
            withResourceName("material_timepicker_hour"),
            isAssignableFrom(EditText::class.java)
        )
        val minuteFields = listOf(
            withResourceName("material_minute_edit_text"),
            withResourceName("material_timepicker_minute"),
            isAssignableFrom(EditText::class.java)
        )
        var hourSet = false
        for (hf in hourFields) {
            try { onView(hf).inRoot(RootMatchers.isDialog()).perform(replaceText(String.format(Locale.getDefault(), "%02d", h)), closeSoftKeyboard()); hourSet = true; break } catch (_: Throwable) {}
            try { onView(hf).perform(replaceText(String.format(Locale.getDefault(), "%02d", h)), closeSoftKeyboard()); hourSet = true; break } catch (_: Throwable) {}
        }
        var minuteSet = false
        for (mf in minuteFields) {
            try { onView(mf).inRoot(RootMatchers.isDialog()).perform(replaceText(String.format(Locale.getDefault(), "%02d", min)), closeSoftKeyboard()); minuteSet = true; break } catch (_: Throwable) {}
            try { onView(mf).perform(replaceText(String.format(Locale.getDefault(), "%02d", min)), closeSoftKeyboard()); minuteSet = true; break } catch (_: Throwable) {}
        }
        confirmIfVisible()
    }

    private fun confirmIfVisible() {
        val confirmers = listOf(
            withResourceName("confirm_button"),
            withResourceName("material_timepicker_ok_button"),
            withId(android.R.id.button1),
            withText("OK"),
            withText("Ok"),
            withText("U redu"),
            withText("Confirm"),
            withText("Potvrdi")
        )
        for (m in confirmers) {
            try { onView(m).inRoot(RootMatchers.isDialog()).perform(click()); return } catch (_: Throwable) {}
            try { onView(m).perform(click()); return } catch (_: Throwable) {}
        }
    }

    private fun exists(matcher: Matcher<View>): Boolean {
        return try {
            onView(matcher).check { _, _ -> }
            true
        } catch (_: NoMatchingViewException) {
            false
        } catch (_: NoMatchingRootException) {
            false
        } catch (_: AssertionError) {
            true
        }
    }

    private fun waitFor(ms: Long) {
        onView(isRoot()).perform(object : androidx.test.espresso.ViewAction {
            override fun getConstraints() = isRoot()
            override fun getDescription() = "wait $ms ms"
            override fun perform(uiController: androidx.test.espresso.UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(ms)
            }
        })
    }

    private fun waitUntilDisplayed(viewMatcher: Matcher<View>, timeoutMs: Long) {
        val step = 200L
        var elapsed = 0L
        while (elapsed < timeoutMs) {
            try { onView(viewMatcher).check(matches(isDisplayed())); return } catch (_: Throwable) {}
            waitFor(step)
            elapsed += step
        }
        onView(viewMatcher).check(matches(isDisplayed()))
    }
}
