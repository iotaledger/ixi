package org.iota.ixi;

import org.iota.ict.ixi.IxiModule;
import org.iota.ict.model.Transaction;
import org.iota.ict.model.TransactionBuilder;
import org.iota.ict.network.event.GossipFilter;
import org.iota.ict.network.event.GossipReceiveEvent;
import org.iota.ict.network.event.GossipSubmitEvent;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Ixi extends IxiModule implements Runnable {

    private Transaction previous1, previous2;
    private BlockingQueue<Transaction> toPrint = new LinkedBlockingQueue<>();

    public static final String ADDRESS = "IXI9CHAT9999999999999999999999999999999999999999999999999999999999999999999999999";
    public static final String NAME = "chat.ixi";

    public Ixi() {
        super(NAME);
    }

    @Override
    public void onIctConnect(String name) {
        setGossipFilter(new GossipFilter().watchAddress(ADDRESS));
        System.out.println("Connected!");
        new Thread(this).start();
    }

    @Override
    public void onTransactionReceived(GossipReceiveEvent event) {
        previous2 = previous1;
        previous1 = event.getTransaction();
        toPrint.add(event.getTransaction());
    }

    @Override
    public void onTransactionSubmitted(GossipSubmitEvent event) { ; }

    @Override
    public void run() {

        Scanner in = new Scanner(System.in);

        System.out.print("ENTER YOUR NAME:\n>>> ");
        String name = in.nextLine();
        System.out.println();

        do {

            String message = in.nextLine();
            if(message.length() > 0) {
                TransactionBuilder builder = new TransactionBuilder();
                builder.address = ADDRESS;
                if(previous2 != null) {
                    builder.trunkHash = previous1.hash;
                    builder.branchHash = previous2.hash;
                }
                builder.asciiMessage("[" + name.toUpperCase() + "] " + message);
                submit(builder.build());
            }

            while (!toPrint.isEmpty()) {
                Transaction t = toPrint.poll();
                System.out.println("[" + t.hash + "] " + t.decodedSignatureFragments);
            }

        } while(true);
    }

    public static void main(String[] args) {
        new Ixi();
    }

}
