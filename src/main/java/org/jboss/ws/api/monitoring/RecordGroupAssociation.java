/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ws.api.monitoring;

import static org.jboss.ws.api.Log.LOGGER;

import java.util.Stack;

/**
 * Associates the record group ID with the current thread. This is implemented internally
 * through a static ThreadLocal member. 
 * 
 * @author <a href="mailto:alessio.soldano@jboss.com">Alessio Soldano</a>
 * @since 8-Dec-2007
 */
public class RecordGroupAssociation
{
   private static ThreadLocal<Stack<String>> groupIDAssoc = new ThreadLocal<Stack<String>>();
   
   /**
    * Associates the specified groupID to the current thread
    * 
    * @param groupID the groupID to associate to the current thread
    */
   public static void pushGroupID(String groupID)
   {
      if (LOGGER.isTraceEnabled()) LOGGER.pushGroupID(groupID, Thread.currentThread().getName());
      Stack<String> stack = groupIDAssoc.get();
      if (stack == null)
      {
         stack = new Stack<String>();
         groupIDAssoc.set(stack);
      }
      stack.push(groupID);
   }

   /**
    * Returns the groupID currently associated to the current thread
    * 
    * @return the current thread groupID
    */
   public static String peekGroupID()
   {
      String groupID = null;
      Stack<String> stack = groupIDAssoc.get();
      if (stack != null && stack.isEmpty() == false)
      {
         groupID = stack.peek();
      }
      if (LOGGER.isTraceEnabled()) LOGGER.peekGroupID(groupID, Thread.currentThread().getName());
      return groupID;
   }

   /**
    * Returns the groupID for the current thread and removes the association.
    * 
    * @return thre current thread groupID
    */
   public static String popGroupID()
   {
      String groupID = null;
      Stack<String> stack = groupIDAssoc.get();
      if (stack != null && stack.isEmpty() == false)
      {
         groupID = stack.pop();
      }
      if (LOGGER.isTraceEnabled()) LOGGER.popGroupID(groupID, Thread.currentThread().getName());
      return groupID;
   }
   
}
