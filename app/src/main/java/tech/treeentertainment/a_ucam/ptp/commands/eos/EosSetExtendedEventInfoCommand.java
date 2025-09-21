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
package tech.treeentertainment.a_ucam.ptp.commands.eos;

import java.nio.ByteBuffer;

import tech.treeentertainment.a_ucam.ptp.EosCamera;
import tech.treeentertainment.a_ucam.ptp.PtpConstants;
import tech.treeentertainment.a_ucam.ptp.PtpCamera.IO;
import tech.treeentertainment.a_ucam.ptp.PtpConstants.Operation;
import tech.treeentertainment.a_ucam.ptp.PtpConstants.Response;

public class EosSetExtendedEventInfoCommand extends EosCommand {

    public EosSetExtendedEventInfoCommand(EosCamera camera) {
        super(camera);
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
        if (responseCode != Response.Ok) {
            camera.onPtpError(String.format(
                    "Couldn't initialize session! Setting extended event info failed, error code %s",
                    PtpConstants.responseToString(responseCode)));
        }
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.EosSetEventMode, 1);
    }
}
