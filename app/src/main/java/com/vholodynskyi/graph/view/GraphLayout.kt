package com.vholodynskyi.graph.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.animation.doOnEnd
import com.vholodynskyi.graph.R

class GraphLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "GraphLayout"

        private const val POPUP_TOP_MARGIN = 450
    }

    private val backgroundView: View = View(context)
    private val graphView = GraphView(context, attrs, defStyleAttr)
    private val headerText = TextView(context)
    private val dateText = TextView(context)
    private val popup = PopupWindow(context)

    private var popupLastX = 0

    init {
        backgroundView.setBackgroundResource(R.drawable.background)

        headerText.text = "Followers"
        headerText.textSize = 14f

        dateText.text = "1 Jan 2020 - 1 Dec 2020"
        dateText.textSize = 14f

        popup.contentView = inflate(context, R.layout.l_popup, null)

        graphView.setData(generateData())

        addView(backgroundView)
        addView(graphView)
        addView(headerText)
        addView(dateText)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(parentWidth, parentHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        headerText.layout(l + 50, t, r, b)
        dateText.layout(l + width - 450, t, r, b)
        graphView.layout(l + 50, t + 150, r - 50, b / 2)
    }

    fun showPopup(dataPoint: Float) {
        if (!popup.isShowing) {
            popup.showAtLocation(this, 0 , dataPoint.toInt(), POPUP_TOP_MARGIN)
            popupLastX = dataPoint.toInt()
        } else {
            ValueAnimator.ofInt(popupLastX, dataPoint.toInt()).apply {
                duration = 500
                addUpdateListener {
                    popup.update((animatedValue as Int), POPUP_TOP_MARGIN, popup.width, popup.height)
                }
                doOnEnd { popupLastX = dataPoint.toInt() }
                start()
            }
        }
    }

    fun dismissPopup() {
        popup.dismiss()
    }
}