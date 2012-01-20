package net.xelnaga.httpimposter

import javax.servlet.http.HttpServletResponse
import net.xelnaga.httpimposter.model.ImposterResponse

class ImposterResponseWriter {

    HttpServletResponse write(ImposterResponse imposterResponse, HttpServletResponse httpResponse) {

        httpResponse.status = imposterResponse.status

        imposterResponse.headers.each { String name, String value ->
            httpResponse.addHeader(name, value)
        }

        httpResponse.outputStream << imposterResponse.body

        return httpResponse
    }
}
