package net.xelnaga.httpimposter

import net.xelnaga.httpimposter.model.BaseResponsePreset
import net.xelnaga.httpimposter.model.HttpHeader
import net.xelnaga.httpimposter.model.Report
import net.xelnaga.httpimposter.serializer.ReportSerializer
import net.xelnaga.httpimposter.serializer.json.JsonReportSerializer

import javax.servlet.http.HttpServletResponse

class ResponseWriter {

    ReportSerializer reportSerializer = new JsonReportSerializer()

    HttpServletResponse write(BaseResponsePreset responsePreset, HttpServletResponse httpResponse) {

        httpResponse.status = responsePreset.status

        responsePreset.headers.each { HttpHeader httpHeader ->
            httpResponse.addHeader(httpHeader.name, httpHeader.value)
        }

        httpResponse.outputStream << responsePreset.body

        return httpResponse
    }

    HttpServletResponse write(Report report, HttpServletResponse response) {

        response.setContentType('application/json')
        response.outputStream << reportSerializer.serialize(report)

        return response
    }
}
