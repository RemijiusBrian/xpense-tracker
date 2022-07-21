package dev.ridill.xpensetracker.feature_expenses.domain.sms

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class PaymentSmsDataServiceTest {

    private lateinit var paymentSmsDataService: PaymentSmsDataService

    @Before
    fun setup() {
        paymentSmsDataService = PaymentSmsDataService()
    }

    @Test
    fun checkInvalidMerchantSender_returnsFalse() {
        val merchantSender = "JKHDFC"
        val result = paymentSmsDataService.isMerchantSms(merchantSender)
        assertThat(result).isFalse()
    }

    @Test
    fun checkValidHDFCBankSender_returnsTrue() {
        val sender = "JM-HDFCBK"
        val result = paymentSmsDataService.isMerchantSms(sender)
        assertThat(result).isTrue()
    }

    @Test
    fun checkMonetaryDebitWithSpentKeyword_returnsTrue() {
        val message =
            "Alert!You've spent Rs.1000 On HDFC Bank Debit Card xx7572 At ..DDRC SRL DIGANOS_ On 2022-07-21:10:59:22 Avl bal: 7536.47 Not you?Call 18002586161"
        val result = paymentSmsDataService.isSmsForMonetaryDebit(message)
        assertThat(result).isTrue()
    }

    @Test
    fun checkMonetaryDebitWithDebitKeyword_returnsTrue() {
        val message =
            "HDFC Bank: Rs 3000.00 debited from a/c **1643 on 14-07-22 to VPA johnjoel582@oksbi(UPI Ref No 219549954262). Not you? Call on 18002586161 to report"
        val result = paymentSmsDataService.isSmsForMonetaryDebit(message)
        assertThat(result).isTrue()
    }

    @Test
    fun checkMonetaryDebitWithoutValidKeyword_returnsFalse() {
        val message =
            "HDFC Bank: Rs 3000.00 credited to a/c **1643 on 14-07-22 to VPA johnjoel582@oksbi(UPI Ref No 219549954262). Not you? Call on 18002586161 to report"
        val result = paymentSmsDataService.isSmsForMonetaryDebit(message)
        assertThat(result).isFalse()
    }

    @Test
    fun extractAmountFromValidSms_returnsValidAmount() {
        val message =
            "HDFC Bank: Rs 3000.00 debited from a/c **1643 on 14-07-22 to VPA johnjoel582@oksbi(UPI Ref No 219549954262). Not you? Call on 18002586161 to report"
        val amount = paymentSmsDataService.extractAmount(message)
        assertThat(amount).isEqualTo("3000.00")
    }

    @Test
    fun extractAmountFromSBISms_returnsValidAmount() {
        val message =
            "Dear Customer, Your a/c no. XXXXXXXX9502 is debited for Rs.1320.00 on 12-07-22 and a/c XXXXXXX643 credited (IMPS Ref no 219310988669). -SBI"
        val amount = paymentSmsDataService.extractAmount(message)
        assertThat(amount).isEqualTo("1320.00")
    }

    @Test
    fun extractMerchantFromDDRCSms_returnsAtDDRC() {
        val message =
            "Alert!You've spent Rs.1000 On HDFC Bank Debit Card xx7572 At DDRC SRL DIGANOS_ On 2022-07-21:10:59:22 Avl bal: 7536.47 Not you?Call 18002586161"
        val result = paymentSmsDataService.extractMerchantName(message)
        assertThat(result).isEqualTo("At DDRC SRL")
    }

    @Test
    fun allValuesExtractedFromSms_entireFlowSucceeds() {
        val sender = "BZ-SBIINB"
        assertThat(paymentSmsDataService.isMerchantSms(sender)).isTrue()
        val message =
            "Alert!You've spent Rs.1000 On HDFC Bank Debit Card xx7572 At DDRC SRL DIGANOS_ On 2022-07-21:10:59:22 Avl bal: 7536.47 Not you?Call 18002586161"
        assertThat(paymentSmsDataService.isSmsForMonetaryDebit(message)).isTrue()
        assertThat(paymentSmsDataService.extractAmount(message)).isEqualTo("1000")
        assertThat(paymentSmsDataService.extractMerchantName(message)).isEqualTo("At DDRC SRL")
    }
}