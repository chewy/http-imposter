package net.xelnaga.httpimposter

import net.xelnaga.httpimposter.factory.ResponsePresetFactory
import net.xelnaga.httpimposter.model.Interaction
import net.xelnaga.httpimposter.model.RequestPattern
import net.xelnaga.httpimposter.model.ResponsePreset
import spock.lang.Specification

class MappedResponseProviderSpec extends Specification {

    ResponseProvider provider

    ResponsePresetFactory mockResponsePresetFactory

    void setup() {

        provider = new MappedResponseProvider()

        mockResponsePresetFactory = Mock(ResponsePresetFactory)
        provider.responsePresetFactory = mockResponsePresetFactory
    }

    def 'get with known request pattern'() {

        given:
            RequestPattern request = Mock(RequestPattern)
            ResponsePreset response = Mock(ResponsePreset)

            Interaction interaction = new Interaction(request, response)

        and:
            provider.add(interaction)

        when:
            ResponsePreset result = provider.get(request)

        then:
            0 * _._

        and:
            result.is(response)
    }

    def 'get with unknown request pattern'() {

        given:
            RequestPattern request = Mock(RequestPattern)
            ResponsePreset response = Mock(ResponsePreset)

        when:
            ResponsePreset result = provider.get(request)

        then:
            1 * mockResponsePresetFactory.makeUnexpected() >> response
            0 * _._

        and:
            result.is(response)
    }

    def 'get with duplicate request pattern'() {

        given:
            RequestPattern request = Mock(RequestPattern)

            ResponsePreset response1 = Mock(ResponsePreset)
            ResponsePreset response2 = Mock(ResponsePreset)

            Interaction interaction1 = new Interaction(request, response1)
            Interaction interaction2 = new Interaction(request, response2)

        and:
            provider.add(interaction1)
            provider.add(interaction2)

        expect:
            provider.get(request).is(response2)
    }

    def 'reset'() {

        given:
            RequestPattern request = Mock(RequestPattern)
            ResponsePreset response = Mock(ResponsePreset)

            Interaction interaction = new Interaction(request, response)

        and:
            provider.add(interaction)

        expect:
            provider.get(request).is(response)

        when:
            provider.reset()

        then:
            provider.get(request) == null
    }
}
