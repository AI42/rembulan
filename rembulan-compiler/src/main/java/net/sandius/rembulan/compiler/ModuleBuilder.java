/*
 * Copyright 2016 Miroslav Janíček
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sandius.rembulan.compiler;

import net.sandius.rembulan.util.Check;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleBuilder {

	private final List<IRFunc> fns;

	public ModuleBuilder() {
		this.fns = new ArrayList<>();
	}

	public void add(IRFunc fn) {
		fns.add(Check.notNull(fn));
	}

	public Module build() {
		return new Module(Collections.unmodifiableList(fns));
	}

}
