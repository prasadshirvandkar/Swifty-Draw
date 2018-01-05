package com.midsizemango.swiftydraw.Animation

/**
 * Created by ABC on 12/30/2017.
 */
import android.animation.TimeAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.midsizemango.swiftydraw.R

import java.util.Random

/**
 * Continuous animation where stars slide from the bottom to the top
 * Created by Patrick Ivarsson on 7/23/17.
 */
class StarAnimationView : View {

    private val mStars = arrayOfNulls<Star>(COUNT)
    private val mRnd = Random(SEED.toLong())

    private var mTimeAnimator: TimeAnimator? = null
    private var mDrawable: Drawable? = null

    private var mBaseSpeed: Float = 0.toFloat()
    private var mBaseSize: Float = 0.toFloat()
    private var mCurrentPlayTime: Long = 0

    /**
     * Class representing the state of a star
     */
    private class Star {
        internal var x: Float = 0.toFloat()
        internal var y: Float = 0.toFloat()
        internal var scale: Float = 0.toFloat()
        internal var alpha: Float = 0.toFloat()
        internal var speed: Float = 0.toFloat()
    }

    /** @see View.View
     */
    constructor(context: Context) : super(context) {
        init()
    }

    /** @see View.View
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    /** @see View.View
     */
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        mDrawable = ContextCompat.getDrawable(context, R.drawable.star)
        mBaseSize = Math.max(mDrawable!!.intrinsicWidth, mDrawable!!.intrinsicHeight) / 2f
        mBaseSpeed = BASE_SPEED_DP_PER_S * resources.displayMetrics.density
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(width, height, oldw, oldh)

        // The starting position is dependent on the size of the view,
        // which is why the model is initialized here, when the view is measured.
        for (i in mStars.indices) {
            val star = Star()
            initializeStar(star, width, height)
            mStars[i] = star
        }
    }

    override fun onDraw(canvas: Canvas) {
        val viewHeight = height
        for (star in mStars) {
            // Ignore the star if it's outside of the view bounds
            val starSize = star!!.scale * mBaseSize
            if (star.y + starSize < 0 || star.y - starSize > viewHeight) {
                continue
            }

            // Save the current canvas state
            val save = canvas.save()

            // Move the canvas to the center of the star
            canvas.translate(star.x, star.y)

            // Rotate the canvas based on how far the star has moved
            val progress = (star.y + starSize) / viewHeight
            canvas.rotate(360 * progress)

            // Prepare the size and alpha of the drawable
            val size = Math.round(starSize)
            mDrawable!!.setBounds(-size, -size, size, size)
            mDrawable!!.alpha = Math.round(255 * star.alpha)

            // Draw the star to the canvas
            mDrawable!!.draw(canvas)

            // Restore the canvas to it's previous position and rotation
            canvas.restoreToCount(save)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mTimeAnimator = TimeAnimator()
        mTimeAnimator!!.setTimeListener(TimeAnimator.TimeListener { animation, totalTime, deltaTime ->
            if (!isLaidOut) {
                // Ignore all calls before the view has been measured and laid out.
                return@TimeListener
            }
            updateState(deltaTime.toFloat())
            invalidate()
        })
        mTimeAnimator!!.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mTimeAnimator!!.cancel()
        mTimeAnimator!!.setTimeListener(null)
        mTimeAnimator!!.removeAllListeners()
        mTimeAnimator = null
    }

    /**
     * Pause the animation if it's running
     */
    fun pause() {
        if (mTimeAnimator != null && mTimeAnimator!!.isRunning) {
            // Store the current play time for later.
            mCurrentPlayTime = mTimeAnimator!!.currentPlayTime
            mTimeAnimator!!.pause()
        }
    }

    /**
     * Resume the animation if not already running
     */
    fun resume() {
        if (mTimeAnimator != null && mTimeAnimator!!.isPaused) {
            mTimeAnimator!!.start()
            // Why set the current play time?
            // TimeAnimator uses timestamps internally to determine the delta given
            // in the TimeListener. When resumed, the next delta received will the whole
            // pause duration, which might cause a huge jank in the animation.
            // By setting the current play time, it will pick of where it left off.
            mTimeAnimator!!.currentPlayTime = mCurrentPlayTime
        }
    }

    /**
     * Progress the animation by moving the stars based on the elapsed time
     * @param deltaMs time delta since the last frame, in millis
     */
    private fun updateState(deltaMs: Float) {
        // Converting to seconds since PX/S constants are easier to understand
        val deltaSeconds = deltaMs / 1000f
        val viewWidth = width
        val viewHeight = height

        for (star in mStars) {
            // Move the star based on the elapsed time and it's speed
            star!!.y -= star!!.speed * deltaSeconds

            // If the star is completely outside of the view bounds after
            // updating it's position, recycle it.
            val size = star.scale * mBaseSize
            if (star.y + size < 0) {
                initializeStar(star, viewWidth, viewHeight)
            }
        }
    }

    /**
     * Initialize the given star by randomizing it's position, scale and alpha
     * @param star the star to initialize
     * @param viewWidth the view width
     * @param viewHeight the view height
     */
    private fun initializeStar(star: Star, viewWidth: Int, viewHeight: Int) {
        // Set the scale based on a min value and a random multiplier
        star.scale = SCALE_MIN_PART + SCALE_RANDOM_PART * mRnd.nextFloat()

        // Set X to a random value within the width of the view
        star.x = viewWidth * mRnd.nextFloat()

        // Set the Y position
        // Start at the bottom of the view
        star.y = viewHeight.toFloat()
        // The Y value is in the center of the star, add the size
        // to make sure it starts outside of the view bound
        star.y += star.scale * mBaseSize
        // Add a random offset to create a small delay before the
        // star appears again.
        star.y += viewHeight * mRnd.nextFloat() / 4f

        // The alpha is determined by the scale of the star and a random multiplier.
        star.alpha = ALPHA_SCALE_PART * star.scale + ALPHA_RANDOM_PART * mRnd.nextFloat()
        // The bigger and brighter a star is, the faster it moves
        star.speed = mBaseSpeed * star.alpha * star.scale
    }

    companion object {

        private val BASE_SPEED_DP_PER_S = 200
        private val COUNT = 32
        private val SEED = 1337

        /** The minimum scale of a star  */
        private val SCALE_MIN_PART = 0.45f
        /** How much of the scale that's based on randomness  */
        private val SCALE_RANDOM_PART = 0.55f
        /** How much of the alpha that's based on the scale of the star  */
        private val ALPHA_SCALE_PART = 0.5f
        /** How much of the alpha that's based on randomness  */
        private val ALPHA_RANDOM_PART = 0.5f
    }
}
