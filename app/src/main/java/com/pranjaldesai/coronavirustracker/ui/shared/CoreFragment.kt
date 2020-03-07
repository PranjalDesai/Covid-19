package com.pranjaldesai.coronavirustracker.ui.shared

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.pranjaldesai.coronavirustracker.helper.EMPTY_STRING
import com.pranjaldesai.coronavirustracker.lifecycle.*
import com.pranjaldesai.coronavirustracker.ui.customView.ProgressIndicatorView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class CoreFragment<ViewDataBindingT : ViewDataBinding> : Fragment(),
    DataboundWindow<ViewDataBindingT>, Toolbar.OnMenuItemClickListener, CoroutineScope {


    override val windowContext: Context?
        get() = context

    override val binding: ViewDataBindingT by lazy { attachBindingLayout() }

    override val coroutineContext: CoroutineContext = Dispatchers.Default

    @MenuRes
    override val menuResourceId: Int? = null

    protected open val toolbar: Toolbar? = null

    protected open val appbar: AppBarLayout? = null

    protected open val collapsingToolbarLayout: CollapsingToolbarLayout? = null

    protected open val appbarExpandedTitle: String by lazy { EMPTY_STRING }

    protected open val homeIntent: Intent = generateHomeIntent()

    protected open var defaultOrientation: Int = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR

    protected open val useCustomBackButtonAction: Boolean = false

    protected var instanceBundle: Bundle? = null

    override val logLifecycleEvents: Boolean = true

    @StringRes
    protected open val toolbarTitleStringResourceId: Int? = null

    protected open val toolbarTitle: String by lazy {
        toolbarTitleStringResourceId?.let {
            getString(
                it
            )
        } ?: defaultToolbarTitleText
    }

    private val defaultToolbarTitleText: String by lazy {
        toolbar?.title?.toString() ?: EMPTY_STRING
    }

    protected var currentState: LifecycleState = LifecycleState.UNKNOWN
        protected set(value) {
            field = value
            onLifecycleStateChanged(value)
        }

    private lateinit var inflater: LayoutInflater

    private lateinit var rootView: View

    private var attachToParent: Boolean = false

    private var container: ViewGroup? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestOrientationChange()
        bindCustomBackButtonAction()
        created()
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (::rootView.isInitialized.not()) {
            this.inflater = inflater
            this.container = container

            rootView = binding.root

            loadSavedInstanceState(savedInstanceState)
            setupToolbar()
            setupAppbar()
            bindData()
        }
        requestOrientationChange()
        viewCreated()
        return rootView
    }

    protected open fun requestOrientationChange() {
        activity?.requestedOrientation = defaultOrientation
    }

    final override fun attachLayout() {}

    final override fun attachBindingLayout(): ViewDataBindingT =
        DataBindingUtil.inflate(layoutInflater, layoutResourceId, container, attachToParent)

    private fun setupToolbar() {
        toolbar?.let { toolbar ->
            setupCustomToolbar(toolbar)
        }
            ?: activity?.let { activity ->
                updateToolbar(activity)
            }
    }

    private fun setupCustomToolbar(toolbar: Toolbar) {
        activity?.let { _ ->
            toolbar.title = toolbarTitle
            onCreateOptionsMenu(toolbar)
        }
    }

    private fun updateToolbar(activity: FragmentActivity) {
        activity.title = toolbarTitle
        setHasOptionsMenu(menuResourceId != null)
    }

    private fun setupAppbar() {
        appbar?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset == 0) {
                collapsingToolbarLayout?.title = EMPTY_STRING
            } else {
                collapsingToolbarLayout?.title = appbarExpandedTitle
            }
        })
    }

    private fun generateHomeIntent(): Intent {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return intent
    }


    @CallSuper
    override fun onStart() {
        super.onStart()
        started()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        resumed()
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        stopped()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        paused()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        destroyed()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        instanceBundle?.let { instanceVariables ->
            onSaveInstanceState(instanceVariables)
        }
        viewDestroyed()
    }

    @CallSuper
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menuResourceId?.let {
            menu.clear()
            inflater.inflate(it, menu)
        } ?: super.onCreateOptionsMenu(menu, inflater)
    }

    @CallSuper
    protected fun onCreateOptionsMenu(toolbar: Toolbar) {
        menuResourceId?.let {
            toolbar.menu.clear()
            toolbar.inflateMenu(it)
            toolbar.setOnMenuItemClickListener(this)
        }
    }

    final override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return onMenuItemClick(item)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return false
    }

    @CallSuper
    override fun onLifecycleStateChanged(state: LifecycleState) {
        logLifecycleEvent(state)
    }

    override fun bindData() {
        /* For stub methods, how do we want to denote that? Custom annotation? Or just empty method body? */
    }

    open fun loadSavedInstanceState(savedInstanceState: Bundle?) {}

    override fun progressIndicatorView(): ProgressIndicatorView? {
        return if (activity != null && activity is CoreActivity<*>) {
            (activity as CoreActivity<*>).progressIndicatorView()
        } else {
            null
        }
    }

    override fun showProgressIndicator() {
        activity?.window?.let { window ->
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            )
        }
        progressIndicatorView()?.visibility = View.VISIBLE
    }

    override fun hideProgressIndicator() {
        activity?.window?.let { window ->
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        }
        progressIndicatorView()?.visibility = View.INVISIBLE
    }

    open fun onBackButtonClicked() {}

    final override fun gatherControls() {
        /* We don't need to gather controls in data bound windows or views, since the binding object handles that */
    }

    final override fun updateLifecycleState(state: LifecycleState) {
        currentState = state
    }

    final override fun <V : View> findViewById(id: Int): V {
        return rootView.findViewById(id)
    }

    final override fun runOnUiThread(task: Runnable, delayInMillis: Long) {
        Handler(Looper.getMainLooper()).postDelayed(task, delayInMillis)
    }

    override fun runOnUiThread(task: Runnable) {
        Handler(Looper.getMainLooper()).post(task)
    }

    fun hideKeyboard() {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun navigationHost(): INavigationHost? {
        return activity?.let { activity ->
            return if (activity is INavigationHost) {
                activity
            } else {
                null
            }
        }
    }

    private fun bindCustomBackButtonAction() {
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(useCustomBackButtonAction) {
                override fun handleOnBackPressed() {
                    onBackButtonClicked()
                }
            }
        )
    }
}