package uz.uzkassa.smartpos.core.data.manager.scanner.types.hid

import android.util.Log
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
//import uz.uzkassa.smartpos.core.data.manager.scanner.BuildConfig
import uz.uzkassa.smartpos.core.data.manager.scanner.params.BarcodeScannerParams
import uz.uzkassa.smartpos.core.data.manager.scanner.types.BarcodeScanner
import uz.uzkassa.smartpos.core.manager.logger.Logger

@Suppress("EXPERIMENTAL_API_USAGE")
internal class HIDBarcodeScanner : BarcodeScanner {
    private val channel = BroadcastChannel<String>(1)
    private var barcode: String = ""

    override fun isResultAvailable(): Boolean =
        barcode.isNotBlank()

    @FlowPreview
    override fun getResult(): Flow<String> {
        return channel
            .asFlow()
            .debounce(85)
            .map { readValue ->
                readValue.trim().replace(NON_VALID_UNICODE_CHARACTERS_REGEX, "")
                    .also {
                        Log.wtf(
                            "HIDBarcodeScanner",
                            "read value = $readValue, after clearing value = $it"
                        )
                    }
            }
            .onEach { barcode = "" }
    }

    override fun setBarcodeScannerParams(params: BarcodeScannerParams): Boolean {
//        val isAvailable: Boolean =
//            (if (params.isMarking) verifiedVendors else vendors)
//                .contains(params.vendor, ignoreCase = true)
        val isAvailable = true;

        Log.wtf(
            "HIDBarcodeScanner",
            "isAvailable = $isAvailable params = $params, barcode = ${barcode + params.value.toString()}"
        )

        if (isAvailable) {
            barcode += params.value.toString()
            channel.sendBlocking(barcode)
        } else {
//            if (BuildConfig.DEBUG)
//                Logger.e("HID Vendor", params.vendor)
        }

        return isAvailable
    }

    private val verifiedVendors: List<String> =
        listOf(
            "BARCODE SCANNER BARCODE SCANNER", //AK15 | UZKASSA (23.12.20)
            "Newtologic  NT1640S", //AK15 (15.07.20)
            "TiCode Electroinc TICODE Barcode Scanner", //AK18H (15.07.20)
            "TMS HIDKeyBoard", //AK27 | UZKASSA (23.12.20)
            "Manufacturer Barcode Reader", // GlobalPOS GP-3300 (16.09.21)
            "HID 0581:011c", // GlobalPOS GP-9400B (16.09.21)
            "Scanner 799", // HW-3809B | UZKASSA (11.10.2021)
            "W233 Scanner", // OCBS-W233 | UZKASSA (11.10.2021)
            "LWTEK Barcode Scanner", // GlobalPOS GP-9400B (25.10.2021)
            "Barcode Scanner HID", // GlobalPOS GP-9400B (25.10.2021)
            "NETUM USB Keyboard"
        )

    private val vendors: List<String> =
        listOf(
            "2D Imager 2D Barcode Scanner", //TI2125 (15.07.20)
            "BarcodeScanners Drives - H2750 H2750  Usb Devices", //XT6603 (24.08.20)
            "BARCODE SCANNER BARCODE SCANNER", //AK15 | UZKASSA (23.12.20)
            "HID 0581:0103",
            "HID 0581:0106", // GlobalPOS (30.04.21)
            "LWTEK Barcode Scanner", // GlobalPOS GP-9400B (25.10.2021)
            "Barcode Scanner HID", // GlobalPOS GP-9400B (25.10.2021)
            "HID Vendor: NewLand HidKeyBoard", // XL-626A (08.09.20)
            "HidKeyBoard",
            "Honeywell",
            "Minde",
            "Newland Auto-ID DM21", //XT6203 (24.08.20)
            "NEWTOLOGIC NEWTOLOGIC", //KD15, LF1650S (24.08.20)
            "Newtologic  NT1640S", //AK15 (15.07.20)
            "Newtologic  VS100S", // VS100S (13.07.21)
            "SuperLead USB HID",
            "TiCode Electroinc TICODE Barcode Scanner", //AK18H (15.07.20)
            "TMS HIDKeyBoard", //AK27 | UZKASSA (23.12.20)
            "USB Adapter USB Device",
            "USB BARCODE SCANNER USB BARCODE SCANNER",
            "USBKey Chip USBKey Module",
            "Manufacturer Barcode Reader", // GlobalPOS GP-3300 (16.09.21)
            "HID 0581:011c", // GlobalPOS GP-9400B (16.09.21)
            "Scanner 799", // HW-3809B | UZKASSA (11.10.2021)
            "W233 Scanner", // OCBS-W233 | UZKASSA (11.10.2021)
            "NETUM USB Keyboard"
        )

    fun List<String>.contains(value: String, ignoreCase: Boolean): Boolean {
        for (element: String in this) {
            if (element.contains(value, ignoreCase)) return true
        }

        return false
    }

    @Suppress("SpellCheckingInspection")
    private companion object {
        val NON_VALID_UNICODE_CHARACTERS_REGEX: Regex =
            "[^\\u0009\\u000a\\u000d\\u0020-\\uD7FF\\uE000-\\uFFFD]".toRegex()
    }
}