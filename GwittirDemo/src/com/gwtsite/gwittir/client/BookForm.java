package com.gwtsite.gwittir.client;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.totsp.gwittir.client.ui.AbstractBoundWidget;
import com.totsp.gwittir.client.ui.Button;
import com.totsp.gwittir.client.ui.Checkbox;
import com.totsp.gwittir.client.ui.TextBox;
import com.totsp.gwittir.client.ui.table.BoundTable;
import com.totsp.gwittir.client.ui.table.Field;
import com.totsp.gwittir.client.ui.util.BoundWidgetTypeFactory;

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
public class BookForm extends AbstractBoundWidget {

	private VerticalPanel mainPanel = new VerticalPanel();
	TextBox titleText;
	TextBox publishedText;
	Checkbox isInStockCheckbox;
	private Button addBookBtn;
	BoundTable booksTable;
	
	public BookForm()
	{
		initWidget(mainPanel);
		this.setValue(new Book());
		buildForm();
		addListeners();
	}

	private void buildForm() {
		
		HorizontalPanel titlePanel = new HorizontalPanel();
		titlePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		Label firstNameLabel = new Label("Book Title: ");
		titlePanel.add(firstNameLabel);
		titleText = new TextBox();
		titlePanel.add(titleText);
		
		HorizontalPanel publishedPanel = new HorizontalPanel();
		publishedPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		Label publishedLabel = new Label("Year published: ");
		publishedLabel.addStyleName("bookFormLabel");
		publishedPanel.add(publishedLabel);
		publishedText = new TextBox();
		publishedPanel.add(publishedText);
		
		HorizontalPanel inStockPanel = new HorizontalPanel();
		inStockPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		Label inStockLabel = new Label("In Stock? ");
		inStockPanel.add(inStockLabel);
		isInStockCheckbox = new Checkbox();
		inStockPanel.add(isInStockCheckbox);
		
		VerticalPanel inputPanel = new VerticalPanel();
		inputPanel.addStyleName("inputPanel");
		inputPanel.add(titlePanel);
		inputPanel.add(publishedPanel);
		inputPanel.add(inStockPanel);
		
		addBookBtn = new Button();
		addBookBtn.setText("Add book");
		
		booksTable = createBooksTable();
		
		
		mainPanel.add(inputPanel);
		mainPanel.add(addBookBtn);
		mainPanel.add(booksTable);
		
	}

	private BoundTable createBooksTable() {
		
		Field[] fields = new Field[] {
			new Field("title", "Book Title"),
			new Field("year", "Published"),
			new Field("inStock", "In Stock?")
		};
		BoundWidgetTypeFactory factory = new BoundWidgetTypeFactory();
		factory.add(String.class, BoundWidgetTypeFactory.LABEL_PROVIDER);
		factory.add(int.class, BoundWidgetTypeFactory.LABEL_PROVIDER);
		factory.add(boolean.class, BoundWidgetTypeFactory.CHECKBOX_PROVIDER);
		BoundTable booksTable = new BoundTable(BoundTable.HEADER_MASK + BoundTable.SCROLL_MASK + BoundTable.NO_SELECT_COL_MASK, 
				factory, fields);
		booksTable.addStyleName("booksTable");
		booksTable.setHeight("200px");
		
		booksTable.add(new Book("GWT in Practice", 2008, false));
		booksTable.add(new Book("Google Web Toolkit Applications", 2007, true));
		booksTable.add(new Book("GWT in Action", 2007, true));
		booksTable.getColumnFormatter().setWidth(0, "300px");
		booksTable.getColumnFormatter().setWidth(1, "30px");
		booksTable.getColumnFormatter().setWidth(2, "100px");
		return booksTable;
	}
	
	private void addListeners() {
		addBookBtn.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				if (getAction() != null)
				{
					getAction().execute(BookForm.this);
				}
			}
		});
		
	}

	public Object getValue() {
		return this.getModel();
	}

	public void setValue(Object value) {
		this.setModel(value);
	}
	
	
}
