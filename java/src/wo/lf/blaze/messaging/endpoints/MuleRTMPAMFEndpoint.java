/*
 * MuleRTMP - use red5's rtmp handler in blaze ds
 *
 * Copyright (c) 2006-2009 by respective authors (see below). All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option) any later
 * version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package wo.lf.blaze.messaging.endpoints;

import flex.messaging.FlexContext;
import flex.messaging.FlexSession;
import flex.messaging.client.FlexClient;
import flex.messaging.endpoints.AMFEndpoint;
import org.red5.server.net.rtmp.codec.MuleRTMPProtocolDecoder;
import org.red5.server.net.rtmp.codec.MuleRTMPProtocolEncoder;
import wo.lf.red5.server.service.MuleServiceInvoker;

public class MuleRTMPAMFEndpoint extends AMFEndpoint{
    public MuleRTMPAMFEndpoint(){
        super();
        MuleServiceInvoker.endpoint = this;
        if(MuleRTMPProtocolEncoder.serializationContext == null){
            MuleRTMPProtocolEncoder.serializationContext = serializationContext;
        }
        if (MuleRTMPProtocolDecoder.serializationContext == null) {
            MuleRTMPProtocolDecoder.serializationContext = serializationContext;
        }
     }

    /**
     * Utility method that endpoint implementations (or associated classes)
     * should invoke when they receive an incoming message from a client but before
     * servicing it. This method looks up or creates the proper FlexClient instance
     * based upon the FlexClient id value received from the client.
     * It also associates this FlexClient instance with the current FlexSession.
     *
     * @param id The FlexClient id value from the client.
     * @return The FlexClient or null if the provided id was null.
     */
    public FlexClient setupFlexClient(String id) {
        FlexClient flexClient = null;

        // This indicates that we're dealing with a non-legacy client that hasn't been
        // assigned a FlexClient Id yet. Reset to null to generate a fresh Id.
        if (id != null && id.equals("nil"))
            id = null;

        flexClient = getMessageBroker().getFlexClientManager().getFlexClient(id);
        // Make sure the FlexClient and FlexSession are associated.
        FlexSession session = FlexContext.getFlexSession();
        
        flexClient.registerFlexSession(session);
        // And place the FlexClient in FlexContext for this request.
        FlexContext.setThreadLocalFlexClient(flexClient);
        return flexClient;
    }
}