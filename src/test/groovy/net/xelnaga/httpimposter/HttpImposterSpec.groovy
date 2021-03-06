package net.xelnaga.httpimposter

import com.google.gson.Gson
import net.xelnaga.httpimposter.factory.RequestPatternFactory
import net.xelnaga.httpimposter.filter.HeaderNameExclusionFilter
import net.xelnaga.httpimposter.filter.HttpHeaderFilter
import net.xelnaga.httpimposter.marshaller.RequestPatternMarshaller
import net.xelnaga.httpimposter.marshaller.ResponsePresetMarshaller
import net.xelnaga.httpimposter.model.Interaction
import net.xelnaga.httpimposter.model.Report
import net.xelnaga.httpimposter.model.RequestPattern
import net.xelnaga.httpimposter.model.ResponsePreset
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class HttpImposterSpec extends Specification {

    HttpImposter httpImposter

    RequestPatternFactory mockRequestPatternFactory
    RequestPatternMarshaller mockRequestPatternMarshaller
    ResponsePresetMarshaller mockResponsePresetMarshaller

    Engine mockEngine

    ResponseWriter mockResponseWriter

    void setup() {

        httpImposter = new HttpImposter()

        mockRequestPatternFactory = Mock(RequestPatternFactory)
        httpImposter.requestPatternFactory = mockRequestPatternFactory

        mockRequestPatternMarshaller = Mock(RequestPatternMarshaller)
        httpImposter.requestPatternMarshaller = mockRequestPatternMarshaller

        mockResponsePresetMarshaller = Mock(ResponsePresetMarshaller)
        httpImposter.responsePresetMarshaller = mockResponsePresetMarshaller

        mockEngine = Mock(Engine)
        httpImposter.engine = mockEngine

        mockResponseWriter = Mock(ResponseWriter)
        httpImposter.responseWriter = mockResponseWriter
    }

    def 'set filter'() {

        given:
            HttpHeaderFilter filter = new HeaderNameExclusionFilter(['qwerty'])

        when:
            httpImposter.setFilter(filter)

        then:
            1 * mockRequestPatternFactory.setProperty('filter', filter)
        then:
            1 * mockRequestPatternMarshaller.setProperty('filter', filter)
            0 * _._
    }

    def 'expect'() {
        
        given:
            HttpServletRequest httpRequest = new MockHttpServletRequest()
            httpRequest.content = new Gson().toJson([cardinality: 4, requestPattern: [qwerty: 'qwerty'], responsePreset: [asdfgh: 'asdfgh']]).bytes

            RequestPattern requestPattern = new RequestPattern(body: 'apple')
            ResponsePreset responsePreset = new ResponsePreset(body: 'pear')
            Interaction interaction = new Interaction(requestPattern, responsePreset)

        when:
            httpImposter.expect(httpRequest)

        then:
            1 * mockRequestPatternMarshaller.fromJson([qwerty: 'qwerty']) >> requestPattern
        then:
            1 * mockResponsePresetMarshaller.fromJson([asdfgh: 'asdfgh']) >> responsePreset
        then:
            1 * mockEngine.expect(interaction)
            0 * _._
    }

    def 'interact'() {
    
        given:
            HttpServletRequest httpRequest = new MockHttpServletRequest(content: 'apple'.bytes)
            HttpServletResponse httpResponse = new MockHttpServletResponse()

            RequestPattern requestPattern = new RequestPattern(body: 'hello')
            ResponsePreset responsePreset = new ResponsePreset(status: 234, body: 'pear')
            Interaction interaction = new Interaction(requestPattern, responsePreset)

        when:
            httpImposter.interact(httpRequest, httpResponse)

        then:
            1 * mockRequestPatternFactory.fromHttpRequest(httpRequest) >> requestPattern
        then:
            1 * mockEngine.interact(requestPattern) >> interaction
        then:
            1 * mockResponseWriter.write(responsePreset, httpResponse)
            0 *_._
    }

    def 'report'() {

        given:
            HttpServletResponse response = new MockHttpServletResponse()
            Report mockReport = Mock(Report)

        when:
            httpImposter.report(response)

        then:
            1 * mockEngine.getReport() >> mockReport
        then:
            1 * mockResponseWriter.write(mockReport, response)
            0 * _._
    }

    def 'reset'() {

        when:
            httpImposter.reset()

        then:
            1 * mockEngine.reset()
            0 * _._
    }
}
