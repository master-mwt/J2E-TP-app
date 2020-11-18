package it.univaq.disim.mwt.postd.business;

import lombok.Getter;

import java.util.Iterator;
import java.util.List;

public class Page<T> {
    @Getter
    private int totalPages;
    @Getter
    private long totalElements;

    @Getter
    private int number;
    @Getter
    private int size;
    @Getter
    private int numberOfElements;
    @Getter
    private List<T> content;

    private boolean hasContent;
    private boolean hasNext;
    private boolean hasPrevious;
    @Getter
    private boolean isFirst;
    @Getter
    private boolean isLast;

    public Page(org.springframework.data.domain.Page<T> page) {
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.numberOfElements = page.getNumberOfElements();
        this.content = page.getContent();
        this.hasContent = page.hasContent();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
    }

    public boolean hasContent() {
        return this.hasContent;
    }

    public boolean hasNext() {
        return this.hasNext;
    }

    public boolean hasPrevious() {
        return this.hasPrevious;
    }

    public Iterator<T> iterator() {
        return this.content.iterator();
    }

    @Override
    public String toString() {
        return "Page[ number: " + this.number + ", size: " + this.size + ", total_pages: " + this.totalPages + ", number_of_elements: " + this.numberOfElements + " ]";
    }
}
