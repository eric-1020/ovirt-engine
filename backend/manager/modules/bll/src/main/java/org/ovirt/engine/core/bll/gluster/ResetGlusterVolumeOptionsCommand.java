package org.ovirt.engine.core.bll.gluster;

import org.ovirt.engine.core.bll.NonTransactiveCommandAttribute;
import org.ovirt.engine.core.common.AuditLogType;
import org.ovirt.engine.core.common.action.gluster.ResetGlusterVolumeOptionsParameters;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterVolumeOptionEntity;
import org.ovirt.engine.core.common.vdscommands.VDSCommandType;
import org.ovirt.engine.core.common.vdscommands.VDSReturnValue;
import org.ovirt.engine.core.common.vdscommands.gluster.ResetGlusterVolumeOptionsVDSParameters;
import org.ovirt.engine.core.dal.VdcBllMessages;

/**
 * BLL Command to Reset Gluster Volume Options
 */
@NonTransactiveCommandAttribute
public class ResetGlusterVolumeOptionsCommand extends GlusterVolumeCommandBase<ResetGlusterVolumeOptionsParameters> {

    private static final long serialVersionUID = 7051669924582153802L;

    public ResetGlusterVolumeOptionsCommand(ResetGlusterVolumeOptionsParameters params) {
        super(params);
    }

    @Override
    protected void setActionMessageParameters() {
        addCanDoActionMessage(VdcBllMessages.VAR__ACTION__RESET);
        addCanDoActionMessage(VdcBllMessages.VAR__TYPE__GLUSTER_VOLUME_OPTION);
    }

    @Override
    protected void executeCommand() {
        VDSReturnValue returnValue = runVdsCommand(VDSCommandType.ResetGlusterVolumeOptions,
                new ResetGlusterVolumeOptionsVDSParameters(getUpServer().getId(),
                        getGlusterVolumeName(), getParameters().getVolumeOption(), getParameters().isForceAction()));
        setSucceeded(returnValue.getSucceeded());

        if (getSucceeded()) {

            if (getParameters().getVolumeOption() != null && !getParameters().getVolumeOption().isEmpty()) {
                GlusterVolumeOptionEntity option = new GlusterVolumeOptionEntity(getParameters().getVolumeId(),
                        getParameters().getVolumeOption(),
                        null);
                removeOptionInDb(option);
            } else {
                for (GlusterVolumeOptionEntity option : getGlusterVolume().getOptions()) {
                    removeOptionInDb(option);
                }
            }
        } else {
            getReturnValue().getExecuteFailedMessages().add(returnValue.getVdsError().getMessage());
            return;
        }
    }


    /**
     * Remove the volume option in DB. If the option with given key already exists for the volume, <br>
     * it will be deleted.
     *
     * @param option
     */
    private void removeOptionInDb(GlusterVolumeOptionEntity option) {
        if (getGlusterVolume().getOptionValue(option.getKey()) != null) {
            getGlusterVolumeDao().removeVolumeOption(option);
        }
    }


    @Override
    public AuditLogType getAuditLogTypeValue() {
        if (getSucceeded()) {
            return AuditLogType.GLUSTER_VOLUME_OPTIONS_RESET;
        } else {
            return AuditLogType.GLUSTER_VOLUME_OPTIONS_RESET_FAILED;
        }
    }

}
