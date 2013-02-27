package org.ovirt.engine.ui.uicommonweb.models.hosts;

import org.ovirt.engine.core.common.utils.LexoNumericComparator;
import org.ovirt.engine.ui.uicommonweb.models.SortedListModel;

public class SetupNetworksBondModel extends HostBondInterfaceModel {

    public SetupNetworksBondModel() {
        getNetwork().setIsAvailable(false);
        getCheckConnectivity().setIsAvailable(false);
        getCommitChanges().setIsAvailable(false);
        getAddress().setIsAvailable(false);
        getSubnet().setIsAvailable(false);
        getGateway().setIsAvailable(false);
        setBootProtocolAvailable(false);

        setBond(new SortedListModel(new LexoNumericComparator()));
    }

}
