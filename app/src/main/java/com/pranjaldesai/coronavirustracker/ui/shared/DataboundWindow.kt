package com.pranjaldesai.coronavirustracker.ui.shared

import androidx.databinding.ViewDataBinding

interface DataboundWindow<ViewDataBindingT : ViewDataBinding> :
    Window {
    val binding: ViewDataBindingT
    fun attachBindingLayout(): ViewDataBindingT
}