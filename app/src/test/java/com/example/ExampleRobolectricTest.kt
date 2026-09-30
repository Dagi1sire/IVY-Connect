package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.IvySampleData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("IVY Parent Connect", appName)
  }

  @Test
  fun `verify initial state has clean slate without sample data`() {
    val announcements = IvySampleData.getInitialAnnouncements()
    assertTrue("Initial announcements should be empty per user request", announcements.isEmpty())

    val events = IvySampleData.getInitialEvents()
    assertTrue("Initial events should be empty per user request", events.isEmpty())
  }
}
