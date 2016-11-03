package com.example.pedrofialho.myweatherapp

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.core.deps.guava.base.Strings
import android.support.test.runner.AndroidJUnit4
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Instrumentation test, which will execute on an Android device.

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class CheckAPIResponsiveness {
    private var requestQueue: RequestQueue? = null

    // Synchronization between test harness thread and callbacks thread
    private var latch: CountDownLatch? = null
    private var error: AssertionError? = null

    private fun waitForCompletion() {
        try {
            if (latch!!.await(60, TimeUnit.SECONDS)) {
                if (error != null)
                    throw error as AssertionError
            } else {
                Assert.fail("Test harness thread timeout while waiting for completion")
            }
        } catch (_: InterruptedException) {
            Assert.fail()
        }

    }

    private fun executeAndPublishResult(assertions:() -> Unit) {
        try {
            assertions
        } catch (error: AssertionError) {
            this.error = error
        } finally {
            latch!!.countDown()
        }
    }


    @Before
    fun prepare() {
        // Preparing Volley's request queue
        requestQueue = Volley.newRequestQueue(InstrumentationRegistry.getTargetContext())
        requestQueue!!.cache.clear()
        // Preparing test harness thread synchronization artifacts
        latch = CountDownLatch(1)
        error = null
    }

    @Test
    fun test_ApiResponsiveness() {
        requestQueue!!.add(
                StringRequest(Request.Method.GET, WEATHER_URL,
                        Response.Listener<String> { response -> executeAndPublishResult {
                            Assert.assertFalse(Strings.isNullOrEmpty(response)) }},
                        Response.ErrorListener { error -> executeAndPublishResult( {
                            Assert.assertNotNull(error.networkResponse) }) }))

        waitForCompletion()
    }
}
