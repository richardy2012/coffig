/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.coffig.spi;

import org.seedstack.coffig.TreeNode;

import java.lang.reflect.Type;

public interface ConfigurationMapper {

    boolean canHandle(Type type);

    Object map(TreeNode treeNode, Type type);

    TreeNode unmap(Object object, Type type);

}
