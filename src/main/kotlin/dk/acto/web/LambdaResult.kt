package dk.acto.web

class LambdaResult(val body: String, val headers: Map<String, String>) {
    val isBase64Encoded = false
    val statusCode = 200
}
