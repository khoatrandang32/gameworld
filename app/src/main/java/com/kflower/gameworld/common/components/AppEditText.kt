package com.kflower.gameworld.common.components

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout

import android.view.LayoutInflater
import android.view.View
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.kflower.gameworld.R
import androidx.databinding.InverseBindingListener


class AppEditText : LinearLayout {

    lateinit var editText: EditText;
    lateinit var txtTitle: TextView;
    var text = ""
    var hint = ""
    var title = ""

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

    private fun initView(attrs: AttributeSet?) {

        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.app_edittext_component_layout, null
        )
        view.layoutParams = layoutParams
        this.addView(view)
        editText = findViewById(R.id.appEditText);
        txtTitle = findViewById(R.id.txtTitle);

        if (attrs != null) {
            val typedArray = context.theme.obtainStyledAttributes(
                attrs, R.styleable.AppEditText, 0, 0
            )
            if (!typedArray.getText(R.styleable.AppEditText_text).isNullOrEmpty()) {
                text = typedArray.getText(R.styleable.AppEditText_text).toString()
                editText.setText(text)
                Log.d("KHOA", "initText $text")
            }
            if (!typedArray.getText(R.styleable.AppEditText_hint).isNullOrEmpty()) {
                hint = typedArray.getText(R.styleable.AppEditText_hint).toString()
                editText.hint = hint
            }
            if (!typedArray.getText(R.styleable.AppEditText_title).isNullOrEmpty()) {
                title = typedArray.getText(R.styleable.AppEditText_title).toString()
                txtTitle.text = title
            }
            editText.imeOptions = typedArray.getInt(R.styleable.AppEditText_imeOptions, 0x00000000);

            var inputType = typedArray.getInt(R.styleable.AppEditText_inputType, 0x00000000);
            if (inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {

            }
            editText.inputType = inputType
            editText.typeface = Typeface.DEFAULT;


        }

    }

    @JvmName("setEdtText")
    public fun setEdtText(text: String) {
        this.text = text;
        editText.setText(text)

    }

    companion object {


        @BindingAdapter("text")
        @JvmStatic
        fun setText(view: AppEditText, value: String?) {
            Log.d("KHOA", "setText $value")
            if (value.isNullOrEmpty()) {
                view.text = ""
            } else {
                if (value != view.text) view.setEdtText(value.toString())
            }
        }

        @InverseBindingAdapter(attribute = "text")
        @JvmStatic
        fun getText(view: AppEditText): String {
            Log.d("KHOA", "getText ${view.text}")
            return view.text;
        }

        @BindingAdapter("textAttrChanged")
        @JvmStatic
        fun setListener(
            view: AppEditText,
            textAttrChanged: InverseBindingListener?
        ) {
            if (textAttrChanged != null) {
                view.editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                    }

                    override fun onTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                    }

                    override fun afterTextChanged(editable: Editable) {
                        textAttrChanged.onChange()
                        view.text = editable.toString()
                    }
                })
            }
        }

    }


}