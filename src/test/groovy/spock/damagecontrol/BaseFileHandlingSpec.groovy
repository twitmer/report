package spock.damagecontrol

import static org.apache.commons.io.FileUtils.deleteDirectory
import spock.lang.Shared

class BaseFileHandlingSpec extends BaseSpec {

    @Shared
    def testFolder

    def setup() {
        testFolder = new File('build/' + this.getClass().name)
        deleteDirectory(testFolder);
        testFolder.mkdirs()
    }

    def cleanup() {
        deleteDirectory(testFolder);
    }
}