package com.gwtsite.dnd.client;

import java.math.BigDecimal;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Book extends Composite implements SourcesMouseEvents{

	private String title;
	private String imgUrl;
	private BigDecimal price;
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private Image bookImage;
	
	public Book(String title, BigDecimal price, String imgUrl)
	{
		this.title = title;
		this.price = price;
		this.imgUrl = imgUrl;
		
		initWidget(mainPanel);
		
		bookImage = new Image(imgUrl);
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(bookImage);
		mainPanel.add(new Label(title));
		mainPanel.add(new Label("$"+price));
		mainPanel.addStyleName("book");
	}

	public void addMouseListener(MouseListener listener) {
		bookImage.addMouseListener(listener);
		
	}

	public void removeMouseListener(MouseListener listener) {
		bookImage.removeMouseListener(listener);
	}
	
	public String getBookTitle() {
		return this.title;
	}

	public String getImageUrl() {
		return this.imgUrl;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void makeDraggable(PickupDragController dragController) {
		dragController.makeDraggable(this, bookImage);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Book other = (Book) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	
}
