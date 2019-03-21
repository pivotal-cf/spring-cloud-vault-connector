/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.spring.cloud.vault.service.common;

import java.util.Collections;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link VaultServiceInfo}.
 *
 * @author Mark Paluch
 */
public class VaultServiceInfoUnitTests {

	@Test
	public void shouldConfigureFromUri() {

		VaultServiceInfo info = new VaultServiceInfo("vault",
				"http://192.168.11.11:8200/", "foo".toCharArray(),
				Collections.singletonMap("transit",
						"cf/20fffe9d-d8d1-4825-9977-1426840a13db/transit"),
				Collections.emptyMap());

		assertThat(info.getToken()).isEqualTo("foo".toCharArray());

		assertThat(info.getHost()).isEqualTo("192.168.11.11");
		assertThat(info.getPort()).isEqualTo(8200);
		assertThat(info.getScheme()).isEqualTo("http");

		assertThat(info.getBackends()).containsEntry("transit",
				"cf/20fffe9d-d8d1-4825-9977-1426840a13db/transit");

	}
}
