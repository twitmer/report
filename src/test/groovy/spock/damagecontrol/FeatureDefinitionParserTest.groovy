package spock.damagecontrol

class FeatureDefinitionParserTest extends BaseSpec {

    FeatureDefinitionParser parser

    def setup() {
        given:
        parser = new FeatureDefinitionParser()
    }

    def 'should parse feature definition'() {
        given:
        String sourceCode = '''
class Spec1 {
    def 'feature name'() {
        for (;;){}
        expect: 'some block'
        // some blocks
    }
    def 'other feature'() {
        // other blocks
    }
}
'''
        when:
        def featureDefinition = parser.parse('feature name', sourceCode)

        then:
        featureDefinition == '''
        for (;;)
        expect: 'some block'
        // some blocks
    '''
    }

    def 'should ignore feature if it is not defined in the source code'() {
        given:
        String sourceCode = '''
class Spec1 {
    def 'some feature that does not match'() {
        for (;;) {}
        // some blocks
    }
}
'''

        when:
        def featureDefinition = parser.parse('feature name', sourceCode)

        then:
        featureDefinition == ''
    }
}
