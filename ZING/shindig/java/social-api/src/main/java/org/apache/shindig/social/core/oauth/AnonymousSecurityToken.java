/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.shindig.social.core.oauth;

import org.apache.shindig.common.SecurityToken;

public class AnonymousSecurityToken implements SecurityToken {

  public AnonymousSecurityToken() { }

  // TODO: We should probably pull this method up into the base SecurityToken interface
  public boolean isAnonymous() {
    return true;
  }

  public String toSerialForm() {
    throw new UnsupportedOperationException();
  }

  public String getOwnerId() {
    throw new UnsupportedOperationException();
  }

  public String getViewerId() {
    throw new UnsupportedOperationException();
  }

  public String getAppId() {
    throw new UnsupportedOperationException();
  }

  public String getDomain() {
    throw new UnsupportedOperationException();
  }

  public String getAppUrl() {
    throw new UnsupportedOperationException();
  }

  public long getModuleId() {
    throw new UnsupportedOperationException();
  }

  public String getUpdatedToken() {
    throw new UnsupportedOperationException();
  }

  public String getTrustedJson() {
    throw new UnsupportedOperationException();
  }
}