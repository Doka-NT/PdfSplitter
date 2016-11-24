/*
 * Copyright (c) 2016 Soshnikov Artem <213036@skobka.com>
 */

package service;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.multipdf.PageExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import service.exceptions.NoBookmarksException;
import service.interfaces.PdfSplitterInterface;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manipulate PDF document
 */
public class PdfManipulationService implements PdfSplitterInterface {

    public void splitByBookmarks(File pdfFile) throws IOException, NoBookmarksException {
        PDDocument document = PDDocument.load(pdfFile);
        this.splitDocument(
                pdfFile,
                document,
                this.getSplitInfo(document)
        );
    }

    private List<BookmarkSplitInfo> getSplitInfo(PDDocument document) throws IOException, NoBookmarksException {
        ArrayList<BookmarkSplitInfo> list = new ArrayList<>();

        PDDocumentOutline bookmarks = document.getDocumentCatalog().getDocumentOutline();
        if (bookmarks == null) {
            throw new NoBookmarksException();
        }

        PDOutlineItem current = bookmarks.getFirstChild();
        int index = 0;
        while (current != null) {
            PDDestination destination = current.getDestination();
            if (destination instanceof PDPageDestination) {
                int pageNumber = ((PDPageDestination) destination).retrievePageNumber();

                if (list.size() > 0) {
                    int i = list.size() - 1;
                    BookmarkSplitInfo prevItem = list.get(i);
                    prevItem.setEndPage(pageNumber - 1);
                    list.set(i, prevItem);
                }

                BookmarkSplitInfo splitInfo = new BookmarkSplitInfo();
                splitInfo.setStartPage(pageNumber);
                splitInfo.setIndex(index);
                list.add(splitInfo);

                index++;
                System.out.println(pageNumber);
            }
            current = current.getNextSibling();
        }

        int i = list.size() - 1;
        BookmarkSplitInfo prevItem = list.get(i);
        prevItem.setEndPage(document.getNumberOfPages() - 1);
        list.set(i, prevItem);

        return list;
    }

    private void splitDocument(File srcFile, PDDocument document, List<BookmarkSplitInfo> splitInfo) {
        File dir = srcFile.getParentFile();
        String baseName = FilenameUtils.removeExtension(srcFile.getName());
        String ext = FilenameUtils.getExtension(srcFile.getName());
        splitInfo.forEach(info -> {
            PageExtractor extractor = new PageExtractor(document);
            extractor.setStartPage(info.getStartPage()+1);
            extractor.setEndPage(info.getEndPage()+1);

            String fileName = (new StringBuilder(dir.getAbsolutePath()))
                    .append(File.separator)
                    .append(baseName)
                    .append("_")
                    .append(info.getIndex()+1)
                    .append(".")
                    .append(ext)
                    .toString();
            File targetFile = new File(fileName);
            System.out.println(fileName);
            try {
                extractor
                        .extract()
                        .save(targetFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
