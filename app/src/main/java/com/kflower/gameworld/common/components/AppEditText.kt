package com.kflower.gameworld.common.components

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.LinearLayout

import android.view.LayoutInflater
import android.view.View
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.kflower.gameworld.R
import androidx.databinding.InverseBindingListener
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.InputMethodManager
import android.graphics.drawable.GradientDrawable
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo


class AppEditText : LinearLayout {

    lateinit var editText: AppEdt;
    lateinit var txtTitle: TextView;
    lateinit var textViewErr: TextView;
    lateinit var imgViewTailIcon: ImageView;
    lateinit var imgLeftIcon: ImageView;
    lateinit var inputContainer: LinearLayout;
    lateinit var inputOutline: LinearLayout;
    lateinit var bgDrawable: GradientDrawable;
    var text = ""
    var strokeWidth = 5;
    var hint = ""
    var title = ""
    var isPassword = false;
    var isShowPassword = false;
    var isEdtError = false;

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
        inputOutline = findViewById(R.id.inputOutline);
        txtTitle = findViewById(R.id.txtTitle);
        textViewErr = findViewById(R.id.textViewErr);
        inputContainer = findViewById(R.id.inputContainer);
        imgViewTailIcon = findViewById(R.id.imgViewTailIcon);
        imgLeftIcon = findViewById(R.id.imgLeftIcon);

        if (attrs != null) {
            val typedArray = context.theme.obtainStyledAttributes(
                attrs, R.styleable.AppEditText, 0, 0
            )
            if (!typedArray.getText(R.styleable.AppEditText_text).isNullOrEmpty()) {
                text = typedArray.getText(R.styleable.AppEditText_text).toString()
                editText.setText(text)
            }
            if (!typedArray.getText(R.styleable.AppEditText_hint).isNullOrEmpty()) {
                hint = typedArray.getText(R.styleable.AppEditText_hint).toString()
                editText.hint = hint
            }
            if (typedArray.getDrawable(R.styleable.AppEditText_iconLeftSrc) != null) {
                imgLeftIcon.visibility = VISIBLE
                imgLeftIcon.setImageDrawable(typedArray.getDrawable(R.styleable.AppEditText_iconLeftSrc))
            }
            if (!typedArray.getText(R.styleable.AppEditText_title).isNullOrEmpty()) {
                txtTitle.visibility = VISIBLE
                title = typedArray.getText(R.styleable.AppEditText_title).toString()
                txtTitle.text = title
            } else {
                txtTitle.visibility = GONE
            }
            editText.imeOptions = typedArray.getInt(R.styleable.AppEditText_imeOptions, 0x00000000);

            var inputType = typedArray.getInt(R.styleable.AppEditText_inputType, 0x00000001);
            isPassword = inputType == 0x00000081
            if (isPassword) {
                imgViewTailIcon.setImageResource(R.drawable.ic_eye_close)
            } else {
                imgViewTailIcon.setImageResource(R.drawable.ic_close)
            }
            setCloseIcon();
            editText.inputType = inputType
            editText.typeface = Typeface.DEFAULT;
            imgViewTailIcon.setOnClickListener {

                if (isPassword) {
                    var selectionStart = editText.selectionStart
                    var selectionEnd = editText.selectionEnd
                    isShowPassword = !isShowPassword;
                    if (isShowPassword) {
                        imgViewTailIcon.setImageResource(R.drawable.ic_eye_open)
                        editText.transformationMethod = null

                    } else {
                        imgViewTailIcon.setImageResource(R.drawable.ic_eye_close)
                        editText.transformationMethod = PasswordTransformationMethod()

                    }
                    editText.setSelection(selectionStart, selectionEnd)

                } else {
                    editText.setText("");
                    imgViewTailIcon.visibility = GONE
                }
            }

            inputContainer.setOnClickListener {
                editText.requestFocus()
                val imm: InputMethodManager? =
                    (context as Activity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            }
            bgDrawable = inputOutline.background as GradientDrawable
            bgDrawable.setStroke(strokeWidth, Color.TRANSPARENT)
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    bgDrawable.setStroke(
                        strokeWidth,
                        context.resources.getColor(R.color.main_color)
                    )
                } else {
                    bgDrawable.setStroke(strokeWidth, Color.TRANSPARENT)
                    textViewErr.visibility = GONE
                    textViewErr.text = "";
                }
            }
        }
        //
        editText.addTextChangedListener(object : TextWatcher {
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
                setCloseIcon()
                if (isEdtError) {
                    setError(false)
                    bgDrawable.setStroke(
                        strokeWidth,
                        view.context.resources.getColor(R.color.main_color)
                    )
                    textViewErr.visibility = GONE
                }

            }
        })

        //
        editText.setOnKeyPreImeListener(object : AppEdt.OnKeyPreImeListener {
            override fun onImeBack(editText: AppEdt) {
                editText.clearFocus()
            }

        })
        editText.setOnEditorActionListener { v, actionId, event ->
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER))
                || (actionId == EditorInfo.IME_ACTION_DONE)
            ) {
                editText.clearFocus()

            }
            false
        }

    }


    private fun setCloseIcon() {
        if (isPassword) {
        } else {
            if (editText.text.isNullOrEmpty()) {
                Log.d("KHOA", "setCloseIcon: 1")
                imgViewTailIcon.visibility = View.GONE
            } else {
                Log.d("KHOA", "setCloseIcon: 2")
                imgViewTailIcon.visibility = View.VISIBLE
            }
        }
    }

    public fun setError(isError: Boolean, errorMessage: String? = null) {
        isEdtError = isError;
        bgDrawable = inputOutline.background as GradientDrawable
        if (isError) {
            bgDrawable.setStroke(strokeWidth, context.resources.getColor(R.color.error_color))
            if (errorMessage.isNullOrEmpty()) {
                textViewErr.visibility = GONE
            } else {
                textViewErr.visibility = VISIBLE
                textViewErr.text = errorMessage
            }
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
                        view.setCloseIcon()
                        if (view.isEdtError) {
                            view.setError(false)
                            view.bgDrawable.setStroke(
                                view.strokeWidth,
                                view.context.resources.getColor(R.color.main_color)
                            )
                            view.textViewErr.visibility = GONE
                        }

                    }
                })
            }
        }

    }


}