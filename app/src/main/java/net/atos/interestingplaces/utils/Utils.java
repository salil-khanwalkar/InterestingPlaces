package net.atos.interestingplaces.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by a551481 on 11-08-2015.
 */
public class Utils {

    private Utils(){}

    /**
     * Launches the map application with the given coordinates
     * @param geoLocation geo location to map
     */
    public static void launchMap(final Context context, final Uri geoLocation){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
    /**
     * Starts an activity with an intent to start the phone dialer.
     * @param number Number to call
     */
    public static void launchCall(final Context context,final String number){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",number,null));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    /**
     * Starts an activity with an intent to send an email
     * Sends email to a single email id.
     * @param emailId Email ID to send the email to.
     * @param subject Subject of the email
     * @param body Body of the email
     */
    public static void launchEmail(final Context context,final String emailId,
                               final String subject,final String body){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",emailId, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(intent, "Email"));
        }
    }
}
