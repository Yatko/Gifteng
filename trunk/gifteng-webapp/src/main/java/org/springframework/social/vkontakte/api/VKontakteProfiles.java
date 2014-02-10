/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.vkontakte.api;

import java.io.Serializable;
import java.util.List;

/**
 * Collection of profiles.
 * @author vkolodrevskiy
 */
public class VKontakteProfiles extends VKResponse implements Serializable {
    public static final long serialVersionUID = -1;

    private List<VKontakteProfile> profiles;

    public List<VKontakteProfile> getProfiles() {
        return profiles;
    }
}
