package org.iota.ixi;

import org.iota.ict.Ict;
import org.iota.ict.utils.Constants;
import org.iota.ict.utils.Properties;

/**
 * This class is just for testing your IXI, so you don't have to run Ict manually.
 * */
public class Main {

    public static void main(String[] args) {
        System.out.println("Running IXI module for Ict version " + Constants.ICT_VERSION);
        Properties properties = new Properties();
        properties.ixiEnabled = true;
        new Ict(properties);
        new Ixi(properties.name);
    }
}
