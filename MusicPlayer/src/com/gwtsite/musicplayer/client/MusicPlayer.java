package com.gwtsite.musicplayer.client;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.HttpProxy;
import com.gwtext.client.data.JsonReader;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MusicPlayer implements EntryPoint {
	private GridPanel grid;
	private Button playSongBtn;
	private Button pauseSongBtn;
	private Sound currentSong;
	private SoundController soundController = new SoundController();
	private HTML lyricsArea;

	public void onModuleLoad() {
	 
		buildSongTable();  
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.add(grid);
		
		VerticalPanel rightPanel = new VerticalPanel();
		pauseSongBtn = new Button();
		pauseSongBtn.setText("Pause song");
		rightPanel.add(pauseSongBtn);
		
		lyricsArea = new HTML();
		rightPanel.add(lyricsArea);
		
		mainPanel.add(rightPanel);

		RootPanel.get().add(mainPanel); 
		addListeners();
		
	}

	private void buildSongTable() {
		RecordDef recordDef = new RecordDef(
			new FieldDef[] {
				new StringFieldDef("title"),
				new StringFieldDef("album"),
				new StringFieldDef("lyrics")
			}
		);
		
		Object[][] data = getSongData();  
		HttpProxy dataProxy = new HttpProxy("songs.json");
//		MemoryProxy proxy = new MemoryProxy(data);  
//		ArrayReader reader = new ArrayReader(recordDef); 	
		
		JsonReader reader = new JsonReader(recordDef);  
		reader.setRoot("data");  
		
//		Store store = new Store(proxy, reader);  
		Store store = new Store(dataProxy, reader); 
		store.load();  
		
		grid = new GridPanel();
		grid.setStore(store);
		
		ColumnConfig[] columns = new ColumnConfig[]{  
				new ColumnConfig("Song Title", "title", 160, true, null, "title"),  
				new ColumnConfig("Album", "album", 100)
		};  
		
		ColumnModel columnModel = new ColumnModel(columns);
		grid.setColumnModel(columnModel);
		
		grid.setFrame(true);  
		grid.setStripeRows(true);  
		grid.setAutoExpandColumn("title");  
		grid.setHeight(350);  
		grid.setWidth(350);  
		grid.setTitle("Jonathan Coulton Songs");
	}

	private void addListeners() {
		grid.addGridRowListener(new GridRowListenerAdapter() {

			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				
				Record record = grid.getStore().getAt(rowIndex);
				String title = record.getAsString("title");
				displaySongLyrics(title);
			}

			private void displaySongLyrics(String title) {
				try
				{
					RequestBuilder request = new RequestBuilder(RequestBuilder.GET, title+".txt");
					request.sendRequest(null, new RequestCallback() {
						public void onError(Request request, Throwable exception) {
							System.out.println("doh, error");
							
						}
						public void onResponseReceived(Request request, Response response) {
							System.out.println("yay got: " + response.getText());
							lyricsArea.setHTML(response.getText());
						}
						
					});
				}
				catch (RequestException e)
				{
					// unable to fetch lyrics
					e.printStackTrace();
				}
			}

			public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
				if (currentSong != null)
					currentSong.stop();
				
				Record record = grid.getStore().getAt(rowIndex);
				String title = record.getAsString("title");
				String mp3Name = title + ".mp3";
				System.out.println("mp3 name: " + mp3Name);
				currentSong = soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG,
						mp3Name);

				currentSong.play();
			}
			
		});
		
		pauseSongBtn.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				currentSong.stop();
	
			}
		});
		
	}

	private Object[][] getSongData() {
		return new Object[][] { 
			new Object[] { "song1", "album1", "lyrics1" },
			new Object[] { "song2", "album2", "lyrics2" },
			new Object[] { "song3", "album1", "lyrics3" }
		};
	}
}
