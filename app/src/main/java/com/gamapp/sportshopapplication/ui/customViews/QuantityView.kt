package com.gamapp.sportshopapplication.ui.customViews

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.databinding.QuantityLayoutBinding
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class QuantityView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes), NetworkResultInterface {
    private var onDeleteListener: OnClickListener? = null
    private var networkRequestInterface: NetworkRequestInterface? = null
    private var buttonsClickable: Boolean = true
    private lateinit var binding: QuantityLayoutBinding


    private var quantityChangeParam: ((Int, NetworkResultInterface) -> Unit)? = null
    var deleteParam: (() -> Unit)? = null


    private var quantity: Int
            by Delegates.observable(1) { property: KProperty<*>, oldValue: Int, newValue: Int ->
                onQuantityChange(newValue)
            }
    var maximum = 10
        private set
    var minimum = 1
        private set

    fun setMaxMin(minimum: Int, maximum: Int) {
        if (minimum <= maximum && minimum in 1..maximum) {
            this.minimum = minimum
            this.maximum = maximum
        } else {
            this.minimum = 1
            this.maximum = 1
        }
        setQuantity(quantity)
    }


    @JvmName("setQuantity1")
    fun setQuantity(quantity: Int) {
        if (quantity in minimum..maximum)
            this.quantity = quantity
        else this.quantity = minimum
    }

    @JvmName("getQuantity1")
    fun getQuantity(): Int {
        return quantity
    }


    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        context.let {
            var typedArray = it.obtainStyledAttributes(attrs, R.styleable.QuantityView)
            setVariables(typedArray)
            typedArray.recycle()
        }
        binding = QuantityLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        Picasso.get().load(R.drawable.minus_icon).fit().centerInside().into(binding.minusBtn)
        Picasso.get().load(R.drawable.plus_icon).fit().centerInside().into(binding.plusBtn)
        setQuantity(1)
        setViewsLogic()
    }

    private fun setViewsLogic() {
        binding.plusBtnFrame.let {
            it.setOnClickListener {
                if (buttonsClickable)
                    increaseQuantity()
            }
        }

        binding.minusBtnFrame.let {
            it.setOnClickListener {
                if (buttonsClickable) {
                    if (quantity > minimum)
                        decreaseQuantity()
                    else {
                        deleteParam?.let {
                            showLoadingView()
                            it()
                        }
                    }
                }
            }
        }
    }

    private fun setVariables(typedArray: TypedArray) {

    }

    private fun increaseQuantity() {
        if (quantity < maximum) {
            saveOnServer(quantity + 1)
        }
    }

    private fun decreaseQuantity() {
        if (quantity > minimum) {
            saveOnServer(quantity - 1)
        }
    }


    private fun saveOnServer(newValue: Int) {
        quantityChangeParam?.let {
            it(newValue, this)
        }
    }


    private fun onQuantityChange(newValue: Int) {
        decreaseBtn(newValue)
        increaseBtn(newValue)
        maximumTextView(newValue)

        setQuantityView(newValue)
    }


    private fun showLoadingView() {
        binding.loadingBounce.visibility = View.VISIBLE
        buttonsClickable = false
        binding.maximum.visibility = View.GONE
        binding.quantity.visibility = View.GONE
    }

    private fun closeLoadingView() {
        binding.loadingBounce.visibility = View.GONE
        buttonsClickable = true
        binding.maximum.visibility = View.GONE
        binding.quantity.visibility = View.VISIBLE
        setQuantity(quantity)
    }

    private fun increaseBtn(quantity: Int) {
        if (quantity == maximum) {
            binding.plusBtn.let {
                it.setColorFilter(ContextCompat.getColor(context, R.color.primaryTextColor))
            }

        } else {
            binding.plusBtn.let {
                it.setColorFilter(ContextCompat.getColor(context, R.color.primaryLightColor))
            }
        }
    }

    private fun decreaseBtn(quantity: Int) {
        if (quantity > minimum) {
            binding.minusBtn.let {
                Picasso.get().load(R.drawable.minus_icon).fit().centerInside().into(it)
            }
        } else if (quantity == minimum) {
            binding.minusBtn.let {
                Picasso.get().load(R.drawable.recycle_bin).fit().centerInside().into(binding.minusBtn)
                it.setColorFilter(ContextCompat.getColor(context, R.color.primaryLightColor))
            }
        }
    }

    private fun maximumTextView(quantity: Int) {
        if (quantity == maximum) {
            binding.maximum.visibility = View.VISIBLE
        } else {
            binding.maximum.visibility = View.GONE
        }
    }


    private fun setQuantityView(newValue: Int) {
        binding.quantity.text = newValue.toString()
    }

    fun onQuantityChange(param: (Int, NetworkResultInterface) -> Unit) {
        quantityChangeParam = param
    }

    fun onDeleteItem(param: () -> Unit) {
        deleteParam = param
    }


    override fun onResult(result: Boolean, value: Int) {
        if (result) {
            setQuantity(value)
        } else {
            setQuantity(quantity)
        }
    }

    fun setLoading(onChanging: Boolean) {
        if (onChanging)
            showLoadingView()
        else
            closeLoadingView()
    }


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    ) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0, 0) {
        init(context, attrs)
    }


}

