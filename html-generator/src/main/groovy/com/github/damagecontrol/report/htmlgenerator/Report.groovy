package com.github.damagecontrol.report.htmlgenerator

import static org.apache.commons.io.FileUtils.copyURLToFile

class Report {

    private static final CSS_URL = getResource('/com/github/damagecontrol/htmlreport/statics/style/damage-control.css')
    private static final JQUERY_URL = getResource('/com/github/damagecontrol/htmlreport/statics/js/jquery-1.7.2.min.js')
    private static final JS_URL = getResource('/com/github/damagecontrol/htmlreport/statics/js/damage-control.js')

    def testResultsFolder
    def testResultsFolders

    def specDefinitionsFolder
    def specDefinitionsFolders

    def outputFolder

    private final collector = new TestResultsCollector()
    private final indexTemplate = new HtmlIndexTemplate()
    private final specTemplate = new HtmlSpecTemplate()

    def run() {
        new XmlFileReader(inputFolders: testResultsFolderList()).forEach { collector.collect(it) }
        TestResults results = collector.results

        HtmlFileWriter writer = new HtmlFileWriter(outputFolder: outputFolder)
        GroovyFileReader reader = new GroovyFileReader(inputFolders: specDefinitionsFolderList())

        writer.write('index', indexTemplate.generate(results))

        results.specs.each { name, spec ->
            spec.parseEachFeatureDefinition(reader.read(name))
            writer.write(name, specTemplate.generate(spec))
        }

        copyURLToFile(CSS_URL, new File(outputFolder.absolutePath + '/style/damage-control.css'))
        copyURLToFile(JQUERY_URL, new File(outputFolder.absolutePath + '/js/jquery.min.js'))
        copyURLToFile(JS_URL, new File(outputFolder.absolutePath + '/js/damage-control.js'))
    }

    private testResultsFolderList() {
        def folders = []

        if (testResultsFolder) {
            folders.add(testResultsFolder)
        }
        if (testResultsFolders) {
            folders.addAll(testResultsFolders)
        }
        folders
    }

    private specDefinitionsFolderList() {
        def folders = []

        if (specDefinitionsFolder) {
            folders.add(specDefinitionsFolder)
        }
        if (specDefinitionsFolders) {
            folders.addAll(specDefinitionsFolders)
        }
        folders
    }
}
