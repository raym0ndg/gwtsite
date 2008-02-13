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
