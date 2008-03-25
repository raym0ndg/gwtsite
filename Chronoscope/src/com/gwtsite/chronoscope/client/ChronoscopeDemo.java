package com.gwtsite.chronoscope.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.timepedia.chronoscope.client.XYDataset;
import org.timepedia.chronoscope.client.browser.ChartPanel;
import org.timepedia.chronoscope.client.browser.Chronoscope;
import org.timepedia.chronoscope.client.canvas.View;
import org.timepedia.chronoscope.client.canvas.ViewReadyCallback;
import org.timepedia.chronoscope.client.data.AppendableXYDataset;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ChronoscopeDemo implements EntryPoint {
	private Button clickMeButton;
	private ChartPanel chartPanel;
	
	private class DiggStory
	{
		private String title;
		private String diggs;
		private String comments;
		private long submitted_date;

		public DiggStory(String title, String diggs, String comments, long submitted_date)
		{
			this.title = title;
			this.diggs = diggs;
			this.comments = comments;
			this.submitted_date = submitted_date;
		}

		public String getTitle() {
			return title;
		}

		public String getDiggs() {
			return diggs;
		}

		public String getComments() {
			return comments;
		}

		public long getSubmitDate() {
			return submitted_date;
		}
		
	}
	
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get();

		clickMeButton = new Button();
		rootPanel.add(clickMeButton);
		clickMeButton.setText("Click me!");
		clickMeButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
//				System.out.println(System.currentTimeMillis());
//				RangeMutableArrayXYDataset ds = (RangeMutableArrayXYDataset) chartPanel.getChart().getPlot().getDataset(0);
//				System.out.println("lookie my dataset: " + ds.getNumSamples());
//				ds.beginUpdate();
//				ds.insertXY(ds.getX(ds.getNumSamples() - 1) + 300, 250 * Math.random());
//				ds.endUpdate();
//				
//				System.out.println("loodsfkie my dataset: " + chartPanel.getChart().getPlot());
				Date diggDate = new Date(1206335533*1000L);
				System.out.println("today: " + System.currentTimeMillis());
				System.out.println("digg date: " + diggDate.getTime());
				System.out.println("digg date: " + diggDate.toString());
				Date weekAgo = new Date(System.currentTimeMillis() - (1000*60*60*24*7));
				Date sixDaysAgo = new Date(System.currentTimeMillis() - (1000*60*60*24*6));
				
				JSONRequest.get(
//						"http://services.digg.com/story/5822395/diggs?appkey=http%3A%2F%2Fgwtsite.com&max_date=131661424&type=javascript&callback=", 
					  "http://services.digg.com/stories/top?appkey=http%3A%2F%2Fgwtsite.com"+
					  "&count=100&type=javascript&callback=", 
					    new JSONRequestHandler() {

								public void onRequestComplete(JavaScriptObject json) {
									JSONObject j = new JSONObject(json);
									JSONArray stories = (JSONArray) j.get("stories");
									List<DiggStory> storyList = new ArrayList<DiggStory>();
									for (int i = 0; i < stories.size(); i++)
									{
										JSONObject digg = (JSONObject) stories.get(i);
										String title = digg.get("title").toString();
										String diggs = digg.get("diggs").toString();
										String comments = digg.get("comments").toString();
										long submit_date = new Long(digg.get("submit_date").toString()).longValue();
										DiggStory story = new DiggStory(title, diggs, comments, submit_date);
										storyList.add(story);
									}
									Collections.sort(storyList, new Comparator<DiggStory>() {
										public int compare(DiggStory ds1, DiggStory ds2) {
											// TODO Auto-generated method stub
											return (int)(ds1.getSubmitDate() - ds2.getSubmitDate());
										}
										
									});
									for (DiggStory story : storyList)
									{
										System.out.println(story.getTitle() + " diggs: " + story.getDiggs());
									}
								} 
					  	
					  });
				
			}
		});
		
		double GOLDEN_RATIO = 1.618; 
		int chartWidth = 600, chartHeight = (int) ( chartWidth / GOLDEN_RATIO ); 
		Chronoscope.setFontBookRendering(true); 
		final XYDataset[] dataset = new XYDataset[1]; 
		dataset[0] = Chronoscope.createMutableXYDataset(getJson("dataset")); 
		
		VerticalPanel vp=new VerticalPanel(); 
		vp.add(new Label(dataset[0].getRangeLabel())); 
		chartPanel = Chronoscope.createTimeseriesChart(dataset, chartWidth, chartHeight); 
		chartPanel.setReadyListener(new ViewReadyCallback() {
      public void onViewReady(final View view) {
        Timer t = new Timer() {
          int count = 0;

          public void run() {
            AppendableXYDataset mxy = (AppendableXYDataset) dataset[0];
            mxy.beginUpdate();
            double x = mxy.getX(mxy.getNumSamples() - 1) + 86400 * 1000
            	+ Math.random() * 5 * 86400 * 1000;
            double y = Math.random() * (mxy.getRangeTop() - mxy.getRangeBottom());
            System.out.println("x: " + x + " y: " + y);
            mxy.insertXY(mxy.getX(mxy.getNumSamples() - 1) + 300, 250 * Math.random());
            mxy.endUpdate();
          }
        };
//        t.scheduleRepeating(1500);
      }
    });
//		vp.add(chartPanel); 
		RootPanel.get("slot1").add(vp); 
	}
	
	private static native JavaScriptObject getJson(String varName) /*-{ 
		return $wnd[varName]; 
	}-*/; 
}
