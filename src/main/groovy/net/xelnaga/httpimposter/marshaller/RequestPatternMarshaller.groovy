package net.xelnaga.httpimposter.marshaller

import net.xelnaga.httpimposter.filter.HttpHeaderFilter
import net.xelnaga.httpimposter.filter.PassThroughFilter
import net.xelnaga.httpimposter.model.HttpHeader
import net.xelnaga.httpimposter.model.RequestPattern
import org.apache.commons.codec.binary.Base64

class RequestPatternMarshaller {

    HttpHeaderFilter filter = new PassThroughFilter()

    RequestPattern fromJson(Map json) {

        RequestPattern requestPattern = new RequestPattern(
                uri: json.uri,
                method: json.method,
                body: new String(Base64.decodeBase64((String) json.body))
        )

        json.headers.each { Map header ->

            HttpHeader httpHeader = new HttpHeader(header.name, header.value)
            addMatchableHeader(requestPattern, httpHeader)
        }

        return requestPattern
    }

    private void addMatchableHeader(RequestPattern requestPattern, HttpHeader httpHeader) {

        if (filter.isMatchable(httpHeader)) {
            requestPattern.headers << httpHeader
        }
    }
}
