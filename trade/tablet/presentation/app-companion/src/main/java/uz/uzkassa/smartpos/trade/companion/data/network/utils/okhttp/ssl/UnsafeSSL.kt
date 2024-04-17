package uz.uzkassa.smartpos.trade.companion.data.network.utils.okhttp.ssl

import android.annotation.SuppressLint
import java.security.SecureRandom
import java.security.cert.CertificateException
import javax.net.ssl.*

internal object UnsafeSSL {

    fun getUnsafeSSLSocketFactory(): SSLSocketFactory =
        SSLContext.getInstance("SSL")
            .apply { init(null, trustAllCerts, SecureRandom()) }
            .socketFactory

    fun getUnsafeTrustManager(): X509TrustManager =
        trustAllCerts[0] as X509TrustManager

    fun getUnsafeHostnameVerifier(): HostnameVerifier =
        HostnameVerifier { _, _ -> true }

    private val trustAllCerts = arrayOf<TrustManager>(
        @SuppressLint("TrustAllX509TrustManager")
        object : X509TrustManager {

            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                chain: Array<java.security.cert.X509Certificate>,
                authType: String
            ) = Unit

            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<java.security.cert.X509Certificate>,
                authType: String
            ) = Unit

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> =
                arrayOf()
        }
    )
}