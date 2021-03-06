package com.social.alexanderpowell.spigotcasestudy.ui

import android.net.UrlQuerySanitizer
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.social.alexanderpowell.spigotcasestudy.Injection
import com.social.alexanderpowell.spigotcasestudy.R
import com.social.alexanderpowell.spigotcasestudy.http.SingletonRequestQueue
import com.social.alexanderpowell.spigotcasestudy.persistence.Device
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URLDecoder


class MainActivity : AppCompatActivity() {

    private lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: DeviceViewModel by viewModels { viewModelFactory }
    private val disposable = CompositeDisposable()
    private lateinit var adapter: DeviceListAdapter
    private val requestTag = "HttpPostRequest"
    private val ROW_ID = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        adapter = DeviceListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModelFactory = Injection.provideViewModelFactory(this)
        get_button.setOnClickListener { parseUrlButtonClick() }
        post_button.setOnClickListener { postButtonClick() }
    }

    override fun onStart() {
        super.onStart()
        disposable.add(
            viewModel.deviceData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ adapter.setDevice(it) },
                    { error -> Log.e(TAG, "Unable to get data", error) })
        )
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
        SingletonRequestQueue.getInstance(this).cancelRequestQueue(requestTag)
    }

    private fun parseUrlButtonClick() {
        val url: String = url_edit_text.text.toString()
        //val url: String = "https://m.alltheapps.org/get/app?userId=B1C92850-8202-44AC-B514-1849569F37B6&implementationid=cl-and-erp&trafficSource=erp&userClass=20200101"
        //val url: String = "https%3A%2F%2Fm.alltheapps.org%2Fget%2Fapp%3FuserId%3DB1C92850-8202-44AC-B514-1849569F37B6%26implementationid%3Dcl-and-erp%26trafficSource%3Derp%26userClass%3D20200101"
        //val url: String = "https://test.com?name=alex";
        val decodedUrl = URLDecoder.decode(url, "utf-8")
        val deviceId: String = Settings.Secure.getString(
            baseContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        val paramsList: MutableList<UrlQuerySanitizer.ParameterValuePair> = UrlQuerySanitizer(decodedUrl).parameterList
        val params = HashMap<String, String>()
        paramsList.map {
            params.put(it.mParameter, it.mValue)
        }
        Log.d(TAG, params.toString())
        get_button.isEnabled = false
        disposable.add(
            viewModel.updateDeviceData(deviceId, manufacturer, model, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    adapter.setDevice(Device(ROW_ID, deviceId, manufacturer, model, params))
                    get_button.isEnabled = true
                },
                    { error -> Log.e(TAG, "Unable to update data", error) })
        )
    }

    private fun postButtonClick() {
        disposable.add(
            viewModel.deviceData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ issueRequest(it) },
                    { error -> Log.e(TAG, "Unable to get data", error) })
        )
    }

    private fun issueRequest(device: Device) {
        val jsonBody = JSONObject()
        jsonBody.put("device_id", device.device_id)
        jsonBody.put("manufacturer", device.manufacturer)
        jsonBody.put("model", device.model)
        device.parameters.map {
            jsonBody.put(it.key, it.value)
        }
        val stringRequest = JsonObjectRequest(
                Request.Method.POST, getString(R.string.endpoint), jsonBody,
                { response ->
                    displayDialog(response.toString())
                    post_button.isEnabled = true
                },
                {
                    Log.d(TAG, "That didn't work!")
                    post_button.isEnabled = true
                })
        stringRequest.tag = requestTag
        post_button.isEnabled = false
        SingletonRequestQueue.getInstance(this).addToRequestQueue(stringRequest)
    }

    private fun displayDialog(response: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.title))
            .setMessage(prettyPrintJson(response))
            .setPositiveButton(resources.getString(R.string.accept)) { _, _ -> }
            .show()
    }

    private fun prettyPrintJson(json: String): String {
        val jp = JsonParser.parseString(json)
        return GsonBuilder().setPrettyPrinting().create().toJson(jp)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}