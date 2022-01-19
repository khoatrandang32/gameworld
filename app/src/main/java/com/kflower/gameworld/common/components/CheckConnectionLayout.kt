package com.kflower.gameworld.common.components

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.kflower.gameworld.R
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import kotlin.math.log


class CheckConnectionLayout : LinearLayout {

    lateinit var errorContainer:LinearLayout;
    lateinit var btnRetry:Button;

    public var mGravity: Int=Gravity.NO_GRAVITY
        get()= field
        set(value) {
            field=value
            gravity= mGravity
        }
    public var isError: Boolean=false
    get()= field
    set(value) {
        field= value
        errorContainer.visibility= if (value) VISIBLE else GONE;
        gravity= if (value) Gravity.CENTER else mGravity;
        if(childCount>1){
            for (index in 1 until childCount) {
                val nextChild = getChildAt(index)
                nextChild.visibility= if (!value) VISIBLE else GONE;
            }
        }
    }


    constructor(context: Context) : super(context) {
        initView(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(attrs)
    }

    public fun setOnRetry(action:() -> Unit){
        btnRetry.clickWithDebounce(action = action)

    }

    private fun initView(attrs: AttributeSet?) {
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.app_check_connection_layout, null
        )
        this.addView(view)

        errorContainer= findViewById(R.id.errorContainer);
        btnRetry= findViewById(R.id.btnRetry);

        if(attrs!=null){
            val typedArray = context.theme.obtainStyledAttributes(
                attrs, R.styleable.CheckConnectionLayout, 0, 0
            )
            isError= typedArray.getBoolean(R.styleable.CheckConnectionLayout_isError,false)
            mGravity= typedArray.getInt(R.styleable.CheckConnectionLayout_isError,Gravity.NO_GRAVITY);

        }

    }



    companion object {


        @BindingAdapter("isError")
        @JvmStatic
        fun setIsError(view: CheckConnectionLayout, value: Boolean) {
            view.isError= value;
        }

        @InverseBindingAdapter(attribute = "isError")
        @JvmStatic
        fun getIsError(view: CheckConnectionLayout): Boolean {
            return view.isError
        }

    }

}