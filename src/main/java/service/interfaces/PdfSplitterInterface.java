/*
 * Copyright (c) 2016 Soshnikov Artem <213036@skobka.com>
 */

package service.interfaces;

import service.exceptions.NoBookmarksException;

import java.io.File;
import java.io.IOException;

public interface PdfSplitterInterface {
    /**
     * Split PDF file by top level bookmarks
     * @param pdfFile file to be processed
     */
    void splitByBookmarks(File pdfFile) throws IOException, NoBookmarksException;
}
