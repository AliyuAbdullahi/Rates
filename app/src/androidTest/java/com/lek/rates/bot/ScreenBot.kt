package com.lek.rates.bot

import android.app.Activity
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.lek.rates.helper.nthChildOf
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf

@Suppress("UNCHECKED_CAST")
abstract class ScreenBot<T : ScreenBot<T>> {
    private var activityContext: Activity? = null

    fun checkIsDisplayed(@IdRes vararg viewIds: Int): T {
        for (viewId in viewIds) {
            onView(withId(viewId)).check(matches(isDisplayed()))
        }
        return this as T
    }

    fun checkIsDisplayedWithTag(tag: String): T {
        onView(withTagValue(Matchers.`is`(tag))).check(matches(isDisplayed()))
        return this as T
    }

    fun checkViewHasChildAtIndex(viewId: Int, parentId: Int, childIndex: Int, text: String): T {
        onView(
            allOf(
                withId(viewId),
                isDescendantOfA(nthChildOf(withId(parentId), childIndex))
            )
        ).check(matches(withText(text)))
        return this as T
    }

    fun checkChildAtPositionEdited(viewId: Int, parentId: Int, childIndex: Int, text: String): T {
        onView(
            allOf(
                withId(viewId),
                isDescendantOfA(nthChildOf(withId(parentId), childIndex))
            )
        ).perform(clearText(), typeText(text))
        return this as T
    }

    fun checkCildAtPostionHasValue(viewId: Int, parentId: Int, childIndex: Int, text: String): T {
        onView(
            allOf(
                withId(viewId),
                isDescendantOfA(nthChildOf(withId(parentId), childIndex))
            )
        ).check(matches(withText(text)))
        return this as T
    }

    fun closeKeyboard(): T {
        Espresso.closeSoftKeyboard()
        return this as T
    }

    fun clickOnViewWithTag(tag: String): T {
        onView(withTagValue(Matchers.`is`(tag))).perform(click())
        return this as T
    }

    companion object {

        fun <T : ScreenBot<*>> withBot(botClass: Class<T>?): T {
            if (botClass == null) {
                throw IllegalArgumentException("instance class == null")
            }

            try {
                return botClass.newInstance()
            } catch (iae: IllegalAccessException) {
                throw RuntimeException("IllegalAccessException", iae)
            } catch (ie: InstantiationException) {
                throw RuntimeException("InstantiationException", ie)
            }
        }
    }
}
