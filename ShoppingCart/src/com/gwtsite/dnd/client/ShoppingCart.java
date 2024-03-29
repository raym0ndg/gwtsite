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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ShoppingCart extends Composite{

	private VerticalPanel mainPanel;
	private Label totalLabel;
	private List cartItems = new ArrayList();
	private VerticalPanel cartContents;
	
	public ShoppingCart()
	{
		mainPanel = new VerticalPanel();
		this.initWidget(mainPanel);
		this.addStyleName("shoppingCart");
		
		Label cartHeader = new Label("Your cart");
		cartHeader.addStyleName("cartHeader");
		mainPanel.add(cartHeader);
		
		cartContents = new VerticalPanel();
		mainPanel.add(cartContents);
		
		mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		totalLabel = new Label("Total: ");
		totalLabel.addStyleName("cartTotal");
		
		mainPanel.add(totalLabel);
	}

	public void add(Book book) {
		
		CartItem item = new CartItem(book);
		item.addStyleName("cartItem");
		int index = cartItems.indexOf(item);
		if (index == -1)
		{
			cartItems.add(item);
			cartContents.add(item);
		}
		else
		{
			item = (CartItem) cartItems.get(index);
		}
		item.incrementQuantity();
		updateTotalLabel();
		
	}

	private void updateTotalLabel() {
		BigDecimal totalPrice = new BigDecimal("0.00");
		for (int i = 0; i < cartItems.size(); i++)
		{
			CartItem item = (CartItem)cartItems.get(i);
			totalPrice = totalPrice.add(item.getPrice());
		}
		totalLabel.setText("Total: $" + totalPrice);
	}

	private class CartItem extends Composite {

		private Book book;
		private int quantity;
		private VerticalPanel mainPanel = new VerticalPanel();
		private Label qtyLabel = new Label();
		
		public CartItem(Book book) {
			this.book = book;
			initWidget(mainPanel);
			mainPanel.add(new Label(book.getBookTitle()));
			mainPanel.add(qtyLabel);
			mainPanel.add(new Label("price: $" + book.getPrice().toString()));
		}


		public void incrementQuantity() {
			quantity++;
			qtyLabel.setText("qty: " + new Integer(quantity).toString());
		}
		
		public int getQuantity() {
			return quantity;
		}
		

		public BigDecimal getPrice() {
			return book.getPrice().multiply(new BigDecimal(""+quantity));
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((book == null) ? 0 : book.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			CartItem other = (CartItem) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (book == null) {
				if (other.book != null)
					return false;
			} else if (!book.equals(other.book))
				return false;
			return true;
		}

		private ShoppingCart getOuterType() {
			return ShoppingCart.this;
		}
	}
}
