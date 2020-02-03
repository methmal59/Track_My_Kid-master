package com.project.dreamsquad.trackmykid.others;

import android.location.Address;
import android.util.Log;

/**
 * Created by this pc on 15-05-17.
 */

public class GeoSearchResult {

    private Address address;
    private String[] array;
    private String ad="";

    public GeoSearchResult(Address address)
    {
        this.address = address;
    }

    public String getAddress(){

        String display_address = "";

        display_address += address.getAddressLine(0) + "\n";

        for(int i = 1; i < address.getMaxAddressLineIndex(); i++)
        {
            display_address += address.getAddressLine(i) + ",";
        }

        if(display_address.charAt(display_address.length()-1)==',')

            display_address = display_address.substring(0, display_address.length() - 2);
        else

            display_address = display_address.substring(0, display_address.length() - 1);

        Log.e("First",display_address+"");

//        array=new String[display_address.length()];
//        array=display_address.split(",");
//        display_address="";
//
//        for(int i=0;i<array.length;i++)
//        {
//            if(ad.length()<40)
//
//            {
//                ad += array[i] + ",";
//                Log.e("Featurhjgjgffhghg", ad);
//            }
//            else
//            {
//                ad=ad.trim();
//                display_address=ad+"\n"+display_address;
//                ad="";
//                i--;
//                Log.e("Featuoooorhjgjhg",display_address);
//            }
//
//        }
//        Log.e("Featurhjgjhg",display_address);
//
//        if(display_address.length()>2)
//        display_address=display_address.substring(0,display_address.length()-2);

        return display_address;

    }

    public String toString(){

        String display_address = "";

        if(address.getFeatureName() != null)
        {
            display_address += address + ", ";
            Log.e("Second",address.getFeatureName()+"");
        }

        for(int i = 0; i < address.getMaxAddressLineIndex(); i++)
        {
            display_address += address.getAddressLine(i);
            Log.e("Third",address.getAddressLine(i)+"");
        }

//        array=new String[display_address.length()];
//        array=display_address.split(",");
//        display_address="";
//
//        for(int i=0;i<array.length;i++)
//        {
//            if(ad.length()<40)
//
//                ad+=array[i]+",";
//            else
//            {
//                display_address+=ad+"\n";
//                ad="";
//                i--;
//            }
//
//        }
//
//        Log.e("Featur",display_address);
        return display_address;
    }
}
