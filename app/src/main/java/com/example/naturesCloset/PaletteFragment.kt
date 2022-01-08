package com.example.naturesCloset

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.naturesCloset.databinding.FragmentPaletteBinding
import android.content.ClipDescription

import android.content.ClipData
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff

import android.graphics.drawable.ColorDrawable
import android.widget.Toast

import android.view.DragEvent

import android.widget.TextView

import android.graphics.drawable.Drawable
import android.view.View.*
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class PaletteFragment : Fragment(){


    private lateinit var binding: FragmentPaletteBinding
    private var mDragListener: MyDragEventListener? = null

    companion object{
        const val TAG : String = "로그"
        fun newInstance(): PaletteFragment{
            return PaletteFragment()
        }
    }

    // 메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "HomeFragment - onCreate called")

    }


    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "HomeFragment - onAttach() called")
    }


    //뷰 생성
    // fragment와 레이아웃 연결
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d(ProfileFragment.TAG, "HomeFragment - onCreateView() called")
        binding = FragmentPaletteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDragListener = MyDragEventListener()
        binding.shirt.setOnDragListener(mDragListener);
        binding.color1.setOnLongClickListener(MyLongClickListener())

    }


    private class MyLongClickListener : OnLongClickListener {
        override fun onLongClick(view: View): Boolean {

            val color = (view.background as ColorDrawable).color
            val colorString = color.toString()

            val item = ClipData.Item(colorString)


            val dragData = ClipData(
                colorString, arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item
            )

            // Instantiates the drag shadow builder
            val myShadow: DragShadowBuilder = MyDragShadowBuilder(view)

            // Starts the drag
            view.startDrag(
                dragData,  //  the data to be drag
                myShadow,  // the drag shadow builder
                null,  // no need to use local data
                0 // flags
            )
            return false
        }
    }

    private class MyDragShadowBuilder(v: View) : DragShadowBuilder(v) {
        // Defines a callback that sends the drag shadow dimensions and touch point back to the system
        override fun onProvideShadowMetrics(size: Point, touch: Point) {
            // Define local variables
            val width: Int
            val height: Int

            // Sets the width of the shadow to half the width of the original view
            width = view.width / 2

            // Sets the height of the shadow to half the height of the original view
            height = view.height / 2

            // The drag shadow will fill the Canvas
            shadow.setBounds(0, 0, width, height)

            // Sets the size parameter's width and height values
            size.set(width, height)

        // Sets the touch point position to be in the middle of the drag shadow
        touch.set(width / 2, height / 2)
    }

    override fun onDrawShadow(canvas: Canvas) {
            shadow.draw(canvas)
        }

        companion object {
            private var shadow: Drawable = ColorDrawable()
        }

        init {

            val color = (v.background as ColorDrawable).color


            shadow = ColorDrawable(getDarkerColor(color))
        }

        fun getDarkerColor(color: Int): Int {
            val hsv = FloatArray(3)
            Color.colorToHSV(color, hsv)
            //hsv[2] = 0.8f *hsv[2];
            hsv[2] = 0.7f * hsv[2] // more darker
            return Color.HSVToColor(hsv)
        }
    }

    protected class MyDragEventListener : OnDragListener {

        override fun onDrag(view: View, event: DragEvent): Boolean {
            // Define the variable to store the action type for the incoming event
            val action = event.action
            when (action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    // Determine if this view can accept dragged data
                    if (event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        // If the view view can accept dragged data
                        // Return true to indicate that the view can accept the dragged data
                        return true
                    }
                    return false
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    // When dragged item entered the receiver view area
                    return true
                }
                DragEvent.ACTION_DRAG_LOCATION ->                     // Ignore the event
                    return true
                DragEvent.ACTION_DRAG_EXITED -> {
                    // When dragged object exit the receiver object
                    // Return true to indicate the dragged object exited the receiver view
                    return true
                }
                DragEvent.ACTION_DROP -> {
                    // Get the dragged data
                    val item = event.clipData.getItemAt(0)
                    val dragData = item.text as String

                    // Cast the receiver view as a TextView object
                    val v = view as ImageView

                    // Change the TextView text color as dragged object background color

                    v.setColorFilter(Color.parseColor("#FFFFBCBC"), PorterDuff.Mode.MULTIPLY)

                    // Return true to indicate the dragged object dop
                    return true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    // Remove the background color from view
                    view.setBackgroundColor(Color.TRANSPARENT)
                    // Return true to indicate the drag ended
                    return true
                }
                else -> Log.e("Drag and Drop example", "Unknown action type received.")
            }
            return false
        }
    }

}