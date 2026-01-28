package com.chandra.syncnote.github.handleExpection

import com.chandra.syncnote.github.NetworkResponse
import org.json.JSONException
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * HTTP status codes are standardized responses from a server to a client's request. While there are many HTTP status codes, here are some of the common ones along with the usual messages that developers often display to users:
 *
 * 1. **2xx Success:**
 *    - **200 OK:** The request was successful.
 *      - **Message:** "Request was successful."
 *    - **201 Created:** The request was successful, and a new resource was created.
 *      - **Message:** "Resource created successfully."
 *    - **204 No Content:** The request was successful, but there is no response body to return.
 *      - **Message:** "Request was successful, but there is no content to display."
 *
 * 2. **3xx Redirection:**
 *    - **301 Moved Permanently:** The requested resource has been permanently moved to a new URL.
 *      - **Message:** "The requested page has been moved permanently to a new location."
 *    - **304 Not Modified:** The resource has not been modified since the last request.
 *      - **Message:** "The resource has not been modified since your last request."
 *
 * 3. **4xx Client Errors:**
 *    - **400 Bad Request:** The server cannot process the request due to a client error.
 *      - **Message:** "Bad request. Please check your request and try again."
 *    - **401 Unauthorized:** The client must authenticate to get the requested response.
 *      - **Message:** "Unauthorized access. Please log in to access this resource."
 *    - **403 Forbidden:** The client does not have the necessary permissions to access the resource.
 *      - **Message:** "Access forbidden. You don't have permission to access this resource."
 *    - **404 Not Found:** The requested resource could not be found on the server.
 *      - **Message:** "Resource not found. The requested page does not exist."
 *    - **429 Too Many Requests:** The client has sent too many requests in a given amount of time.
 *      - **Message:** "Too many requests. Please try again later."
 *
 * 4. **5xx Server Errors:**
 *    - **500 Internal Server Error:** A generic error message, given when the server encounters an error and no more specific message is suitable.
 *      - **Message:** "Internal server error. Please try again later."
 *    - **502 Bad Gateway:** The server, while acting as a gateway or proxy, received an invalid response from the upstream server it accessed in attempting to fulfill the request.
 *      - **Message:** "Bad gateway. There was an error in processing your request."
 *    - **503 Service Unavailable:** The server is not ready to handle the request.
 *      - **Message:** "Service unavailable. The server is temporarily unable to handle your request due to maintenance downtime or capacity problems."
 *
 * Please note that these messages are generic and can be customized based on the specific context and user experience requirements of your application.
 */

/**
 *
 * This method handles all the Http Response and then pass response state (NetWorkResponse.Failure) from Repository -> ViewModel-> UI
 * @param throwable
 * @return
 */
fun HandleRetrofitExceptions(throwable: Throwable): NetworkResponse.Failure {
    return when (throwable) {
        is UnknownHostException -> {
            NetworkResponse.Failure(
                null,
                "Unable to connect to the server. Please check your internet connection.",
                throwable.cause
            )
        }

        is SocketTimeoutException -> {
            NetworkResponse.Failure(
                null,
                "Connection Time Out",
                throwable.cause
            )
        }

        is ConnectException -> {
            NetworkResponse.Failure(
                null,
                "Server refused the connection. Please try again later.",
                throwable.cause
            )
        }

        is IOException -> NetworkResponse.Failure(
            null,
            "An error occurred while communicating with the server. Please check your internet connection.",
            throwable.cause
        )

        is HttpException -> {
            NetworkResponse.Failure(
                throwable.code(),
                "HTTP Error: ${throwable.code()} ${throwable.message()}",
                throwable.cause
            )
        }

        is JSONException -> {
            NetworkResponse.Failure(
                null,
                "Error parsing server response. Please try again later.",
                throwable.cause
            )
        }

        else -> NetworkResponse.Failure(
            null,
            "An unexpected error occurred: ${throwable.message}",
            throwable
        )
    }
}

/**
 * It is generally not a good practice to show the exact HTTP exception response directly to users on the UI
 * in a production environment. HTTP error codes and messages are technical details meant for developers, not end-users.
 * Displaying raw error messages could potentially expose sensitive information about your server or system internals,
 * which could be exploited by malicious users.
 *
 * However, it is essential to log detailed error information, including the HTTP status code, response body
 * (in a sanitized form, without sensitive information), and any other relevant details on the server or in your logging system.
 *
 * @param response
 * @return
 */
fun handleHttpException(response: Response<*>): NetworkResponse.Failure {

    return when (response.code()) {

        400 -> {
            NetworkResponse.Failure(
                response.code(),
                "Bad Request: The server could not understand the request.",
                null
            )
        }

        401 -> {
            NetworkResponse.Failure(
                response.code(),
                "Token authentication failed. Kindly login again to continue",
                null
            )
        }

        403 -> {
            NetworkResponse.Failure(
                response.code(),
                "Forbidden: Access is forbidden to the requested resource.",
                null
            )
        }

        404 -> {
            NetworkResponse.Failure(
                response.code(),
                "Not Found: The requested resource was not found on the server.",
                null
            )
        }

        408 -> {
            NetworkResponse.Failure(
                response.code(),
                "Request Timeout: The server timed out waiting for the request.",
                null
            )
        }

        500 -> {
            NetworkResponse.Failure(
                response.code(),
                "Internal Server Error: Something went wrong on the server.",
                null
            )
        }

        502 -> {
            NetworkResponse.Failure(
                response.code(),
                "Bad Gateway: The server received an invalid response from an upstream server.",
                null
            )
        }

        503 -> {
            NetworkResponse.Failure(
                response.code(),
                "Service Unavailable: The server is temporarily unavailable.",
                null
            )
        }

        504 -> {
            NetworkResponse.Failure(
                response.code(),
                "Gateway Timeout: The server did not receive a timely response from the upstream server.",
                null
            )
        }

        else -> {
            NetworkResponse.Failure(
                response.code(),
                "Unexpected Server Error: ${response.code()} ${response.message()}",
                null
            )
        }
    }

}


