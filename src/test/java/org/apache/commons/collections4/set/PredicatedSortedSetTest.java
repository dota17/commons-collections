/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4.set;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.Test;

import org.apache.commons.collections4.BulkTest;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;

/**
 * Extension of {@link AbstractSortedSetTest} for exercising the
 * {@link PredicatedSortedSet} implementation.
 *
 * @since 3.0
 */
public class PredicatedSortedSetTest<E> extends AbstractSortedSetTest<E> {

    public PredicatedSortedSetTest(final String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(PredicatedSortedSetTest.class);
    }

    protected Predicate<E> truePredicate = TruePredicate.<E>truePredicate();

    @Override
    public SortedSet<E> makeObject() {
        return PredicatedSortedSet.predicatedSortedSet(new TreeSet<E>(), truePredicate);
    }

    @Override
    public SortedSet<E> makeFullCollection() {
        final TreeSet<E> set = new TreeSet<>();
        set.addAll(Arrays.asList(getFullElements()));
        return PredicatedSortedSet.predicatedSortedSet(set, truePredicate);
    }

    protected Predicate<E> testPredicate =
        o -> o instanceof String && ((String) o).startsWith("A");

    protected PredicatedSortedSet<E> makeTestSet() {
        return PredicatedSortedSet.predicatedSortedSet(new TreeSet<E>(), testPredicate);
    }

    public void testGetSet() {
        final PredicatedSortedSet<E> set = makeTestSet();
        assertTrue(set.decorated() != null);
    }

    @SuppressWarnings("unchecked")
    public void testIllegalAdd() {
        final SortedSet<E> set = makeTestSet();
        final String testString = "B";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            set.add((E) testString);
        });
        assertTrue(exception.getMessage().contains("Cannot add Object 'B'"));
        assertTrue(!set.contains(testString));
    }

    @SuppressWarnings("unchecked")
    public void testIllegalAddAll() {
        final SortedSet<E> set = makeTestSet();
        final Set<E> elements = new TreeSet<>();
        elements.add((E) "Aone");
        elements.add((E) "Atwo");
        elements.add((E) "Bthree");
        elements.add((E) "Afour");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            set.addAll(elements);
        });
        assertTrue(exception.getMessage().contains("Cannot add Object 'Bthree'"));
        assertTrue(!set.contains("Aone"));
        assertTrue(!set.contains("Atwo"));
        assertTrue(!set.contains("Bthree"));
        assertTrue(!set.contains("Afour"));
    }

    public void testComparator() {
        final SortedSet<E> set = makeTestSet();
        final Comparator<? super E> c = set.comparator();
        assertNull(c);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) getCollection(), "src/test/resources/data/test/PredicatedSortedSet.emptyCollection.version4.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) getCollection(), "src/test/resources/data/test/PredicatedSortedSet.fullCollection.version4.obj");
//    }

}
