/**
 * Copyright 2019-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazonaws.mobileconnectors.pinpoint.analytics;

import android.content.Context;
import android.net.wifi.WifiManager;

import androidx.test.core.app.ApplicationProvider;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.targeting.TargetingClient;
import com.amazonaws.mobileconnectors.pinpoint.targeting.endpointProfile.EndpointProfile;
import com.amazonaws.mobileconnectors.pinpoint.targeting.endpointProfile.EndpointProfileUser;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.pinpoint.model.ChannelType;
import com.amazonaws.testutils.AWSTestBase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class EndpointProfileIntegrationTest extends AWSTestBase {

    private static Context appContext;

    private PinpointManager pinpointManager;
    private PinpointConfiguration pinpointConfiguration;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private WifiManager wifiManager;

    private String appId;
    private Regions regions;

    private static String TAG = EndpointProfileIntegrationTest.class.getSimpleName();

    @Before
    public void setUp() throws Exception {
        final String identityPoolId = getPackageConfigure("pinpoint")
                .getString("identity_pool_id");

        appId = getPackageConfigure("pinpoint")
                .getString("AppId");
        regions = Regions.fromName(getPackageConfigure("pinpoint")
                .getString("Region"));

        appContext = ApplicationProvider.getApplicationContext();
        appContext.deleteDatabase("awspinpoint.db");

        wifiManager = (WifiManager) ApplicationProvider.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        assertTrue(wifiManager.setWifiEnabled(true));

        credentialsProvider = new CognitoCachingCredentialsProvider(
                appContext,
                identityPoolId,
                regions);
        pinpointConfiguration = new PinpointConfiguration(appContext,
                appId,
                regions,
                ChannelType.GCM,
                credentialsProvider);
        pinpointManager = new PinpointManager(pinpointConfiguration);
    }

    @After
    public void tearDown() {
        assertTrue(wifiManager.setWifiEnabled(true));
        pinpointManager.getAnalyticsClient().closeDB();
        appContext.deleteDatabase("awspinpoint.db");
    }

    @Test
    public void testEndpointProfileUpdate() {
        TargetingClient targetingClient = pinpointManager.getTargetingClient();
        assertNotNull(targetingClient);

        targetingClient.updateEndpointProfile();
        EndpointProfile endpointProfile = targetingClient.currentEndpoint();
        assertNotNull(endpointProfile);
        assertNull(endpointProfile.getUser().getUserId());
        assertNull(endpointProfile.getUser().getUserAttributes());

        EndpointProfileUser user = new EndpointProfileUser();
        user.setUserId(credentialsProvider.getIdentityId());
        user.addUserAttribute("user-key", Collections.singletonList("user-value"));
        endpointProfile.setUser(user);

        targetingClient.updateEndpointProfile();
        endpointProfile = targetingClient.currentEndpoint();
        assertNotNull(endpointProfile);
        assertEquals(credentialsProvider.getIdentityId(),
                endpointProfile.getUser().getUserId());
        assertNotNull(endpointProfile.getUser().getUserAttributes());
        assertEquals(Collections.singletonMap("user-key", Collections.singletonList("user-value")),
                endpointProfile.getUser().getUserAttributes());

        endpointProfile.addAttribute("key", Arrays.asList("value"));
        targetingClient.updateEndpointProfile();
        endpointProfile = targetingClient.currentEndpoint();
        assertNotNull(endpointProfile);
        assertEquals(credentialsProvider.getIdentityId(),
                endpointProfile.getUser().getUserId());
        assertEquals("value", endpointProfile.getAllAttributes().get("key").get(0));
    }
}
