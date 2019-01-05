package org.iota.ict.ixi;

import org.iota.ict.network.event.GossipEvent;
import org.iota.ict.network.event.GossipListener;

/**
 * This is an example IXI module. Use it as template to implement your own module. Run Main.main() to test it.
 * Do neither rename this class nor move it into a different package. Use this constructor only for initialization
 * because it blocks Ict. Use run() as main thread.
 * https://github.com/iotaledger/ixi
 * */
public class IxiImplementation extends IxiModule {

    public IxiImplementation(IctProxy proxy) {
        super(proxy);
        addGossipListener(new CutomGossipListener());
    }

    public void run() {
        // submit a new transaction
        submit("Hello world!");
    }
}

/**
 * A custom gossip listener which prints every message submitted or received.
 * */
class CutomGossipListener extends GossipListener {
    @Override
    public void onGossipEvent(GossipEvent event) {
        String message = (event.isOwnTransaction() ? "SUBMITTED >>> " : "RECEIVED  <<< ")
            + event.getTransaction().decodedSignatureFragments;
        System.out.println(message);
    }
}