/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.streampipes.container.html.page;

import org.apache.streampipes.container.html.model.Description;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public abstract class WelcomePageGenerator<T> {

  protected List<Description> descriptions;
  protected Collection<T> declarers;
  protected String baseUri;

  public WelcomePageGenerator(String baseUri, Collection<T> declarers) {
    this.declarers = declarers;
    this.baseUri = baseUri;
    this.descriptions = new ArrayList<>();
  }

  public abstract List<Description> buildUris();

  public List<Description> getDescriptions() {
    return descriptions;
  }
}
