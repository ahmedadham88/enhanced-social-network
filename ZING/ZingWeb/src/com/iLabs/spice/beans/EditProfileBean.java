/*
 * Copyright 2008 Impetus Infotech.
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
package com.iLabs.spice.beans;

import java.util.ArrayList;
import java.util.List;

import com.iLabs.spice.dto.UserProfile;

public class EditProfileBean extends UserProfile{
	
	protected List<String> interestsList;

		
	public List<String> getInterestsList() {
		return interestsList;
	}

	public void setInterestsList(List<String> interestsList) {
		this.interestsList = interestsList;
	}		
}
