/*
 * Copyright (c) 2016 Soshnikov Artem <213036@skobka.com>
 */

package service;

/**
 * Split information fp PdfManipulationService
 */
class BookmarkSplitInfo {
    private int startPage;
    private int endPage;
    private int index;

    int getStartPage() {
        return startPage;
    }

    void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    int getEndPage() {
        return endPage;
    }

    void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    int getIndex() {
        return index;
    }

    void setIndex(int index) {
        this.index = index;
    }
}
