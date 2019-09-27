/*
 * Copyright 2019 KTM Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ktm_technologies.markovspeech;



import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class MarkovChainTest {

    private final static int _WINDOW = 1;

    @Test
    public void markov_ctor() {
        MarkovChain mc = new MarkovChain(MarkovChainTest._WINDOW);
    }

    @Test
    public void markov_scanEmptyEmpty() {
        MarkovChain mc = new MarkovChain(MarkovChainTest._WINDOW);
        double result = mc.scan(new LinkedList<String>(), null);
        assertEquals(-1.0, result, 0.0001);
    }

    @Test
    public void markov_matchEmptyEmpty() {
        MarkovChain mc = new MarkovChain(MarkovChainTest._WINDOW);
        double result = mc.match(new LinkedList<String>());
        assertEquals(-1.0, result, 0.0001);
    }

    @Test
    public void markov_matchEmptyModel() {
        MarkovChain mc = new MarkovChain(MarkovChainTest._WINDOW);
        List<String> phrase = new LinkedList<>(Arrays.asList("foo", "bar"));
        double result = mc.match(phrase);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void markov_scanEmptyModel() {
        MarkovChain mc = new MarkovChain(MarkovChainTest._WINDOW);
        List<String> phrase = new LinkedList<>(Arrays.asList("foo", "bar"));
        double result = mc.scan(phrase, null);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void markov_createChain() {
        MarkovChain mc = createFooBarBazChain();
    }

    @Test
    public void markov_matchSingle() {
        MarkovChain mc = createFooBarBazChain();
        List<String> phrase = new LinkedList<>(Arrays.asList("foo", "bar", "baz"));
        double result = mc.match(phrase);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void markov_scanSingleFull() {
        MarkovChain mc = createFooBarBazChain();
        List<String> phrase = new LinkedList<>(Arrays.asList("foo", "bar", "baz"));
        double result = mc.scan(phrase, null);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void markov_scanSingleStart() {
        MarkovChain mc = createFooBarBazChain();
        List<String> phrase = new LinkedList<>(Arrays.asList("foo", "bar"));
        double result = mc.scan(phrase, null);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void markov_scanSingleEnd() {
        MarkovChain mc = createFooBarBazChain();
        List<String> phrase = new LinkedList<>(Arrays.asList("bar", "baz"));
        double result = mc.scan(phrase, null);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void markov_scanSingleFullLong() {
        MarkovChain mc = createFooBarBazChain();
        List<String> phrase = new LinkedList<>(Arrays.asList("foo", "bar", "baz", "maman"));
        double result = mc.scan(phrase, null);
        // 2 out of 3 edges with probability 1.0 match
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void markov_scanSingleStartLong() {
        MarkovChain mc = createFooBarBazChain();
        List<String> phrase = new LinkedList<>(Arrays.asList("foo", "bar", "maman"));
        double result = mc.scan(phrase, null);
        // 1 out of 2 edges with probability 1.0 match
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void markov_scanSingleEndLong() {
        MarkovChain mc = createFooBarBazChain();
        List<String> phrase = new LinkedList<>(Arrays.asList("bar", "baz", "maman"));
        double result = mc.scan(phrase, null);
        // 1 out of 2 edges with probability 1.0 match
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void markov_scanDetails() {
        MarkovChain mc = new MarkovChain(MarkovChainTest._WINDOW);
        List<String> model = new LinkedList<>(Arrays.asList("a", "b", "c", "d", "e", "f"));
        List<String> phrase = new LinkedList<>(Arrays.asList("x", "y", "c", "d", "e", "z"));
        MatchResults details = new MatchResults();
        mc.train(model);
        mc.scan(phrase, details);
        MatchResults.Entry entry = details.getEntries().getFirst();
        assertArrayEquals(entry.getMatchPhrase().toArray(), new String[] {"c", "d", "e"});
        assertEquals(entry.getAvgProbability(), 1.0, 0.0001);
        assertEquals(entry.getOffset(), 2);
    }

    @Test
    public void markov_scanSubPhrases() {
        MarkovChain mc = new MarkovChain(MarkovChainTest._WINDOW);
        List<String> model = new LinkedList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h"));
        List<String> match = new LinkedList<>(Arrays.asList("x", "b", "c", "y", "d", "e", "f", "z"));
        MatchResults details = new MatchResults();
        mc.train(model);
        mc.scan(match, details);
        MatchResults.Entry entry;
        // First match
        entry = details.getEntries().getFirst();
        assertArrayEquals(entry.getMatchPhrase().toArray(), new String[] {"b", "c"});
        assertEquals(entry.getAvgProbability(), 1.0, 0.0001);
        assertEquals(entry.getOffset(), 1);
        // Second match
        entry = details.getEntries().getLast();
        assertArrayEquals(entry.getMatchPhrase().toArray(), new String[] {"d", "e", "f"});
        assertEquals(entry.getAvgProbability(), 1.0, 0.0001);
        assertEquals(entry.getOffset(), 4);
    }

    @Test
    public void markov_matchW2() {
        MarkovChain mc = MarkovChainTest.createFoxChainW2();
        List<String> phrase = new LinkedList<>(Arrays.asList("the quick brown fox jumps over the lazy dog".split( " ")));
        double result = mc.match(phrase);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void markov_scanW2Full() {
        MarkovChain mc = MarkovChainTest.createFoxChainW2();
        List<String> phrase = new LinkedList<>(Arrays.asList("the quick brown fox jumps over the lazy dog".split( " ")));
        MatchResults details = new MatchResults();
        double result = mc.scan(phrase, details);
        assertEquals(1.0, result, 0.0001);
        assertEquals(1, details.getEntries().size());
    }

    @Test
    public void markov_scanW2Sub() {
        MarkovChain mc = MarkovChainTest.createFoxChainW2();
        List<String> phrase = new LinkedList<>(Arrays.asList("fox jumps over".split( " ")));
        MatchResults details = new MatchResults();
        double result = mc.scan(phrase, details);
        assertEquals(1.0, result, 0.0001);
        assertEquals(1, details.getEntries().size());
    }

    @Test
    public void markov_testLabel() {

        boolean ret = MarkovChain.testLabel();
        assertTrue(ret);
    }

    static MarkovChain createFooBarBazChain() {
        MarkovChain mc = new MarkovChain(MarkovChainTest._WINDOW);
        List<String> phrase = new LinkedList<>(Arrays.asList("foo", "bar", "baz"));
        mc.train(phrase);
        return mc;
    }

    @SuppressWarnings("unchecked")
    static MarkovChain createDiamondChain() {
        MarkovChain mc = new MarkovChain(MarkovChainTest._WINDOW);
        Object[] phrases = {
                new LinkedList<>(Arrays.asList("a", "b", "c")),
                new LinkedList<>(Arrays.asList("a", "d", "c")),
                new LinkedList<>(Arrays.asList("a", "e", "c"))
        };
        for (Object phrase : phrases) {
            mc.train((List<String>) phrase);
        }
        return mc;
    }

    static MarkovChain createFoxChainW1() {
        MarkovChain mc = new MarkovChain(1);
        List<String> phrase = new LinkedList<>(Arrays.asList("the quick brown fox jumps over the lazy dog".split( " ")));
        mc.train(phrase);
        return mc;
    }

    static MarkovChain createFoxChainW2() {
        MarkovChain mc = new MarkovChain(2);
        List<String> phrase = new LinkedList<>(Arrays.asList("the quick brown fox jumps over the lazy dog".split( " ")));
        mc.train(phrase);
        return mc;
    }

    static MarkovChain createFishChainW1() {
        MarkovChain mc = new MarkovChain(1);
        List<String> phrase = new LinkedList<>(Arrays.asList("one fish two fish red fish blue fish".split( " ")));
        mc.train(phrase);
        return mc;
    }

    static MarkovChain createFishChainW2() {
        MarkovChain mc = new MarkovChain(2);
        List<String> phrase = new LinkedList<>(Arrays.asList("one fish two fish red fish blue fish".split( " ")));
        mc.train(phrase);
        return mc;
    }
}