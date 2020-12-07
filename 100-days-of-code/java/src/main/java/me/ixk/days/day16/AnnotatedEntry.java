/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day16;

import java.lang.reflect.AnnotatedElement;
import me.ixk.days.day5.AnnotationUtils;
import me.ixk.days.day5.MergedAnnotation;

/**
 * AnnotatedEntry
 *
 * @author Otstar Lin
 * @date 2020/11/9 下午 8:14
 */
public class AnnotatedEntry<E extends AnnotatedElement> {

    private final E element;
    private final MergedAnnotation annotation;

    public AnnotatedEntry(final E element) {
        this.element = element;
        this.annotation = AnnotationUtils.getAnnotation(element);
    }

    public AnnotatedEntry(final E element, final MergedAnnotation annotation) {
        this.element = element;
        this.annotation = annotation;
    }

    public E getElement() {
        return element;
    }

    public MergedAnnotation getAnnotation() {
        return annotation;
    }

    public static class ChangeableEntry<E extends AnnotatedElement>
        extends AnnotatedEntry<E> {

        private boolean changed = false;

        public ChangeableEntry(E element) {
            super(element);
        }

        public ChangeableEntry(E element, MergedAnnotation annotation) {
            super(element, annotation);
        }

        public ChangeableEntry(final AnnotatedEntry<E> annotatedEntry) {
            super(annotatedEntry.getElement(), annotatedEntry.getAnnotation());
        }

        public boolean isChanged() {
            return changed;
        }

        public void setChanged(final boolean changed) {
            this.changed = changed;
        }
    }
}
