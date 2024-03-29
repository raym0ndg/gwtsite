package com.gwtsite.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Code taken from FileUpload widget.
 * {@link http://google-web-toolkit.googlecode.com/svn/jaavadoc/1.4/com/google/gwt/user/client/ui/FileUpload.html}
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class FileUploader implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Create a FormPanel and point it at a service.
		final FormPanel form = new FormPanel();
		form.setAction(GWT.getModuleBaseURL() + "upload.php");

		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		VerticalPanel panel = new VerticalPanel();
		form.setWidget(panel);

		// Create a TextBox, giving it a name so that it will be submitted.
		final TextBox tb = new TextBox();
		tb.setName("textBoxFormElement");
		panel.add(tb);

		// Create a ListBox, giving it a name and some values to be associated
		// with its options.
		ListBox lb = new ListBox();
		lb.setName("listBoxFormElement");
		lb.addItem("foo", "fooValue");
		lb.addItem("bar", "barValue");
		lb.addItem("baz", "bazValue");
		panel.add(lb);

		// Create a FileUpload widget.
		FileUpload upload = new FileUpload();
		upload.setName("uploadFormElement");
		panel.add(upload);

		// Add a 'submit' button.
		panel.add(new Button("Submit", new ClickListener() {
			public void onClick(Widget sender) {
				form.submit();
			}
		}));

		// Add an event handler to the form.
		form.addFormHandler(new FormHandler() {
			public void onSubmit(FormSubmitEvent event) {
				// This event is fired just before the form is submitted. We can
				// take this opportunity to perform validation.
				if (tb.getText().length() == 0) {
					Window.alert("The text box must not be empty");
					event.setCancelled(true);
				}
			}

			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				// When the form submission is successfully completed, this
				// event is fired. Assuming the service returned a response of type
				// text/html, we can get the result text here (see the FormPanel
				// documentation for further explanation).
				Window.alert(event.getResults());
			}
		});

		RootPanel.get().add(form);
	}
}
