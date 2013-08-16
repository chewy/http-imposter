package net.xelnaga.httpimposter.factory

import net.xelnaga.httpimposter.model.BaseResponsePreset
import net.xelnaga.httpimposter.model.ByteArrayResponsePreset
import net.xelnaga.httpimposter.model.ResponsePreset

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR
import net.xelnaga.httpimposter.model.HttpHeader

class ResponsePresetFactory {

    ResponsePreset makeUnexpected() {

        return new ResponsePreset(
                status: SC_INTERNAL_SERVER_ERROR,
                headers: [
                        new HttpHeader('Content-Type', 'text/plain')
                ],
                body: 'UNEXPECTED REQUEST PATTERN'
        )
    }

    BaseResponsePreset makeResponsePreset(String type) {

        if (type == 'ByteArray') {
            return new ByteArrayResponsePreset()
        }

        return new ResponsePreset()
    }
}
