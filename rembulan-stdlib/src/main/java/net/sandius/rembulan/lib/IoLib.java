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
import net.sandius.rembulan.Userdata;
import net.sandius.rembulan.runtime.LuaFunction;

/**
 * <p>The I/O library provides two different styles for file manipulation. The first one uses
 * implicit file handles; that is, there are operations to set a default input file and
 * a default output file, and all input/output operations are over these default files.
 * The second style uses explicit file handles.</p>
 *
 * <p>When using implicit file handles, all operations are supplied by table {@code io}.
 * When using explicit file handles, the operation {@link #_open() {@code io.open}}
 * returns a file handle and then all operations are supplied as methods of the file handle.</p>
 *
 * <p>The table {@code io} also provides three predefined file handles with their usual
 * meanings from C: {@code io.stdin}, {@code io.stdout}, and {@code io.stderr}.
 * The I/O library never closes these files.</p>
 *
 * Unless otherwise stated, all I/O functions return <b>nil</b> on failure (plus an error
 * message as a second result and a system-dependent error code as a third result) and some
 * value different from <b>nil</b> on success. On non-POSIX systems, the computation
 * of the error message and error code in case of errors may be not thread safe, because they
 * rely on the global C variable {@code errno}.
 */
public abstract class IoLib extends Lib {

	@Override
	public String name() {
		return "io";
	}

	@Override
	public Table toTable(TableFactory tableFactory) {
		Table t = tableFactory.newTable();
		t.rawset("close", _close());
		t.rawset("flush", _flush());
		t.rawset("input", _input());
		t.rawset("lines", _lines());
		t.rawset("open", _open());
		t.rawset("output", _output());
		t.rawset("popen", _popen());
		t.rawset("read", _read());
		t.rawset("tmpfile", _tmpfile());
		t.rawset("type", _type());
		t.rawset("write", _write());

		t.rawset("stdin", _stdin());
		t.rawset("stdout", _stdout());
		t.rawset("stderr", _stderr());
		return t;
	}

	/**
	 * {@code io.close ([file])}
	 *
	 * <p>Equivalent to {@code file:close()}. Without a {@code file}, closes the default
	 * output file.</p>
	 *
	 * @return the {@code io.close} function
	 */
	public abstract LuaFunction _close();

	/**
	 * {@code io.flush ()}
	 *
	 * <p>Equivalent to {@code io.output():flush()}.</p>
	 *
	 * @return the {@code io.flush} function
	 */
	public abstract LuaFunction _flush();

	/**
	 * {@code io.input ([file])}
	 *
	 * <p>When called with a file name, it opens the named file (in text mode), and sets
	 * its handle as the default input file. When called with a file handle, it simply sets
	 * this file handle as the default input file. When called without parameters, it returns
	 * the current default input file.</p>
	 *
	 * <p>In case of errors this function raises the error, instead of returning an error code.</p>
	 *
	 * @return the {@code io.input} function
	 */
	public abstract LuaFunction _input();

	/**
	 * {@code io.lines ([filename, ···])}
	 *
	 * <p>Opens the given file name in read mode and returns an iterator function that works
	 * like {@code file:lines(···)} over the opened file. When the iterator function detects
	 * the end of file, it returns no values (to finish the loop) and automatically closes
	 * the file.</p>
	 *
	 * <p>The call {@code io.lines()} (with no file name) is equivalent
	 * to {@code io.input():lines("*l")}; that is, it iterates over the lines of the default
	 * input file. In this case it does not close the file when the loop ends.</p>
	 *
	 * <p>In case of errors this function raises the error, instead of returning an error code.</p>
	 *
	 * @return the {@code io.lines} function
	 */
	public abstract LuaFunction _lines();

	/**
	 * {@code io.open (filename [, mode])}
	 *
	 * <p>This function opens a file, in the mode specified in the string {@code mode}.
	 * It returns a new file handle, or, in case of errors, <b>nil</b> plus an error message.</p>
	 *
	 * <p>The mode string can be any of the following:</p>
	 * <ul>
	 * <li><b>"{@code r}"</b>: read mode (the default);</li>
	 * <li><b>"{@code w}"</b>: write mode;</li>
	 * <li><b>"{@code a}"</b>: append mode;</li>
	 * <li><b>"{@code r+}"</b>: update mode, all previous data is preserved;</li>
	 * <li><b>"{@code w+}"</b>: update mode, all previous data is erased;</li>
	 * <li><b>"{@code a+}"</b>: append update mode, previous data is preserved, writing is only allowed
	 * at the end of file.</li>
	 * </ul>
	 *
	 * <p>The mode string can also have a '{@code b}' at the end, which is needed in some systems
	 * to open the file in binary mode.</p>
	 *
	 * @return the {@code io.open} function
	 */
	public abstract LuaFunction _open();

