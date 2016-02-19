/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.configuration.data;

import org.junit.Test;
import org.seedstack.configuration.data.ArrayNode;
import org.seedstack.configuration.data.TreeNode;
import org.seedstack.configuration.data.ValueNode;

import static org.assertj.core.api.Assertions.assertThat;

public class ArrayNodeTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testValue() {
        new ArrayNode("plop").value();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNode() {
        new ArrayNode("plop").value("plop");
    }

    @Test
    public void testChildNodes() {
        TreeNode[] treeNodes = new ArrayNode("foo", "bar").values();
        assertThat(treeNodes).hasSize(2);
        assertThat(treeNodes[0].value()).isEqualTo("foo");
        assertThat(treeNodes[1].value()).isEqualTo("bar");
    }

    @Test
    public void testMerge() {
        ArrayNode arrayNode1 = new ArrayNode("foo", "bar");
        ArrayNode arrayNode2 = new ArrayNode("foo", "bar");
        assertThat(arrayNode1.merge(arrayNode2)).isSameAs(arrayNode2);

        ArrayNode arrayNode3 = new ArrayNode("foo", "fuu");
        assertThat(arrayNode1.merge(arrayNode3)).isSameAs(arrayNode3);
    }

    @Test
    public void testEquals() {
        assertThat(new ArrayNode("foo", "bar")).isEqualTo(new ArrayNode(new ValueNode("foo"), new ValueNode("bar")));
        assertThat(new ArrayNode("foo", "bar", "fuu")).isNotEqualTo(new ArrayNode("foo", "bar"));
        assertThat(new ArrayNode("foo", "bar")).isNotEqualTo(new ArrayNode("foo", "bar", "fuu"));
        assertThat(new ArrayNode("foo", "bar")).isNotEqualTo(new ArrayNode("foo", "fuu"));
    }
}
