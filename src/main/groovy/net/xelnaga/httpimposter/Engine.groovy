package net.xelnaga.httpimposter

import net.xelnaga.httpimposter.factory.ResponsePresetFactory
import net.xelnaga.httpimposter.model.Interaction
import net.xelnaga.httpimposter.model.Report
import net.xelnaga.httpimposter.model.RequestPattern
import net.xelnaga.httpimposter.model.ResponsePreset

class Engine {

    private List<Interaction> expectations
    private List<Interaction> interactions

    ResponseProvider responseProvider
    ResponsePresetFactory responsePresetFactory

    Engine() {

        expectations = []
        interactions = []

        responseProvider = new MappedResponseProvider(
                responsePresetFactory: responsePresetFactory
        )
    }

    Report getReport() {
        return new Report(expectations, interactions)
    }

    void expect(Interaction expectation) {

        expectations << expectation
        responseProvider.add(expectation)
    }

    Interaction interact(RequestPattern request) {

        ResponsePreset response = responseProvider.get(request)

        Interaction interaction = new Interaction(request, response)
        interactions << interaction

        return interaction
    }

    void reset() {

        expectations.clear()
        interactions.clear()

        responseProvider.reset()
    }
}
