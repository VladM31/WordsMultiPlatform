package vm.words.ua.settings.ui.vm

import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.domain.managers.ByteContentManager

class InstructionViewModel(
    byteContentManager: ByteContentManager
) : PdfViewerViewModel(byteContentManager, { AppRemoteConfig.instruction.link })