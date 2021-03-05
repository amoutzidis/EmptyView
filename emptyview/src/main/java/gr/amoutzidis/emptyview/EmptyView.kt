package gr.amoutzidis.emptyview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import gr.amoutzidis.emptyview.databinding.MergeEmptyViewBinding

class EmptyView: ConstraintLayout {

    companion object{

        private const val NOT_SET_MARGIN = -999f
        private const val NO_RESOURCE = 0
    }


    private lateinit var binding: MergeEmptyViewBinding
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(
            context,
            attrs,
            R.attr.emptyShadowView
    )
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ){
        inflateLayout()

        val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.EmptyView, defStyleAttr,
                R.style.EmptyView
        )

        icon(a.getResourceId(R.styleable.EmptyView_icon, NO_RESOURCE))

        setupTitle(
                a.getResourceId(R.styleable.EmptyView_titleStyle, NO_RESOURCE),
                a.getString(R.styleable.EmptyView_title)
        )
        setupDescription(
                a.getResourceId(R.styleable.EmptyView_descriptionStyle, NO_RESOURCE),
                a.getString(R.styleable.EmptyView_description)

        )
        setupButton(
                a.getResourceId(R.styleable.EmptyView_buttonStyle, NO_RESOURCE),
                a.getString(R.styleable.EmptyView_button)
        )

//
        iconMargin(a.getDimension(R.styleable.EmptyView_iconMargin, NOT_SET_MARGIN))

        descriptionMargin(a.getDimension(R.styleable.EmptyView_descriptionMargin, NOT_SET_MARGIN))

        buttonMargin(a.getDimension(R.styleable.EmptyView_buttonMargin, NOT_SET_MARGIN))
    }


    fun icon(@DrawableRes resId: Int){
        if(resId == NO_RESOURCE){
            icon(null)
            return
        }

        icon(ContextCompat.getDrawable(context, resId))
    }

    fun icon(drawable: Drawable?){
        binding.imageView.setImageDrawable(drawable)
        if(drawable == null) {
            binding.imageView.isGone = true
            return
        }

        binding.imageView.isVisible = true
    }

    fun iconMargin(@DimenRes dimenRes: Int){
        iconMargin(context.resources.getDimension(dimenRes))
    }

    fun iconMargin(pixels: Float){
        _margin(
                binding.imageView,
                pixels,
                false
        )
    }

    fun descriptionMargin(margin: Float){
        _margin(
                binding.titleTextView,
                margin,
                false
        )
    }

    fun buttonMargin(margin: Float){
        _margin(
                binding.button,
                margin,
                true
        )
    }

    private fun _margin(view: View, margin: Float, topMargin: Boolean){
        if(margin == NOT_SET_MARGIN)
            return

        val newLayoutParams = view.layoutParams as LayoutParams

        if(topMargin){
            newLayoutParams.topMargin = margin.toInt()
        }else{
            newLayoutParams.bottomMargin = margin.toInt()
        }

        view.layoutParams = newLayoutParams
    }


    private fun setupTitle(@StyleRes resId: Int, text: String?){
        setup(binding.titleTextView, resId, text)
    }

    private fun setupDescription(@StyleRes resId: Int, text: String?){
        setup(binding.descriptionTextView, resId, text)
    }

    private fun setupButton(@StyleRes resId: Int, text: String?){
        setup(binding.button, resId, text)

        if(text != null){
            binding.button.setOnClickListener {
                callback?.invoke()
            }
        }
    }

    private fun setup(textView: TextView, @StyleRes resId: Int, text: String?){
        if(resId != NO_RESOURCE)
            TextViewCompat.setTextAppearance(textView, resId)

        if(text == null){
            textView.isGone = true
        }else {
            textView.isVisible = true
            textView.text = text
        }
    }

    private var callback: (()-> Unit)?= null
    fun onActionCallback(callback: (()-> Unit)?){
        this.callback = callback
    }

    private fun inflateLayout(){
        binding = MergeEmptyViewBinding.inflate(LayoutInflater.from(context), this)
    }
}