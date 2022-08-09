package co.tiagoaguiar.netflixremake.util

import android.util.Log
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class CategoryTask {

    fun execute(url: String) {
        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            try {
                val requestURL = URL(url) // abrir uma URL
                val urlConnection =
                    requestURL.openConnection() as HttpURLConnection // abrir a conexão
                urlConnection.readTimeout = 2000 // tempo de leitura (2sec)
                urlConnection.connectTimeout = 2000 // tempo de conexão (2sec)

                val statusCode: Int = urlConnection.responseCode

                if (statusCode > 400) {
                    throw IOException("Erro na comunicação com o servidor!")
                }

                val stream = urlConnection.inputStream // sequencia de bytes
                // forma 1: Simples e rápido
                //val jsonAsString = stream.bufferedReader().use{it.readText()} // byte para string

                // forma 2: bytes -> string
                val buffer = BufferedInputStream(stream)
                val jsonAsString = toString(buffer)


                Log.i("Test", jsonAsString)

            } catch (e: IOException) {
                Log.e("test", e.message ?: "Erro desconhecido", e)
            }
        }
    }

    private fun toString(stream: InputStream): String {
        val bytes = ByteArray(1024)
        val baos = ByteArrayOutputStream()
        var read: Int
        while (true) {
            read = stream.read(bytes)
            if (read <= 0 ) {
                break
            }
            baos.write(bytes, 0, read)
        }
        return String(baos.toByteArray())
    }

}