package com.gwtsite.musicplayer.client;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.allen_sauer.gwt.voices.client.handler.SoundCompleteEvent;
import com.allen_sauer.gwt.voices.client.handler.SoundHandler;
import com.allen_sauer.gwt.voices.client.handler.SoundLoadStateChangeEvent;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
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
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;

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
public class MusicPlayer implements EntryPoint {
	private GridPanel grid;
	private Button stopSongBtn;
	private Sound currentSong;
	private SoundController soundController = new SoundController();
	private Panel lyricsArea;
	private HTML nowPlaying;

	public void onModuleLoad() {
	 
		buildSongGrid();  
		buildLyricsArea();
		
		RootPanel.setVisible(DOM.getElementById("loading-indicator"), false);
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.addStyleName("mainPanel");
		
		HorizontalPanel topArea = new HorizontalPanel();
		topArea.addStyleName("topArea");
		topArea.add(grid);
		topArea.add(lyricsArea);
		
		nowPlaying = new HTML();
		nowPlaying.addStyleName("nowPlaying");
		nowPlaying.setText("Double-click on a song to play");
		nowPlaying.setWidth("320px");
		
		stopSongBtn = new Button();
		stopSongBtn.setText("Stop song");
		stopSongBtn.setVisible(false);
		stopSongBtn.addStyleName("stopSongBtn");
		
		mainPanel.add(topArea);
		mainPanel.add(nowPlaying);
		mainPanel.add(stopSongBtn);
		
		RootPanel.get("content").add(mainPanel);
		addListeners();
		
	}

	private void buildLyricsArea() {
		lyricsArea = new Panel();
		lyricsArea.setWidth("300px");
		lyricsArea.setHeight("325px");
		lyricsArea.setTitle("Lyrics");
		lyricsArea.setCollapsible(true);
		lyricsArea.setVisible(false);
		lyricsArea.setAutoScroll(true);
		lyricsArea.addStyleName("lyrics");
	}

	private void buildSongGrid() {
		
		RecordDef recordDef = new RecordDef(
			new FieldDef[] {
				new StringFieldDef("title"),
				new StringFieldDef("album")
			}
		);
		
		HttpProxy dataProxy = new HttpProxy("songs.json");
		JsonReader reader = new JsonReader(recordDef);  
		reader.setRoot("data");  
		
		Store store = new Store(dataProxy, reader); 
		store.load();  
		
		grid = new GridPanel();
		grid.setStore(store);
		
		ColumnConfig[] columns = new ColumnConfig[]{  
				new ColumnConfig("Song Title", "title", 160, true, null, "title"),  
				new ColumnConfig("Album", "album", 160)
		};  
		
		ColumnModel columnModel = new ColumnModel(columns);
		grid.setColumnModel(columnModel);
		
		grid.setFrame(true);  
		grid.setStripeRows(true);  
		grid.setAutoExpandColumn("title");  
		grid.setWidth(320);  
		grid.setHeight(350);
		grid.setTitle("Jonathan Coulton Songs");

	}

	private void addListeners() {
		grid.addGridRowListener(new GridRowListenerAdapter() {

			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				Record record = grid.getStore().getAt(rowIndex);
				String title = record.getAsString("title");
				lyricsArea.setVisible(true);
				lyricsArea.setTitle("Lyrics for " + title);
				displaySongLyrics(title);
			}

			private void displaySongLyrics(String title) {
				try
				{
					RequestBuilder request = new RequestBuilder(RequestBuilder.GET, title+".txt");
					request.sendRequest(null, new RequestCallback() {
						public void onError(Request request, Throwable exception) {
							System.out.println("doh, error: " + exception.getStackTrace());
						}
						public void onResponseReceived(Request request, Response response) {
							lyricsArea.setHtml(response.getText());
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
				if (currentSong != null) {
					currentSong.stop();
				}
				
				Record record = grid.getStore().getAt(rowIndex);
				final String title = record.getAsString("title");
				String mp3Name = title + ".mp3";
				
				currentSong = soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG,
						mp3Name);

				
				currentSong.addEventHandler(new SoundHandler() {
					public void onSoundComplete(SoundCompleteEvent event) {
						songPlaying(false);
					}

					public void onSoundLoadStateChange(SoundLoadStateChangeEvent event) { 
						nowPlaying.setText("Now Playing - "+ title);
					}
					
				});
				currentSong.play();
				nowPlaying.setHTML("<img class=\"loading-img\" src=\"1-0.gif\"/>Loading song - "+ title);
				songPlaying(true);
			}
			
		});
		
		stopSongBtn.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				currentSong.stop();
				songPlaying(false);
			}
		});
		
	}
	
	private void songPlaying(boolean playing)
	{
		stopSongBtn.setVisible(playing);
		nowPlaying.setVisible(playing);
	}
}
