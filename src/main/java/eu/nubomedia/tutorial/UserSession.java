/*
 * (C) Copyright 2016 NUBOMEDIA (http://www.nubomedia.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package eu.nubomedia.tutorial;

import org.kurento.client.IceCandidate;
import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

/**
 * User session.
 *
 * @author Victor Manuel Hidalgo (vmhidalgo@visual-tools.com)
 * @author Boni Garcia (boni.garcia@urjc.es)
 * @since 6.6.1
 */
public class UserSession {

  private final Logger log = LoggerFactory.getLogger(UserSession.class);

  private WebRtcEndpoint webRtcEndpoint;
  private MediaPipeline mediaPipeline;
  private KurentoClient kurentoClient;
  private String sessionId;

  public UserSession(String sessionId) {
    this.sessionId = sessionId;

    // One KurentoClient instance per session
    kurentoClient = KurentoClient.create();
    log.info("Created kurentoClient (session {})", sessionId);

    mediaPipeline = getKurentoClient().createMediaPipeline();
    log.info("Created Media Pipeline {} (session {})", getMediaPipeline().getId(), sessionId);

    webRtcEndpoint = new WebRtcEndpoint.Builder(getMediaPipeline()).build();
  }

  public WebRtcEndpoint getWebRtcEndpoint() {
    return webRtcEndpoint;
  }

  public MediaPipeline getMediaPipeline() {
    return mediaPipeline;
  }

  public KurentoClient getKurentoClient() {
    return kurentoClient;
  }

  public void addCandidate(IceCandidate candidate) {
    getWebRtcEndpoint().addIceCandidate(candidate);
  }

  public void addCandidate(JsonObject jsonCandidate) {
    IceCandidate candidate = new IceCandidate(jsonCandidate.get("candidate").getAsString(),
        jsonCandidate.get("sdpMid").getAsString(), jsonCandidate.get("sdpMLineIndex").getAsInt());
    getWebRtcEndpoint().addIceCandidate(candidate);
  }

  public void release() {
    log.info("Releasing media pipeline {}(session {})", getMediaPipeline().getId(), sessionId);
    getMediaPipeline().release();
    log.info("Destroying kurentoClient (session {})", sessionId);
    getKurentoClient().destroy();
  }

  public String getSessionId() {
    return sessionId;
  }

}
