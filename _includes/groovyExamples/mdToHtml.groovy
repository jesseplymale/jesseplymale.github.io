@Grab(group='org.pegdown', module='pegdown', version='1.4.1')
import groovy.xml.MarkupBuilder
import org.pegdown.PegDownProcessor

def pdp = new PegDownProcessor()
new MarkupBuilder().html {
    head {
        title(args[0])
    }
    body {
        mkp.yieldUnescaped(pdp.markdownToHtml(new File(args[0]).text))
    }
}