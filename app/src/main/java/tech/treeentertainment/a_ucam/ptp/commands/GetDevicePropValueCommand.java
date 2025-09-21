/**
 * Copyright 2013 Nils Assbeck, Guersel Ayaz and Michael Zoech
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
package tech.treeentertainment.a_ucam.ptp.commands;

import java.nio.ByteBuffer;

import tech.treeentertainment.a_ucam.ptp.PtpCamera;
import tech.treeentertainment.a_ucam.ptp.PtpCamera.IO;
import tech.treeentertainment.a_ucam.ptp.PtpConstants;
import tech.treeentertainment.a_ucam.ptp.PtpConstants.Datatype;
import tech.treeentertainment.a_ucam.ptp.PtpConstants.Response;

public class GetDevicePropValueCommand extends Command {

    private final int property;
    private final int datatype;
    private int value;

    public GetDevicePropValueCommand(PtpCamera camera, int property, int datatype) {
        super(camera);
        this.property = property;
        this.datatype = datatype;
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
        if (responseCode == Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
        }
        if (responseCode == Response.Ok) {
            camera.onPropertyChanged(property, value);
        }
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, PtpConstants.Operation.GetDevicePropValue, property);
    }

    @Override
    protected void decodeData(ByteBuffer b, int length) {
        if (datatype == Datatype.int8) {
            value = b.get();
        } else if (datatype == Datatype.uint8) {
            value = b.get() & 0xFF;
        } else if (datatype == Datatype.uint16) {
            value = b.getShort() & 0xFFFF;
        } else if (datatype == Datatype.int16) {
            value = b.getShort();
        } else if (datatype == Datatype.int32 || datatype == Datatype.uint32) {
            value = b.getInt();
        }
    }
}
