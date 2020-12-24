//Deepak Veerapandian 801100869, Rishi Kumar Gnanasundaram 801101490
package com.example.homework05;

import java.io.Serializable;

public class Source implements Serializable {
    String id;
    String name;

    public Source() {
    }

    @Override
    public String toString() {
        return name;
    }
}
