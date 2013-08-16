package net.xelnaga.httpimposter.marshaller

import net.xelnaga.httpimposter.factory.ResponsePresetFactory
import net.xelnaga.httpimposter.model.HttpHeader
import net.xelnaga.httpimposter.model.ResponsePreset
import spock.lang.Specification

class ResponsePresetMarshallerSpec extends Specification {

    private ResponsePresetMarshaller marshaller

    ResponsePresetFactory mockResponsePresetFactory

    void setup() {

        marshaller = new ResponsePresetMarshaller()

        mockResponsePresetFactory = Mock(ResponsePresetFactory)
        marshaller.responsePresetFactory = mockResponsePresetFactory
    }

    def 'from json'() {

        given:
            Map json = [
                    headers: [
                            [name: 'Content-Type', value: 'text/exciting'],
                            [name: 'Lemon', value: 'Lime']
                    ],
                    type: 'type',
                    status: 234,
                    body: 'Ym9keXRlc3Q2NA=='
            ]

        and:
            ResponsePreset responsePreset = new ResponsePreset()

        when:
            ResponsePreset result = marshaller.fromJson(json)

        then:
            1 * mockResponsePresetFactory.makeResponsePreset('type') >> responsePreset
            0 * _

        and:
            result == new ResponsePreset(
                    status: 234,
                    body: 'bodytest64',
                    headers: [
                            new HttpHeader('Content-Type', 'text/exciting'),
                            new HttpHeader('Lemon', 'Lime')
                    ])
    }
}
