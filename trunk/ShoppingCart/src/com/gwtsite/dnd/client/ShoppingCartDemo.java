package com.gwtsite.dnd.client;

import java.math.BigDecimal;
import java.util.Iterator;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ShoppingCartDemo implements EntryPoint {
	public void onModuleLoad() {

		RootPanel rootPanel = RootPanel.get();

		AbsolutePanel containingPanel = new AbsolutePanel();
		containingPanel.setPixelSize(650, 600);
		containingPanel.addStyleName("bookContainer");
		PickupDragController dragController = new PickupDragController(containingPanel, false) {

			public void dragEnd() {
				super.dragEnd();
//				this.restoreSelectedWidgetsLocation();
			}
			
			protected Widget newDragProxy(DragContext context) {
			    AbsolutePanel container = new AbsolutePanel();
			    DOM.setStyleAttribute(container.getElement(), "overflow", "visible");

			    for (Iterator iterator = context.selectedWidgets.iterator(); iterator.hasNext();) {
			      Widget widget = (Widget) iterator.next();
			      Book book = (Book)widget;
			      Widget proxy = new SimplePanel();
			      proxy.setPixelSize(widget.getOffsetWidth(), widget.getOffsetHeight());
			      proxy.addStyleName("dragdrop-proxy");
			      container.add(new Image(book.getImageUrl()));
			    }

			    return container;
			}
			
		};
		dragController.setBehaviorDragProxy(true);
		ShoppingCart cart = new ShoppingCart();
		CartDropController dropController = new CartDropController(cart);
		dragController.registerDropController(dropController);
		
		Book[] books = new Book[] {
				new Book("GWT In Action", new BigDecimal("31.49"), "gwt_in_action.jpg"),
				new Book("GWT in Practice", new BigDecimal("31.49"), "gwt_in_practice.jpg"),
				new Book("Google Web Toolkit Solutions", new BigDecimal("33.10"), "gwt_solutions.jpg"),
				new Book("Accelerated GWT", new BigDecimal("24.41"), "accelerated_gwt.jpg"),
				new Book("GWT Java Ajax Programming", new BigDecimal("44.99"), "gwt_java_ajax.jpg"),
				new Book("Google Web Toolkit Applications", new BigDecimal("41.10"), "gwtapps.jpg"),
				new Book("Google Web Toolkit: Taking the Pain Out of Ajax", new BigDecimal("8.50"), "taking_the_pain.jpg")
				
		};
		
		FlowPanel flowPanel = new FlowPanel();
		flowPanel.addStyleName("flowPanel");
		for (int i = 0; i < books.length; i++)
		{
			Book book = books[i];
			book.makeDraggable(dragController);
			flowPanel.add(book);
		}
		
		
		containingPanel.add(cart);
		containingPanel.add(flowPanel);
		rootPanel.add(new HTML("<h3>Drag some books to your cart</h3>"));
		rootPanel.add(containingPanel);
	}
}