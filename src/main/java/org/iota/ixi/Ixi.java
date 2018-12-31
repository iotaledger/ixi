package org.iota.ixi;

import org.iota.ict.ixi.IxiModule;
import org.iota.ict.network.event.GossipFilter;
import org.iota.ict.network.event.GossipReceiveEvent;
import org.iota.ict.network.event.GossipSubmitEvent;

/**
 * This is an example IXI module. Use it as template to implement your own module.
 * Run Main.main() to test it.
 *
 * https://github.com/iotaledger/ixi
 * */
public class Ixi extends IxiModule {

    // TODO rename your IXI
    public static final String NAME = "example.ixi";

    public static void main(String[] args) {
        new Ixi(args.length >= 1 ? args[0] : "ict");
    }

    public Ixi(String ictName) {
        super(NAME, ictName);
        System.out.println(NAME + " started");
        setGossipFilter(new GossipFilter().setWatchingAll(true));
        System.out.println("submitting message ...");
        submit("Hello World!");
    }

    @Override
    public void onTransactionReceived(GossipReceiveEvent event) {
        System.out.println("Received message:  " + event.getTransaction().decodedSignatureFragments);
    }

    @Override
    public void onTransactionSubmitted(GossipSubmitEvent event) {
        System.out.println("Submitted message: " + event.getTransaction().decodedSignatureFragments);
    }
}
