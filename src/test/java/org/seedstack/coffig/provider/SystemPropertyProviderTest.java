/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.coffig.provider;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.coffig.node.MapNode;

import java.util.Properties;

@RunWith(JMockit.class)
public class SystemPropertyProviderTest {

    private SystemPropertyProvider underTest = new SystemPropertyProvider();

    @Test
    public void testProvide() {
        new MockUp<System>() {
            @Mock
            Properties getProperties() {
                Properties properties = new Properties();
                properties.put("test.property", "testValue");
                return properties;
            }
        };

        MapNode conf = underTest.provide();

        Assertions.assertThat(conf.get("sys.test\\.property").get().value()).isEqualTo("testValue");
    }

    @Test
    public void testProvideEmptyMap() {
        new MockUp<System>() {
            @Mock
            Properties getProperties() {
                return new Properties();
            }
        };

        MapNode conf = underTest.provide();

        Assertions.assertThat(conf).isNotNull();
    }
}
