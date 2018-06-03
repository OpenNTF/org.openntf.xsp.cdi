/**
 * Copyright © 2018 Jesse Gallagher
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
 */
package org.openntf.xsp.cdi.impl;

import org.jboss.weld.environment.se.WeldContainer;
import org.openntf.xsp.cdi.CDILibrary;
import org.openntf.xsp.cdi.util.ContainerUtil;
import org.openntf.xsp.jakartaee.LibraryUtil;

import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.application.events.ApplicationListener2;

/**
 * Manages the lifecycle of the app's associated Weld instance.
 * 
 * @author Jesse Gallagher
 * @since 1.0.0
 */
public class WeldApplicationListener implements ApplicationListener2 {
	// *******************************************************************************
	// * ApplicationListener2 methods
	// *******************************************************************************

	@Override
	public void applicationCreated(ApplicationEx application) {
		if(LibraryUtil.usesLibrary(CDILibrary.LIBRARY_ID, application)) {
			// Ensure that the container exists
			ContainerUtil.getContainer(application);
		}
	}

	@Override
	public void applicationDestroyed(ApplicationEx application) {
		if(LibraryUtil.usesLibrary(CDILibrary.LIBRARY_ID, application)) {
			WeldContainer container = ContainerUtil.getContainer(application);
			if(container.isRunning()) {
				container.shutdown();
			}
		}
	}

	@Override
	public void applicationRefreshed(ApplicationEx application) {
		if(LibraryUtil.usesLibrary(CDILibrary.LIBRARY_ID, application)) {
			applicationDestroyed(application);
			applicationCreated(application);
		}
	}
}
