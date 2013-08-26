#!/bin/env groovy

@Grab('org.apache.pdfbox:pdfbox:1.8.2')
import groovy.json.JsonSlurper
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem

// Define the command-line options
def cli = new CliBuilder(usage: 'pdfCompiler.groovy -l <pdfLibraryFile> -c <contentFile> -o <outputPdf>')
cli.with {
  l(longOpt: 'libraryFile', 'JSON file of PDF source files', args: 1, required: true)
  c(longOpt: 'contentFile', 'JSON file which defines the pages from the PDF source ' +
      'files which the output PDF will contain', args: 1, required: true)
  o(longOpt: 'outputFile', 'Filename of the PDF file to output', args: 1, required: true)
  h('Display help', required: false)
}

// Parse the command-line
def opt = cli.parse(args)
if (!opt) {
  // opt will be null if the command line could not be parsed
  return
} else if (opt.h) {
  // Print usage and return if h is selected
  cli.usage()
  return
}
// Read in the config file that was specified on the command line
def sourcePdfs = new JsonSlurper().parseText(new File(opt.l).text)
def contentItems = new JsonSlurper().parseText(new File(opt.c).text)

// The input PDF files
Map<String, PDDocument> pdfIdToPdf = [:]
Map<String, Integer> pdfIdToOffset = [:]
for(sourcePdf in sourcePdfs) {
  String pdfId = sourcePdf["pdfId"]
  pdfIdToPdf[pdfId] = PDDocument.load(sourcePdf["pdfFilename"])
  pdfIdToOffset[pdfId] = sourcePdf["pageNumberOffset"] as Integer
}

// Create the output document
PDDocument outputPdf = new PDDocument()
PDDocument firstInputPdf = pdfIdToPdf.values().first()
outputPdf.documentInformation = firstInputPdf.documentInformation
outputPdf.documentInformation.title = opt.o
outputPdf.documentInformation.creationDate = Calendar.instance
outputPdf.documentCatalog.viewerPreferences = firstInputPdf.documentCatalog.viewerPreferences

// Creating the PDF bookmarks
// Derived from PDFBox's example CreateBookmarks.java
PDDocumentOutline outline =  new PDDocumentOutline();
outputPdf.documentCatalog.documentOutline = outline

// We will put all the pages to output into this map
Map<String, Map<Integer, PDPage>> pdfIdToPageNumToPage = [:]

// Loop through each of the input PDFs, grabbing whatever pages
// we need from each one
pdfIdToPdf.each { String pdfId, PDDocument inputPdf ->
  // Get a map ready to hold our results
  Map<Integer, PDPage> pageNumToPage = [:]
  pdfIdToPageNumToPage[pdfId] = pageNumToPage

  // Get all the page numbers which we will need from this input PDF
  Set<Integer> pageNumbersToInclude = [] as Set
  for(contentItem in contentItems) {
    if(contentItem["inPdf"] == pdfId) {
      int pageNumber = contentItem["pageNumber"] as int
      int pageLength = contentItem["pageLength"] as int
      (pageNumber..<(pageNumber+pageLength)).each {
        pageNumbersToInclude << it
      }
    }
  }

  // Loop through the input PDF and get all the pages we need out of it
  inputPdf.documentCatalog.allPages.eachWithIndex{ PDPage page, int pageIndex ->
    // The page number specified in the contentItem doesn't take into account
    // the offset between page number and the actual PDF page number, so
    // we add it here via pdfIdToOffset
    int pageNumber = pageIndex + 1 - pdfIdToOffset[pdfId]
    if(pageNumber in pageNumbersToInclude) {
      pdfIdToPageNumToPage[pdfId][pageNumber] = page
    }
  }
}

// Now that we have all the pages in our pdfIdToPageNumToPage Map,
// we will insert them into our output PDF, in the order given in our
// config file's contentItems array
for(contentItem in contentItems) {
  int pageNumber = contentItem["pageNumber"] as int
  int pageLength = contentItem["pageLength"] as int
  String inPdf = contentItem["inPdf"]
  String title = contentItem["title"]

  // Import first page
  PDPage firstPage = pdfIdToPageNumToPage[inPdf][pageNumber]
  PDPage importedPage = importPage(outputPdf, firstPage)

  // Create bookmark for first page
  PDPageFitDestination dest = new PDPageFitDestination();
  dest.page = importedPage
  PDOutlineItem bookmark = new PDOutlineItem();
  bookmark.destination = dest
  bookmark.title = title
  outline.appendChild(bookmark)

  // Import any following pages
  (pageLength-1).times {
    PDPage nextPage = pdfIdToPageNumToPage[inPdf][pageNumber + 1 + it]
    importPage(outputPdf, nextPage)
  }
}

// Whether we want the outline open by default
outline.openNode()

// Save our resulting file
outputPdf.save(opt.o)

// Method derived from PDFBox's Splitter.java
public PDPage importPage(PDDocument outputPdf, PDPage pageToImport) {
  PDPage importedPage = outputPdf.importPage(pageToImport)
  importedPage.cropBox = pageToImport.findCropBox()
  importedPage.mediaBox = pageToImport.findMediaBox()
  importedPage.resources = pageToImport.resources
  importedPage.rotation = pageToImport.findRotation()
  return importedPage
}
