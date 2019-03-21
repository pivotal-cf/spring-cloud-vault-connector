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
package io.pivotal.spring.cloud.vault.config;

import io.pivotal.spring.cloud.StubCloudConnectorTest;
import org.junit.Test;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.assertNotNull;

public abstract class AbstractCloudConfigServiceTest<SC> extends StubCloudConnectorTest {
	protected abstract ServiceInfo createService(String id);

	protected abstract Class<SC> getConnectorType();

	protected abstract ApplicationContext getTestApplicationContextWithServiceId(
			ServiceInfo... serviceInfos);

	protected abstract ApplicationContext getTestApplicationContextWithoutServiceId(
			ServiceInfo... serviceInfos);

	// Tests where <cloud:data-source id="some-id" ...> form is used
	@Test
	public void cloudConnectorWithServiceNameSpecified_UniqueServiceExists() {
		ApplicationContext testContext = getTestApplicationContextWithServiceId(createService("my-service"));

		assertNotNull("Getting service by type", testContext.getBean(getConnectorType()));
		assertNotNull("Getting service by id",
				testContext.getBean("my-service", getConnectorType()));
	}

	@Test(expected = BeanCreationException.class)
	public void cloudConnectorWithServiceNameSpecified_NoServiceExists_byType() {
		ApplicationContext testContext = getTestApplicationContextWithServiceId();

		testContext.getBean(getConnectorType());
	}

	@Test(expected = BeanCreationException.class)
	public void cloudConnectorWithServiceNameSpecified_NoServiceExists_byId() {
		ApplicationContext testContext = getTestApplicationContextWithServiceId();

		testContext.getBean("my-service", getConnectorType());
	}

	@Test
	public void cloudConnectorWithServiceNameSpecified_TwoServiceExist() {
		ApplicationContext testContext = getTestApplicationContextWithServiceId(
				createService("my-service"), createService("my-service-2"));

		assertNotNull("Getting service by id",
				testContext.getBean("my-service", getConnectorType()));
		assertNotNull("Getting service by type", testContext.getBean(getConnectorType()));
	}

	// Tests where <cloud:data-source ...> form is used.
	// Since id is NOT specified, getting a service will work only if there is a unique
	// relational service bound
	// In unique services case, the id of the bean must match the service id.
	@Test
	public void cloudConnectorWithoutServiceNameSpecified_UniqueServiceExists() {
		ApplicationContext testContext = getTestApplicationContextWithoutServiceId(createService("my-service"));

		assertNotNull("Getting service by id",
				testContext.getBean("my-service", getConnectorType()));
		assertNotNull("Getting service by type", testContext.getBean(getConnectorType()));
	}

	@Test(expected = BeanCreationException.class)
	public void cloudConnectorWithoutServiceNameSpecified_NoServiceExists_byType() {
		ApplicationContext testContext = getTestApplicationContextWithoutServiceId();

		testContext.getBean(getConnectorType());
	}

	@Test(expected = BeanCreationException.class)
	public void cloudConnectorWithoutServiceNameSpecified_NoServiceExists_byId() {
		ApplicationContext testContext = getTestApplicationContextWithoutServiceId();

		testContext.getBean("my-service", getConnectorType());
	}

	@Test(expected = BeanCreationException.class)
	public void cloudConnectorWithoutServiceNameSpecified_TwoServiceExist_byType() {
		ApplicationContext testContext = getTestApplicationContextWithoutServiceId(
				createService("my-service"), createService("my-service-2"));

		testContext.getBean("my-service", getConnectorType());
	}

	@Test(expected = BeanCreationException.class)
	public void cloudConnectorWithoutServiceNameSpecified_TwoServiceExist_byId() {
		ApplicationContext testContext = getTestApplicationContextWithoutServiceId(
				createService("my-service"), createService("my-service-2"));

		testContext.getBean(getConnectorType());
	}
}
