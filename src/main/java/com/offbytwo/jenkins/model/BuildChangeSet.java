/**
 *
 */
package com.offbytwo.jenkins.model;

import java.util.List;

/**
 * Build ChangeSet.
 *
 * @author hgontijo
 *
 */
public class BuildChangeSet {
   List items;
   String kind;

   /**
    *
    */
   public void setItems(List items) {
      this.items = items;
   }

   /**
    * @return the items
    */
   public List getItems() {
      return items;
   }

   /**
    * @return the kind
    */
   public String getKind() {
      return kind;
   }

   /**
    *
    */
   public void setKind(String kind) {
      this.kind = kind;
   }

}
