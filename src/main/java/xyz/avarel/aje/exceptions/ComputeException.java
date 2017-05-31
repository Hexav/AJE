/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package xyz.avarel.aje.exceptions;

import xyz.avarel.aje.parser.lexer.Position;

public class ComputeException extends AJEException {
    public ComputeException(String msg) {
        super(msg);
    }

    public ComputeException(String msg, Position position) {
        super(msg + position);
    }

    public ComputeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ComputeException(String s, Position position, Throwable throwable) {
        super(s + position, throwable);
    }

    public ComputeException(Throwable throwable) {
        super(throwable);
    }
}