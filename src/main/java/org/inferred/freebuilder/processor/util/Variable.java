/*
 * Copyright 2017 Google Inc. All rights reserved.
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
package org.inferred.freebuilder.processor.util;

import org.inferred.freebuilder.processor.util.Scope.Level;

public class Variable extends ValueType implements Scope.Element<Variable> {

  private final String name;

  public Variable(String name) {
    this.name = name;
  }

  @Override
  public Level level() {
    return Level.METHOD;
  }

  @Override
  protected void addFields(FieldReceiver fields) {
    fields.add("name", name);
  }
}
