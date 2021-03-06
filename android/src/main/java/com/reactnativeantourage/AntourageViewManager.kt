package com.reactnativeantourage

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.antourage.weaverlib.ui.fab.AntourageFab
import com.facebook.react.bridge.*
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.RCTEventEmitter
import javax.annotation.Nonnull
import kotlin.math.roundToInt


class AntourageViewManager : SimpleViewManager<AntourageFab?>(), View.OnClickListener, LifecycleEventListener {
  companion object {
    private const val REACT_CLASS = "AntourageView"
    private const val TAG = "AntourageFabLogs"
  }

  private var fab: AntourageFab? = null

  fun getInstance(): AntourageFab? {
    return fab
  }

  override fun onClick(v: View) {
    val map = Arguments.createMap()
    val context = v.context as ReactContext
    context.getJSModule(RCTEventEmitter::class.java).receiveEvent(v.id, "topChange", map)
  }

  override fun getName(): String {
    return REACT_CLASS
  }

  override fun createViewInstance(@Nonnull reactContext: ThemedReactContext): AntourageFab {
    reactContext.addLifecycleEventListener(this)
    if (fab != null) {
      Log.d(TAG, "createViewInstance old instance")
      if (fab?.parent != null) {
        (fab?.parent as ViewGroup).removeView(fab)
      }
      fab?.resetViewIsDrawn()
    } else {
      Log.d(TAG, "createViewInstance new instance")
      fab = AntourageFab(reactContext)
    }
    return fab as AntourageFab
  }

  @ReactProp(name = "widgetPosition")
  fun setPosition(button: AntourageFab, position: String?) {
    Log.d(TAG, "setPosition: $position")
    if (fab != null) fab?.setPosition(position)
  }

  @ReactProp(name = "widgetMargins")
  fun setMargins(button: AntourageFab, position: ReadableMap) {
    try {
      val horizontalMargin = position.getInt("horizontal")
      val verticalMargin = position.getInt("vertical")
      Log.d(TAG, "setMargins: $position")
      if (fab != null) fab?.setMargins(horizontalMargin, verticalMargin)
    } catch (e: NoSuchKeyException) {
      Log.e(TAG, "wrong parameters in setMargins method")
    }
  }

  @ReactProp(name = "widgetLocale")
  fun setLocale(button: AntourageFab, locale: String?) {
    Log.d(TAG, "setLocale: $locale")
    locale?.let { fab?.setLocale(it) }
  }

  override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any>? {
    return MapBuilder.of(
      "onViewerAppear",
      MapBuilder.of("registrationName", "onViewerAppear"),
      "onViewerDisappear",
      MapBuilder.of("registrationName", "onViewerDisappear")
    )
  }

  override fun onHostResume() {
    Log.d(TAG, "onResume")
    fab?.onResume()
  }

  override fun onHostPause() {
    Log.d(TAG, "onPause")
    fab?.onPause()
  }

  override fun onHostDestroy() {
  }
}
