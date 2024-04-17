package uz.uzkassa.smartpos.feature.launcher.presentation.features.sync

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.Lazy
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import uz.uzkassa.smartpos.core.presentation.support.delegate.dialog.error.ErrorDialogFragment
import uz.uzkassa.smartpos.core.presentation.utils.app.withArguments
import uz.uzkassa.smartpos.feature.launcher.R
import uz.uzkassa.smartpos.feature.launcher.presentation.features.sync.di.SyncComponent
import javax.inject.Inject
import uz.uzkassa.smartpos.feature.launcher.databinding.FragmentFeatureLauncherSyncBinding as ViewBinding

internal class SyncFragment : MvpAppCompatFragment(R.layout.fragment_feature_launcher_sync),
    IHasComponent<SyncComponent>, SyncView {

    @Inject
    lateinit var lazyPresenter: Lazy<SyncPresenter>
    private val presenter by moxyPresenter { lazyPresenter.get() }

    private val binding: ViewBinding by viewBinding()

    override fun getComponent(): SyncComponent =
        SyncComponent.create(XInjectionManager.findComponent())

    override fun onCreate(savedInstanceState: Bundle?) {
        XInjectionManager.bindComponent(this).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply { retryButton.setOnClickListener { presenter.getSyncState() } }
    }

    override fun onLoadingSyncState() {
        binding.apply {
            launcherBackgroundLayout.setText(R.string.fragment_feature_launcher_sync_loading_text)
            retryButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun onErrorSyncState(throwable: Throwable) {
        binding.apply {
            launcherBackgroundLayout.setText(R.string.fragment_feature_launcher_sync_error_text)
            progressBar.visibility = View.GONE
            retryButton.visibility = View.VISIBLE
        }

        if (!presenter.isInRestoreState(this))
            ErrorDialogFragment.show(this, throwable)
    }

    companion object {

        fun newInstance() =
            SyncFragment().withArguments()
    }
}