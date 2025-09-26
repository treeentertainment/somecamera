
package tech.treeentertainment.camera.ptp;

/**
 * {@code PtpActions} execute one or more {@code Command}s against the actual
 * hardware.
 *
 * A {@code PtpCamera} queues {@Code PtpAction}s into the worker thread
 * for further communications. The action should do the communication via the
 * given {@code IO} interface and based on the received data and response repor
 * back to the actual {@code PtpCamera}.
 */
public interface PtpAction {

    void exec(PtpCamera.IO io);

    /**
     * Reset an already used action so it can be re-used.
     */
    void reset();
}
