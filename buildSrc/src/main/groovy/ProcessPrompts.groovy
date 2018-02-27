import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*

import java.util.regex.Pattern

class ProcessPrompts extends DefaultTask {

    @Input
    Property<Pattern> pattern = project.objects.property(Pattern)

    @OutputFile
    final RegularFileProperty destFile = newOutputFile()

    @TaskAction
    void process() {
        project.copy {
            from project.configurations.data
            into project.buildDir
            eachFile { fileDetails ->
                destFile.get().asFile.withWriter { writer ->
                    writer.println "---"
                    writer.println "header-includes: |"
                    writer.println "    \\usepackage{media9}"
                    writer.println "    \\usepackage{etoolbox}"
                    writer.println "    \\appto\\frame{%"
                    writer.println "      \\includemedia[addresource=beep.mp3, width=1pt, height=1pt, activate=pageopen, noplaybutton, attachfiles, flashvars={%"
                    writer.println "        source=beep.mp3&autoPlay=true&hideBar=true"
                    writer.println "      }]{}{APlayer.swf}"
                    writer.println "    }"
                    writer.println "revealjs-url: https://revealjs.com"
                    writer.println "include-after:"
                    writer.println "  - <audio src='beep.mp3' id='beep'></audio>"
                    writer.println "  - <script>Reveal.addEventListener( 'slidechanged', function( event ) { var beep = document.getElementById('beep'); beep.play(); } );</script>"
                    writer.println "..."
                    writer.println()

                    fileDetails.file.eachLine { line ->
                        if (line.startsWith(';'))
                            return
                        def matcher = line =~ pattern.get()
                        if (matcher.matches()) {
                            writer.println "## ${matcher.group('prompt')}"
                            writer.println()
                            writer.println matcher.group('code')
                            writer.println()
                        }
                    }
                }
                fileDetails.exclude()
            }
        }
    }
}
