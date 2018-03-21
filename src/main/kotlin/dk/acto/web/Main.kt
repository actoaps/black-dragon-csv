package dk.acto.web

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.amazonaws.util.StringInputStream
import com.google.gson.Gson
import com.google.gson.JsonParser
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.text.DecimalFormat
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

fun main(args : Array<String>) {
   Main().handleRequest(StringInputStream(args.getOrElse(0, {""})), System.out, null)
}

class Main : RequestStreamHandler {
    override fun handleRequest(input: InputStream?, output: OutputStream?, context: Context?) {

        val temp = input?.bufferedReader()?.readText()

        val myInput = try {
            JsonParser().parse(temp)
                    .asJsonObject.get("pathParameters")
                    .asJsonObject.get("proxy")
                    .asString
        } catch (e: Exception){ temp.orEmpty() }

        val myOutput = LambdaResult(generateData(myInput), mapOf("Content-Type" to "text/csv", "Content-Disposition" to "filename=dragon.csv"))

        output?.bufferedWriter()?.use { it.write(Gson().toJson(myOutput)) }
    }

    private val format: DecimalFormat = DecimalFormat("#.##")

    private fun generateData(input: String) : String {
        val seed = ByteBuffer.wrap(input.padStart(8, Char.MIN_SURROGATE).toByteArray()).long
        val random = Random(seed)
        val sb = StringBuilder()
        val blah = Value::class.declaredMemberProperties.shuffled(random)

        sb.append(title(blah))

        for (i in 1..random.nextInt(5) + 10) {
            val item = Value(random)
            sb.append(row(blah, item))
        }
        return sb.toString()
    }

    private fun title(source: Collection<KProperty1<Value, *>>): Any {
        return source.joinToString(separator = ", ", postfix = "\n") { it.name }
    }

    private fun row(source: Collection<KProperty1<Value, *>>, item: Value): Any {
        return source.joinToString(separator = ", ", postfix = "\n") { format.format(it.get(item)) }
    }
}
