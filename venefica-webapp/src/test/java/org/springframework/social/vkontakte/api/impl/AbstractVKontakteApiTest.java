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
package org.springframework.social.vkontakte.api.impl;

import org.junit.Before;
import org.junit.Ignore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.social.test.client.MockRestServiceServer;

/**
 * Generic class for template testing.
 * @author vkolodrevskiy
 */
@Ignore
public class AbstractVKontakteApiTest {

    protected VKontakteTemplate vkontakte;
    protected VKontakteTemplate unauthorizedVKontakte;
    protected MockRestServiceServer mockServer;
    protected HttpHeaders responseHeaders;

    @Before
    public void setup() {
        vkontakte = new VKontakteTemplate("ACCESS_TOKEN", "USER_ID");
        mockServer = MockRestServiceServer.createServer(vkontakte.getRestTemplate());
        responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        unauthorizedVKontakte = new VKontakteTemplate();
        // create a mock server just to avoid hitting real vkontakte if something gets past the authorization check
        MockRestServiceServer.createServer(unauthorizedVKontakte.getRestTemplate());
    }

    protected Resource jsonResource(String filename) {
        return new ClassPathResource(filename + ".json", getClass());
    }
}
