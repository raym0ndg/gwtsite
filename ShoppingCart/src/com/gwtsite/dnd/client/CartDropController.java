/*
 * Copyright 2008 Chris Fong
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.gwtsite.dnd.client;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.user.client.ui.Widget;

public class CartDropController extends SimpleDropController {

	private ShoppingCart cart;
	
	public CartDropController(Widget dropTarget) {
		super(dropTarget);
		cart = (ShoppingCart)dropTarget;
	}

	public void onDrop(DragContext context) {
		super.onDrop(context);
		Book book = (Book)context.draggable;
		cart.add(book);
	}

	public void onEnter(DragContext context) {
		super.onEnter(context);
		cart.addStyleName("enterCart");
	}

	public void onLeave(DragContext context) {
		super.onLeave(context);
		cart.removeStyleName("enterCart");
	}

	public void onPreviewDrop(DragContext context) throws VetoDragException {
		
		super.onPreviewDrop(context);
	}
	
	

}