	/**
	 * {@code io.output ([file])}
	 *
	 * <p>Similar to {@code io.input}, but operates over the default output file.</p>
	 *
	 * @return the {@code io.output} function
	 */
	public abstract LuaFunction _output();

	/**
	 * {@code io.popen (prog [, mode])}
	 *
	 * <p>This function is system dependent and is not available on all platforms.</p>
	 *
	 * <p>Starts program {@code prog} in a separated process and returns a file handle
	 * that you can use to read data from this program (if {@code mode} is "{@code r}",
	 * the default) or to write data to this program (if {@code mode} is "{@code w}").</p>
	 *
	 * @return the {@code io.popen} function
	 */
	public abstract LuaFunction _popen();

	/**
	 * {@code io.read (···)}
	 *
	 * <p>Equivalent to {@code io.input():read(···)}.</p>
	 *
	 * @return the {@code io.read} function
	 */
	public abstract LuaFunction _read();

	/**
	 * {@code io.tmpfile ()}
	 *
	 * <p>Returns a handle for a temporary file. This file is opened in update mode
	 * and it is automatically removed when the program ends.</p>
	 *
	 * @return the {@code io.tmpfile} function
	 */
	public abstract LuaFunction _tmpfile();

	/**
	 * {@code io.type (obj)}
	 *
	 * <p>Checks whether {@code obj} is a valid file handle. Returns the string {@code "file"}
	 * if {@code obj} is an open file handle, {@code "closed file"} if {@code obj} is
	 * a closed file handle, or <b>nil</b> if {@code obj} is not a file handle.</p>
	 *
	 * @return the {@code io.type} function
	 */
	public abstract LuaFunction _type();

	/**
	 * {@code io.write (···)}
	 *
	 * <p>Equivalent to {@link #_file_write() {@code io.output():write(···)}}.</p>
	 *
	 * @return the {@code io.write} function
	 */
	public abstract LuaFunction _write();

	/**
	 * {@code io.stdin}
	 *
	 * <p>The standard input file.</p>
	 *
	 * @return the standard input file
	 */
	public abstract Userdata _stdin();

	/**
	 * {@code io.stdout}
	 *
	 * <p>The standard output file.</p>
	 *
	 * @return the standard output file
	 */
	public abstract Userdata _stdout();

	/**
	 * {@code io.stderr}
	 *
	 * <p>The standard error file.</p>
	 *
	 * @return the standard error file
	 */
	public abstract Userdata _stderr();

	/**
	 * {@code file:close ()}
	 *
	 * <p>Closes {@code file}. Note that files are automatically closed when their handles
	 * are garbage collected, but that takes an unpredictable amount of time to happen.</p>
	 *
	 * <p>When closing a file handle created with {@link #_popen() {@code io.popen}},
	 * {@code file:close} returns the same values returned by {@code os.execute}.</p>
	 *
	 * @return the {@code file:close} function
	 */
	public abstract LuaFunction _file_close();

	/**
	 * {@code file:flush ()}
	 *
	 * <p>Saves any written data to {@code file}.</p>
	 *
	 * @return the {@code file:flush} function
	 */
	public abstract LuaFunction _file_flush();

	/**
	 * {@code file:lines (···)}
	 *
	 * <p>Returns an iterator function that, each time it is called, reads the file according
	 * to the given formats. When no format is given, uses "{@code l}" as a default.
	 * As an example, the construction</p>
	 *
	 * <pre>
	 * {@code
	 * for c in file:lines(1) do body end
	 * }
	 * </pre>
	 *
	 * <p>will iterate over all characters of the file, starting at the current position.
	 * Unlike {@link #_lines() {@code io.lines}}, this function does not close the file
	 * when the loop ends.</p>
	 *
	 * <p>In case of errors this function raises the error, instead of returning an error code.</p>
	 *
	 * @return the {@code file:lines} function
	 */
	public abstract LuaFunction _file_lines();

