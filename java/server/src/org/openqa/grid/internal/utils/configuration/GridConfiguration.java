// Licensed to the Software Freedom Conservancy (SFC) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The SFC licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.openqa.grid.internal.utils.configuration;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;

public class GridConfiguration extends StandaloneConfiguration {
  /*
   * config parameters which serialize and deserialize to/from json
   */

  /**
   * Clean up cycle for remote proxies. Default determined by configuration type.
   */
  @Expose
  // initially defaults to null from type
  public Integer cleanUpCycle;

  /**
   * Custom key/value pairs for the hub registry. Default empty.
   */
  @Expose
  public Map<String, String> custom = new HashMap<>();

  /**
   * Hostname or IP to use. Defaults to {@code null}. Automatically determined when {@code null}.
   */
  @Expose
  // initially defaults to null from type
  public String host;

  /**
   * Max "browser" sessions a node can handle. Default determined by configuration type.
   */
  @Expose
  // initially defaults to null from type
  public Integer maxSession;

  /**
   * Extra servlets to initialize/use on the hub or node. Default empty.
   */
  @Expose
  public List<String> servlets = new ArrayList<>();

  /**
   * Default servlets to exclude on the hub or node. Default empty.
   */
  @Expose
  public List<String> withoutServlets = new ArrayList<>();

  /**
   * Creates a new configuration with default values
   */
  GridConfiguration() {
    // defeats instantiation outside of this package
  }

  /**
   * replaces this instance of configuration value with the 'other' value if it's set.
   * @param other
   */
  public void merge(GridConfiguration other) {
    if (other == null) {
      return;
    }
    super.merge(other);

    // don't merge 'host'
    if (isMergeAble(other.cleanUpCycle, cleanUpCycle)) {
      cleanUpCycle = other.cleanUpCycle;
    }
    if (isMergeAble(other.custom, custom)) {
      if (custom == null) {
        custom = new HashMap<>();
      }
      custom.putAll(other.custom);
    }
    if (isMergeAble(other.maxSession, maxSession) &&
        other.maxSession.intValue() > 0) {
      maxSession = other.maxSession;
    }
    if (isMergeAble(other.servlets, servlets)) {
      servlets = other.servlets;
    }
    if (isMergeAble(other.withoutServlets, withoutServlets)) {
      withoutServlets = other.withoutServlets;
    }
  }

  /**
   * @param servlet the {@link Servlet} to look for
   * @return whether this configuration requests a 'default' servlet to be omitted
   */
  public boolean isWithOutServlet(Class <? extends Servlet> servlet) {
    return withoutServlets != null &&
           servlet != null &&
           withoutServlets.contains(servlet.getCanonicalName());
  }

  @Override
  public String toString(String format) {
    StringBuilder sb = new StringBuilder();
    sb.append(super.toString(format));
    sb.append(toString(format, "cleanUpCycle", cleanUpCycle));
    sb.append(toString(format, "custom", custom));
    sb.append(toString(format, "host", host));
    sb.append(toString(format, "maxSession", maxSession));
    sb.append(toString(format, "servlets", servlets));
    sb.append(toString(format, "withoutServlets", withoutServlets));
    return sb.toString();
  }
}
