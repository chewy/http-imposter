package net.xelnaga.httpimposter.factory

import net.xelnaga.httpimposter.model.BaseResponsePreset
import net.xelnaga.httpimposter.model.ByteArrayResponsePreset
import net.xelnaga.httpimposter.model.HttpHeader
import net.xelnaga.httpimposter.model.ResponsePreset
import spock.lang.Specification
import spock.lang.Unroll

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR

class ResponsePresetFactorySpec extends Specification {

    ResponsePresetFactory factory

    void setup() {
        factory = new ResponsePresetFactory()
    }

    def 'make unexpected'() {

        when:
            ResponsePreset responsePreset = factory.makeUnexpected()

        then:
            responsePreset == new ResponsePreset(
                    status: SC_INTERNAL_SERVER_ERROR,
                    headers: [
                            new HttpHeader('Content-Type', 'text/plain')
                    ],
                    body: 'UNEXPECTED REQUEST PATTERN'
            )
    }

    @Unroll
    def 'make response preset by type'() {

        when:
            BaseResponsePreset responsePreset = factory.makeResponsePreset(type)

        then:
            responsePreset.class == expected

        where:
            type        | expected
            'String'    | ResponsePreset
            'ByteArray' | ByteArrayResponsePreset
            null        | ResponsePreset
    }
}
