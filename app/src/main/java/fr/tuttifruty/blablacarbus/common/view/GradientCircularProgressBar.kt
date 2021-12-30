package fr.tuttifruty.blablacarbus.common.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import com.google.android.material.color.MaterialColors
import fr.tuttifruty.blablacarbus.R
import kotlin.math.asin
import kotlin.math.ceil


class GradientCircularProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private var mPaddingPx = 0f
    private var mBodyStrokeWidthPx = 0f
    private lateinit var mPaintBody: Paint
    private var mRect: RectF? = null
    private var mBodyGradient: SweepGradient? = null
    private lateinit var mBodyGradientFromToColors: IntArray
    private lateinit var mGradientFromToPositions: FloatArray

    //additional offset caused by rendered cup at start
    private var mCupAdditionalOffset = 0
    private var mRotationAnimator: ObjectAnimator? = null
    private val rot = 0f

    init {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        if (!mRotationAnimator!!.isStarted) {
            mRotationAnimator!!.start()
        }
        canvas.rotate(rot, width.toFloat() / 2, height.toFloat() / 2)
        val left = mPaddingPx
        val top = mPaddingPx
        val right: Float = width - mPaddingPx
        val bottom: Float = height - mPaddingPx
        mRect!![left, top, right] = bottom
        mRect?.let { rect ->
            canvas.drawArc(rect, mCupAdditionalOffset.toFloat(), BODY_LENGTH, false, mPaintBody)
        }
    }

    override fun onSizeChanged(newWidth: Int, newHeight: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(newWidth, newHeight, oldw, oldh)
        val centerX = (newWidth / 2).toFloat()
        val centerY = (newHeight / 2).toFloat()
        mBodyGradient = SweepGradient(
            centerX, centerY,
            mBodyGradientFromToColors, mGradientFromToPositions
        )
        mPaintBody.shader = mBodyGradient
        val cupRadius = mPaddingPx
        val arcRadius = newWidth / 2f - mPaddingPx
        mCupAdditionalOffset = computeOffset(cupRadius, arcRadius)
    }

    private fun init() {
        //disable hardware acceleration
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        val bodyColor: Int = MaterialColors.getColor(this, R.attr.colorPrimary, Color.BLACK)

        val displayMetrics: DisplayMetrics = resources.displayMetrics
        mBodyStrokeWidthPx = TypedValue
            .applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                BODY_STROKE_WIDTH.toFloat(),
                displayMetrics
            )
        mPaddingPx = TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, PADDING.toFloat(), displayMetrics)

        mRect = RectF()
        mPaintBody = Paint()
        mPaintBody.isAntiAlias = true
        mPaintBody.color = bodyColor
        mPaintBody.strokeWidth = mBodyStrokeWidthPx
        mPaintBody.style = Paint.Style.STROKE
        mPaintBody.strokeJoin = Paint.Join.ROUND
        mPaintBody.strokeCap = Paint.Cap.ROUND
        mBodyGradientFromToColors = intArrayOf(Color.TRANSPARENT, bodyColor)
        mGradientFromToPositions = floatArrayOf(0f, NORMALIZED_GRADIENT_LENGTH)
        mRotationAnimator = createRotateAnimator()
    }

    private fun createRotateAnimator(): ObjectAnimator {
        val rotateAnimator = ObjectAnimator.ofFloat(
            this,
            ROTATION_PROPERTY_NAME, 0f, ROTATION_LENGTH.toFloat()
        )
        rotateAnimator.duration = ROTATION_DURATION.toLong()
        rotateAnimator.interpolator = LinearInterpolator()
        rotateAnimator.repeatMode = ValueAnimator.RESTART
        rotateAnimator.repeatCount = ValueAnimator.INFINITE
        return rotateAnimator
    }

    companion object {
        private const val BODY_STROKE_WIDTH = 2

        // padding has to be the half size of the stroke width plus blur filter radius
        private const val PADDING = 5
        private const val BODY_LENGTH = 270f
        private const val NORMALIZED_GRADIENT_LENGTH = BODY_LENGTH / 720f
        private const val ROTATION_DURATION = 1200
        private const val ROTATION_LENGTH = 360
        private const val ROTATION_PROPERTY_NAME = "rotation"
        private fun computeOffset(capRadius: Float, arcRadius: Float): Int {
            val sinus = capRadius / arcRadius
            val degreeRad = asin(sinus.toDouble())
            val degree = Math.toDegrees(degreeRad)
            return ceil(degree).toInt()
        }
    }
}