package com.gwtsite.gwittir.client;

import com.totsp.gwittir.client.action.BindingAction;
import com.totsp.gwittir.client.beans.Binding;
import com.totsp.gwittir.client.ui.BoundWidget;
import com.totsp.gwittir.client.validator.CompositeValidationFeedback;
import com.totsp.gwittir.client.validator.PopupValidationFeedback;
import com.totsp.gwittir.client.validator.StyleValidationFeedback;
import com.totsp.gwittir.client.validator.ValidationException;
import com.totsp.gwittir.client.validator.ValidationFeedback;
import com.totsp.gwittir.client.validator.Validator;

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
public class AddBookAction implements BindingAction {

	private Binding binding = new Binding();
	
	public void bind(BoundWidget widget) {
		binding.bind();
	}

	public void set(BoundWidget widget) {
		BookForm bookForm = (BookForm)widget;
		Book book = (Book) bookForm.getModel();
		
		ValidationFeedback feedback =
            new PopupValidationFeedback(PopupValidationFeedback.RIGHT)
            .addMessage(NewBooksOnlyValidator.class, "We only accept books published after the year 2000.");
		
		CompositeValidationFeedback cvf = new CompositeValidationFeedback()
        	.add(feedback)
        	.add(new StyleValidationFeedback("validation-error"));
		  
		binding.getChildren().add(new Binding(book, "title", 
				bookForm.titleText, "value"));
		binding.getChildren().add(new Binding(book, "year", null, null, 
				bookForm.publishedText, "value", 
				new NewBooksOnlyValidator(), cvf));
		binding.getChildren().add(new Binding(book, "inStock", bookForm.isInStockCheckbox, "value"));
	}

	public void unbind(BoundWidget widget) {
		binding.unbind();
        binding.getChildren().clear();
		
		
	}

	public void execute(BoundWidget widget) {
		BookForm bookForm = (BookForm)widget;
		Book book = (Book) bookForm.getModel();
		if (binding.isValid())
		{
			bookForm.booksTable.add(book);
		}
		
	}
	
	  
	private class NewBooksOnlyValidator implements Validator
	{
		public Object validate(Object value) throws ValidationException {
			String year = value.toString();
			if (new Integer(year).intValue() < 1950)
				throw new ValidationException("Must be after 1950.");
			return new Integer(year);
		}
	}

}
