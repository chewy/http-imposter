package net.xelnaga.httpimposter.model

abstract class BaseResponsePreset<T> {

    int status
    Set<HttpHeader> headers = [] as TreeSet
    T body
}
