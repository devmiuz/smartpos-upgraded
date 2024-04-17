package uz.uzkassa.common.feature.browser.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.common.feature.browser.R
import uz.uzkassa.common.feature.browser.data.model.WebPage
import uz.uzkassa.common.feature.browser.presentation.delegate.WebViewDelegate
import uz.uzkassa.common.feature.browser.presentation.di.BrowserComponent
import uz.uzkassa.smartpos.core.presentation.app.fragment.dialog.DialogFragmentSupportDelegate
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.support.delegate.view.ToolbarDelegate
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.core.presentation.utils.content.colorAccent
import uz.uzkassa.smartpos.core.presentation.utils.view.setTint
import uz.uzkassa.smartpos.core.utils.resource.string.get
import javax.inject.Inject
import uz.uzkassa.common.feature.browser.databinding.FragmentFeatureBrowserBinding as ViewBinding

class BrowserFragment : MvpAppCompatDialogFragment(), IHasComponent<BrowserComponent>, BrowserView,
    Toolbar.OnMenuItemClickListener, WebViewDelegate.OnWebViewStateChangedListener {

    @Inject
    internal lateinit var lazyPresenter: Lazy<BrowserPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private lateinit var binding: ViewBinding
    private val delegate by lazy { DialogFragmentSupportDelegate(this) }
    private val toolbarDelegate by lazy { ToolbarDelegate(this) }
    private val webViewDelegate by lazy { WebViewDelegate(this, this) }

    override fun getComponent(): BrowserComponent =
        BrowserComponent.create(XInjectionManager.findComponent())

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        delegate.onCreateDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ViewBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            toolbarDelegate.apply {
                onCreate(toolbar, savedInstanceState)
                inflateMenu(R.menu.menu_feature_browser, this@BrowserFragment)
                findMenuItemById(R.id.agreement_menu_item)?.let { it ->
                    it.setTint(requireContext().colorAccent)
                    it.isVisible = false
                }
                setNavigationOnClickListener { presenter.dismiss() }
            }
            webViewDelegate.apply {
                onCreate(contentWebView, savedInstanceState)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.agreement_menu_item -> presenter.dismiss().let { true }
        else -> false
    }

    override fun onStartPageLoading() =
        presenter.setLoadingWebPage()

    override fun onFinishPageLoading() {
        toolbarDelegate.apply {
            findMenuItemById(R.id.agreement_menu_item)
                ?.isVisible = true
        }
        presenter.setSuccessWebPage()
    }

    override fun onLoadingWebPage() {
        binding.loadingProgressBar.isVisible = true
    }

    override fun onSuccessWebPage() {
        binding.loadingProgressBar.isVisible = false
    }

    override fun onContentUrlDefined(webPage: WebPage) {
        toolbarDelegate.setTitle(webPage.title.resourceString.get(requireContext()))
        webViewDelegate.loadUrl(webPage.contentUrl)
    }

    override fun onErrorWebPage(throwable: Throwable) {
        binding.loadingProgressBar.isVisible = false

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    override fun onDismissView() =
        dismiss()

    companion object {

        fun newInstance() =
            BrowserFragment().withArguments()
    }
}