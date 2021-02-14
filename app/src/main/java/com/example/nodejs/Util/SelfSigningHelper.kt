package com.example.nodejs.Util

import com.example.nodejs.NodeApplication
import com.example.nodejs.R
import okhttp3.OkHttpClient
import java.io.InputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.security.cert.Certificate
import javax.net.ssl.*

class SelfSigningHelper {
    private lateinit var sslContext: SSLContext
    private lateinit var trustManagerFactory: TrustManagerFactory

    init {
        setUp()
    }

    private fun setUp() {
        val certificateFactory : CertificateFactory
        val certificate : Certificate
        val inputStream : InputStream

        try {
            certificateFactory = CertificateFactory.getInstance("X.509")
            inputStream = NodeApplication.instance.context().resources.openRawResource(R.raw.server)

            certificate = certificateFactory.generateCertificate(inputStream)

            val keyStoreType = KeyStore.getDefaultType()
            val keyStore = KeyStore.getInstance(keyStoreType)
            keyStore.load(null, null)
            keyStore.setCertificateEntry("certificate", certificate)

            val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
            trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm)
            trustManagerFactory.init(keyStore)

            sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagerFactory.trustManagers, SecureRandom())

            inputStream.close()
        } catch (e : Exception) { e.printStackTrace() }
    }

    fun setSSLOKHttp(builder : OkHttpClient.Builder, target : String) : OkHttpClient.Builder {
        builder.sslSocketFactory(getInstance().sslContext.socketFactory, (getInstance().trustManagerFactory.trustManagers[0]) as X509TrustManager)

        val hostnameVerifier = HostnameVerifier{ hostname, _ ->
            hostname!!.contentEquals(target)
        }
        builder.hostnameVerifier(hostnameVerifier)

        return builder
    }

    companion object {
        fun getInstance() : SelfSigningHelper = SelfSigingClientBuilderHolder.INSTANCE

        class SelfSigingClientBuilderHolder {
            companion object {
                val INSTANCE : SelfSigningHelper = SelfSigningHelper()
            }
        }
    }
}