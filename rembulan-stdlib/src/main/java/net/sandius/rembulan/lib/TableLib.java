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
 *
 * --
 * Portions of this file are licensed under the Lua license. For Lua
 * licensing details, please visit
 *
 *     http://www.lua.org/license.html
 *
 * Copyright (C) 1994-2016 Lua.org, PUC-Rio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.sandius.rembulan.lib;

import net.sandius.rembulan.Table;
import net.sandius.rembulan.TableFactory;
import net.sandius.rembulan.runtime.LuaFunction;

/**
 * <p>This library provides generic functions for table manipulation. It provides all its functions
 * inside the table {@code table}.</p>
 *
 * Remember that, whenever an operation needs the length of a table, the table must be a proper
 * sequence or have a {@code __len} metamethod (see §3.4.7 of the Lua Reference Manual).
 * All functions ignore non-numeric keys in the tables given as arguments.
 */
public abstract class TableLib extends Lib {

	@Override
	public String name() {
		return "table";
	}

	@Override
	public Table toTable(TableFactory tableFactory) {
		Table t = tableFactory.newTable();
		t.rawset("concat", _concat());
		t.rawset("insert", _insert());
		t.rawset("move", _move());
		t.rawset("pack", _pack());
		t.rawset("remove", _remove());
		t.rawset("sort", _sort());
		t.rawset("unpack", _unpack());
		return t;
	}

	/**
	 * {@code table.concat (list [, sep [, i [, j]]])}
	 *
	 * <p>Given a list where all elements are strings or numbers, returns the string
	 * {@code list[i]..sep..list[i+1] ··· sep..list[j]}. The default value for {@code sep}
	 * is the empty string, the default for {@code i} is 1, and the default for {@code j}
	 * is {@code #list}. If {@code i} is greater than {@code j}, returns the empty string.</p>
	 *
	 * @return the {@code table.concat} function
	 */
	public abstract LuaFunction _concat();

	/**
	 * {@code table.insert (list, [pos,] value)}
	 *
	 * <p>Inserts element value at position {@code pos} in {@code list}, shifting up the elements
	 * {@code list[pos], list[pos+1], ···, list[#list]}. The default value for {@code pos}
	 * is {@code #list+1}, so that a call {@code table.insert(t,x)} inserts {@code x}
	 * at the end of list {@code t}.</p>
	 *
	 * @return the {@code table.insert} function
	 */
	public abstract LuaFunction _insert();

	/**
	 * {@code table.move (a1, f, e, t [,a2])}
	 *
	 * <p>Moves elements from table {@code a1} to table {@code a2}. This function performs
	 * the equivalent to the following multiple assignment:
	 * {@code a2[t],··· = a1[f],···,a1[e]}. The default for {@code a2} is {@code a1}.
	 * The destination range can overlap with the source range. The number of elements
	 * to be moved must fit in a Lua integer.</p>
	 *
	 * @return the {@code table.move} function
	 */
	public abstract LuaFunction _move();

	/**
	 * {@code table.pack (···)}
	 *
	 * <p>Returns a new table with all parameters stored into keys 1, 2, etc. and with
	 * a field {@code "n"} with the total number of parameters. Note that the resulting table
	 * may not be a sequence.</p>
	 *
	 * @return the {@code table.pack} function
	 */
	public abstract LuaFunction _pack();

	/**
	 * {@code table.remove (list [, pos])}
	 *
	 * <p>Removes from {@code list} the element at position {@code pos}, returning the value
	 * of the removed element. When {@code pos} is an integer between 1 and {@code #list},
	 * it shifts down the elements{@code  list[pos+1], list[pos+2], ···, list[#list]}
	 * and erases element {@code list[#list]}; The index pos can also be 0 when {@code #list}
	 * is 0, or {@code #list + 1}; in those cases, the function erases the element
	 * {@code list[pos]}.</p>
	 *
	 * <p>The default value for {@code pos} is {@code #list}, so that a call
	 * {@code table.remove(l)} removes the last element of list {@code l}.</p>
	 *
	 * @return the {@code table.remove} function
	 */
	public abstract LuaFunction _remove();

	/**
	 * {@code table.sort (list [, comp])}
	 *
	 * <p>Sorts list elements in a given order, in-place, from {@code list[1]}
	 * to {@code list[#list]}. If {@code comp} is given, then it must be a function that receives
	 * two list elements and returns <b>true</b> when the first element must come before
	 * the second in the final order (so that, after the sort, {@code i < j} implies
	 * {@code not comp(list[j],list[i])}). If {@code comp} is not given, then the standard
	 * Lua operator {@code <} is used instead.</p>
	 *
	 * <p>Note that the {@code comp} function must define a strict partial order over the elements
	 * in the list; that is, it must be asymmetric and transitive. Otherwise, no valid sort may
	 * be possible.</p>
	 *
	 * <p>The sort algorithm is not stable; that is, elements not comparable by the given order
	 * (e.g., equal elements) may have their relative positions changed by the sort.</p>
	 *
	 * @return the {@code table.sort} function
	 */
	public abstract LuaFunction _sort();

	/**
	 * {@code table.unpack (list [, i [, j]])}
	 *
	 * <p>Returns the elements from the given list. This function is equivalent to</p>
	 *
	 * <blockquote>
	 *  {@code return list[i], list[i+1], ···, list[j] }
	 * </blockquote>
	 *
	 * <p>By default, {@code i} is 1 and {@code j} is {@code #list}.</p>
	 *
	 * @return the {@code table.unpack} function
	 */
	public abstract LuaFunction _unpack();

}
