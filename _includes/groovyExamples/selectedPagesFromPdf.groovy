#!/bin/env groovy

@Grab('org.apache.pdfbox:pdfbox:1.8.2')
import groovy.json.JsonSlurper
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem

// Method derived from PDFBox's Splitter.java
public PDPage importPage(PDDocument outputPdf, PDPage pageToImport) {
  PDPage importedPage = outputPdf.importPage(pageToImport)
  importedPage.cropBox = pageToImport.findCropBox()
  importedPage.mediaBox = pageToImport.findMediaBox()
  importedPage.resources = pageToImport.resources
  importedPage.rotation = pageToImport.findRotation()
  return importedPage
}

// Read in the config file that was specified on the command line
def config = new JsonSlurper().parseText(new File(args[0]).text)

// The input PDF files
Map<String, PDDocument> pdfIdToPdf = [:]
Map<String, Integer> pdfIdToOffset = [:]
for(sourcePdf in config["sourcePdfs"]) {
  String pdfId = sourcePdf["pdfId"]
  pdfIdToPdf[pdfId] = PDDocument.load(sourcePdf["pdfFilename"])
  pdfIdToOffset[pdfId] = sourcePdf["pageNumberOffset"] as Integer
}

// Create the output document
PDDocument outputPdf = new PDDocument()
PDDocument firstInputPdf = pdfIdToPdf.values().first()
outputPdf.documentInformation = firstInputPdf.documentInformation
outputPdf.documentInformation.title = config["outputPdfTitle"]
outputPdf.documentInformation.creationDate = Calendar.instance
outputPdf.documentCatalog.viewerPreferences = firstInputPdf.documentCatalog.viewerPreferences

// Creating the PDF bookmarks
// Derived from PDFBox's example CreateBookmarks.java
PDDocumentOutline outline =  new PDDocumentOutline();
outputPdf.documentCatalog.documentOutline = outline

// Create top-level bookmark
PDOutlineItem pagesOutline = new PDOutlineItem();
pagesOutline.title = config["outputPdfTitle"]
outline.appendChild(pagesOutline);

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
  for(contentItem in config["contentItems"]) {
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
for(contentItem in config["contentItems"]) {
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
  pagesOutline.appendChild(bookmark)

  // Import any following pages
  (pageLength-1).times {
    PDPage nextPage = pdfIdToPageNumToPage[inPdf][pageNumber + 1 + it]
    importPage(outputPdf, nextPage)
  }
}

// Whether we want the outline open by default
pagesOutline.openNode()
outline.openNode()

// Save our resulting file
outputPdf.save(config["outputPdfFilename"])
