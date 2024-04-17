package uz.uzkassa.smartpos.core.data.manager.printer

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import uz.uzkassa.engine.printer.integration.PrinterIntegrationApi
import uz.uzkassa.smartpos.core.data.manager.printer.content.*
import uz.uzkassa.smartpos.core.data.manager.printer.content.alignment.TextAlignmentPrintParams
import uz.uzkassa.smartpos.core.data.manager.printer.exception.PrinterException
import uz.uzkassa.smartpos.core.data.manager.printer.print.PrintData
import uz.uzkassa.engine.printer.integration.model.content.PrintContentData as IntegrationPrintContentData
import uz.uzkassa.engine.printer.integration.model.data.print.ImagePrintData as IntegrationImagePrintData
import uz.uzkassa.engine.printer.integration.model.data.print.QrCodePrintData as IntegrationQrCodePrintData
import uz.uzkassa.engine.printer.integration.model.data.print.SingleLinePrintData as IntegrationSingleLinePrintData
import uz.uzkassa.engine.printer.integration.model.data.print.SpacePrintData as IntegrationSpacePrintData
import uz.uzkassa.engine.printer.integration.model.data.print.TextPairPrintData as IntegrationTextPairPrintData
import uz.uzkassa.engine.printer.integration.model.data.print.TextSinglePrintData as IntegrationTextSinglePrintData
import uz.uzkassa.engine.printer.integration.model.data.print.aligment.TextAlignmentPrintParams as IntegrationTextAlignmentParams

internal class PrinterManagerImpl(private val context: Context) : PrinterManager {
    private val integrationApi by lazy { PrinterIntegrationApi(context) }

    override fun print(data: PrintData): Flow<Unit> {
        val printContentData: IntegrationPrintContentData = IntegrationPrintContentData()
            .apply { addAll(data.getPrintContentData(context).mapToIntegrationPrintData()) }

        return flow { emit(integrationApi.print(printContentData)) }
            .map { if (it.isSuccess) it.dataOrThrow() else throw PrinterException(it.errorOrThrow()) }
            .map { Unit }
    }

    private fun List<PrintContentData>.mapToIntegrationPrintData() = map {
        return@map when (it) {
            is ImagePrintContentData -> it.map()
            is QrCodePrintContentData -> it.map()
            is SingleLinePrintContentData -> IntegrationSingleLinePrintData()
            is SpacePrintContentData -> IntegrationSpacePrintData()
            is TextPairPrintContentData -> it.map()
            is TextSinglePrintContentData -> it.map()
            else -> IntegrationSpacePrintData()
        }
    }

    private fun ImagePrintContentData.map(): IntegrationImagePrintData =
        IntegrationImagePrintData(base64)

    private fun TextPairPrintContentData.map(): IntegrationTextPairPrintData =
        IntegrationTextPairPrintData(leftText, rightText, fontSize, isBold)

    private fun QrCodePrintContentData.map(): IntegrationQrCodePrintData =
        IntegrationQrCodePrintData(data)

    private fun TextSinglePrintContentData.map(): IntegrationTextSinglePrintData =
        IntegrationTextSinglePrintData(text, fontSize, alignmentParams.map(), isBold)

    private fun TextAlignmentPrintParams.map(): IntegrationTextAlignmentParams = when (this) {
        TextAlignmentPrintParams.LEFT -> IntegrationTextAlignmentParams.LEFT
        TextAlignmentPrintParams.CENTER -> IntegrationTextAlignmentParams.CENTER
        TextAlignmentPrintParams.RIGHT -> IntegrationTextAlignmentParams.RIGHT
    }
}