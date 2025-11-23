package vm.words.ua.settings.ui.vm

import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.domain.managers.ByteContentManager

class PolicyViewModel(
    byteContentManager: ByteContentManager
) : PdfViewerViewModel(
    byteContentManager = byteContentManager,
    linkProvider = { AppRemoteConfig.policy.link },
    defaultLinkProvider = { AppRemoteConfig.defaultPolicy.link }
)