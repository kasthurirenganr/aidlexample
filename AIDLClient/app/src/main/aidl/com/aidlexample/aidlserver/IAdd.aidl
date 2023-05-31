// IAdd.aidl
package com.aidlexample.aidlserver;
import com.aidlexample.aidlserver.Person;
// Declare any non-default types here with import statements

interface IAdd {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int sendString( String data );//2 argument method to add
}