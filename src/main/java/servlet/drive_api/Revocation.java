/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.drivequickstart.drivequickstart;

import com.google.api.client.http.HttpResponseException;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.TimelineItem;
import java.io.IOException;
/**
 *
 * @author Carlos Henrique
 */
public class Revocation {
    static void printTimelineItem(Mirror service, String itemId) {
    try {
      TimelineItem item = service.timeline().get(itemId).execute();

      System.out.println("Text: " + item.getText());
      System.out.println("HTML: " + item.getHtml());
    } catch (HttpResponseException e) {
      if (e.getStatusCode() == 401) {
        // Credentials have been revoked.
        // TODO: Redirect the user to the authorization URL and/or
        //       remove the credentials from the database.
        throw new UnsupportedOperationException();
      }
    } catch (IOException e) {
      System.out.println("An error occurred: " + e);
    }
  }

}
