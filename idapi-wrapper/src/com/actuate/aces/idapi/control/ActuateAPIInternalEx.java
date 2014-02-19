/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.control;

import com.actuate.schemas.internal.ActuateAPI;
import org.apache.axis.client.Call;

public interface ActuateAPIInternalEx extends ActuateAPI {

	public Call getCall();

}