	/**
	 * {@code file:read (···)}
	 *
	 * <p>Reads the file {@code file}, according to the given formats, which specify what to read.
	 * For each format, the function returns a string or a number with the characters read,
	 * or <b>nil</b> if it cannot read data with the specified format. (In this latter case,
	 * the function does not read subsequent formats.) When called without formats, it uses
	 * a default format that reads the next line (see below).</p>
	 *
	 * <p>The available formats are</p>
	 * <ul>
	 * <li><b>"{@code n}"</b>: reads a numeral and returns it as a float or an integer,
	 * following the lexical conventions of Lua. (The numeral may have leading spaces and a sign.)
	 * This format always reads the longest input sequence that is a valid prefix for a numeral;
	 * if that prefix does not form a valid numeral (e.g., an empty string, {@code "0x"},
	 * or {@code "3.4e-"}), it is discarded and the function returns <b>nil</b>.</li>
	 * <li><b>"{@code a}"</b>: reads the whole file, starting at the current position. On end of file,
	 * it returns the empty string.</li>
	 * <li><b>"{@code l}"</b>: reads the next line skipping the end of line, returning <b>nil</b>
	 * on end of file. This is the default format.</li>
	 * <li><b>"{@code L}"</b>: reads the next line keeping the end-of-line character (if present),
	 * returning <b>nil</b> on end of file.</li>
	 * <li><b><i>number</i></b>: reads a string with up to this number of bytes,
	 * returning <b>nil</b> on end of file. If <i>number</i> is zero, it reads nothing
	 * and returns an empty string, or <b>nil</b> on end of file.</li>
	 * </ul>
	 *
	 * <p>The formats "{@code l}" and "{@code L}" should be used only for text files.</p>
	 *
	 * @return the {@code file:read} function
	 */
	public abstract LuaFunction _file_read();

	/**
	 * {@code file:seek ([whence [, offset]])}
	 *
	 * <p>Sets and gets the file position, measured from the beginning of the file,
	 * to the position given by offset plus a base specified by the string {@code whence},
	 * as follows:</p>
	 * <ul>
	 * <li><b>{@code "set"}</b>: base is position 0 (beginning of the file);</li>
	 * <li><b>{@code "cur"}</b>: base is current position;</li>
	 * <li><b>{@code "end"}</b>: base is end of file;</li>
	 * </ul>
	 *
	 * <p>In case of success, {@code seek} returns the final file position, measured in bytes from
	 * the beginning of the file. If {@code seek} fails, it returns <b>nil</b>, plus a string
	 * describing the error.</p>
	 *
	 * <p>The default value for whence is {@code "cur"}, and for offset is 0. Therefore,
	 * the call {@code file:seek()} returns the current file position, without changing it;
	 * the call {@code file:seek("set")} sets the position to the beginning of the file
	 * (and returns 0); and the call {@code file:seek("end")} sets the position to the end
	 * of the file, and returns its size.</p>
	 *
	 * @return the {@code file:seek} function
	 */
	public abstract LuaFunction _file_seek();

	/**
	 * {@code file:setvbuf (mode [, size])}
	 *
	 * <p>Sets the buffering mode for an output file. There are three available modes:</p>
	 * <ul>
	 * <li><b>{@code "no"}</b>: no buffering; the result of any output operation appears
	 * immediately.</li>
	 * <li><b>{@code "full"}</b>: full buffering; output operation is performed only when
	 * the buffer is full or when you explicitly flush the file
	 * (see {@link #_flush() {@code io.flush}}).</li>
	 * <li><b>{@code "line"}</b>: line buffering; output is buffered until a newline is output
	 * or there is any input from some special files (such as a terminal device).</li>
	 * </ul>
	 * <p>For the last two cases, {@code size} specifies the size of the buffer, in bytes.
	 * The default is an appropriate size.</p>
	 *
	 * @return the {@code file:setvbuf} function
	 */
	public abstract LuaFunction _file_setvbuf();

	/**
	 * {@code file:write (···)}
	 *
	 * <p>Writes the value of each of its arguments to {@code file}. The arguments must be strings
	 * or numbers.</p>
	 *
	 * <p>In case of success, this function returns {@code file}. Otherwise it returns <b>nil</b>
	 * plus a string describing the error.</p>
	 *
	 * @return the {@code file:write} function
	 */
	public abstract LuaFunction _file_write();

}
