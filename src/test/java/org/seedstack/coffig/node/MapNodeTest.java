/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.coffig.node;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.seedstack.coffig.ConfigurationException;
import org.seedstack.coffig.PropertyNotFoundException;
import org.seedstack.coffig.TreeNode;

import static org.assertj.core.api.Assertions.assertThat;

public class MapNodeTest {

    @Test
    public void testChildNode() {
        MapNode mapNode1 = new MapNode(new NamedNode("id", "foo"));

        assertThat(mapNode1.item("id")).isNotNull();
        assertThat(mapNode1.item("id")).isInstanceOf(ValueNode.class);

        try {
            mapNode1.item("name");
            Assertions.failBecauseExceptionWasNotThrown(PropertyNotFoundException.class);
        } catch (PropertyNotFoundException e) {
            assertThat(e).hasMessage("Property not found: name");
            assertThat(e.getPropertyName()).isEqualTo("name");
        }
    }

    @Test
    public void testValues() {
        assertThat(new MapNode(new NamedNode("key1", "val1"), new NamedNode("key2", "val2")).items()).containsOnly(new ValueNode("val1"), new ValueNode("val2"));
    }

    @Test(expected = ConfigurationException.class)
    public void testValue() {
        new MapNode().value();
    }

    @Test
    public void testMerge() {
        MapNode mapNode1 = new MapNode(new NamedNode("id", "foo"), new NamedNode("name", "The foo app"));
        MapNode mapNode2 = new MapNode(new NamedNode("name", "foo app"), new NamedNode("description", "The app description"));

        TreeNode mapNode3 = mapNode1.merge(mapNode2);
        assertThat(mapNode3).isNotNull();
        assertThat(mapNode3.item("id")).isSameAs(mapNode1.item("id"));
        assertThat(mapNode3.item("id").value()).isEqualTo("foo");
        assertThat(mapNode3.item("name").value()).isEqualTo("foo app");
        assertThat(mapNode3.item("description").value()).isEqualTo("The app description");
    }

    @Test
    public void testMergeIsImmutable() {
        MapNode mapNode1 = new MapNode(new NamedNode("id", "foo"));
        MapNode mapNode2 = new MapNode(new NamedNode("name", "foo app"));

        TreeNode newMapNode = mapNode1.merge(mapNode2);
        assertThat(newMapNode).isNotSameAs(mapNode1);
        assertThat(newMapNode).isNotSameAs(mapNode2);

        // test that we avoid to create unnecessary objects
        assertThat(newMapNode.item("id")).isSameAs(mapNode1.item("id"));
        assertThat(newMapNode.item("name")).isSameAs(mapNode2.item("name"));
    }

    @Test
    public void testMergeOnlyWithMapNode() {
        MapNode mapNode1 = new MapNode(new NamedNode("id", "foo"));
        ValueNode mapNode2 = new ValueNode("foo app");

        try {
            mapNode1.merge(mapNode2);
            Assertions.failBecauseExceptionWasNotThrown(ConfigurationException.class);
        } catch (ConfigurationException e) {
            assertThat(e).hasMessage(String.format("Illegal attempt to merge %s with %s", ValueNode.class.getCanonicalName(), MapNode.class.getCanonicalName()));
        }
    }

    @Test
    public void testEquals() {
        assertThat(new MapNode(new NamedNode("id", "foo"))).isEqualTo(new MapNode(new NamedNode("id", "foo")));
        assertThat(new MapNode(new NamedNode("id", "foo"))).isNotEqualTo(new MapNode(new NamedNode("id", "foo2")));
        assertThat(new MapNode(new NamedNode("id", "foo"))).isNotEqualTo(new MapNode(new NamedNode("id", "foo"), new NamedNode("name", "foo app")));
        assertThat(new MapNode(new NamedNode("id", "foo"), new NamedNode("name", "foo app"))).isNotEqualTo(new MapNode(new NamedNode("id", "foo")));
    }

    @Test
    public void testSearch() throws Exception {
        MapNode mapNode = new MapNode(new NamedNode("id", "foo"), new NamedNode("name", "The foo app"));
        assertThat(mapNode.get("id")).isNotNull();
        assertThat(mapNode.get("id").get().value()).isEqualTo("foo");
        assertThat(mapNode.get("name").get().value()).isEqualTo("The foo app");
        assertThat(mapNode.get("foo").isPresent()).isFalse();
    }
}
