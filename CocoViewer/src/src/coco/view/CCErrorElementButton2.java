package src.coco.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import src.coco.model.CCCompileErrorList;
import clib.common.filesystem.CDirectory;

public class CCErrorElementButton2 extends JButton implements
		ChartMouseListener {

	/**
	 * minigraphを表示する chartPanelがActionListenerに対応していないので、MouseListenerで実装
	 */

	private static final long serialVersionUID = 1L;

	private CCCompileErrorList list;

	private CDirectory libDir;
	private CDirectory base;

	// private int buttonWidth = 100;
	// private int buttonHeight = 100;

	private ChartPanel chartpanel;

	public CCErrorElementButton2(int buttonWidth, int buttonHeight,
			CCCompileErrorList list, CDirectory libDir, CDirectory base) {
		this.list = list;
		this.libDir = libDir;
		this.base = base;
		// this.buttonWidth = buttonWidth;
		// this.buttonHeight = buttonHeight;

		super.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		// super.setLayout(null);

		makeGraph();
	}

	private void makeGraph() {
		// 日本語が文字化けしないテーマ(フォント指定で避けたので使わない)
		// ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());

		// グラフデータを設定する
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < list.getErrors().size(); i++) {
			dataset.addValue(list.getErrors().get(i).getCorrectTime(), "修正時間",
					Integer.toString(i + 1));
		}

		// TODO: グラフの生成 messageが長すぎる場合、短くする処理をどうするか
		// 10文字くらいで区切って、ToolTipで補完する手段を考え中
		String message = list.getMessage();
		if (list.getMessage().length() > 10) {
			message = message.substring(0, 9) + "...";
		}

		JFreeChart chart = ChartFactory.createLineChart(message, "修正回数",
				"修正時間", dataset, PlotOrientation.VERTICAL, false, false, false);
		// フォント指定しないと文字化けする
		chart.getTitle().setFont(new Font("Font2DHandle", Font.PLAIN, 20));

		// 背景色のセット
		chart.setBackgroundPaint(new CCGraphBackgroundColor().graphColor(list
				.getRare()));

		// Plotクラスを準備
		CategoryPlot plot = chart.getCategoryPlot();

		// 縦軸の設定 ・ 軸は整数値のみを指すようにする
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		numberAxis.setVerticalTickLabels(false);
		numberAxis.setAutoRangeStickyZero(true);
		numberAxis.setRangeWithMargins(0, 60);
		numberAxis.setLabelFont(new Font("Font2DHandle", Font.PLAIN, 16));

		// x軸の設定
		CategoryAxis domainAxis = (CategoryAxis) plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("Font2DHandle", Font.PLAIN, 16));

		// プロットの設定
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot
				.getRenderer();
		renderer.setSeriesPaint(0, ChartColor.RED);
		renderer.setSeriesStroke(0, new BasicStroke(1));
		renderer.setSeriesShapesVisible(0, true);

		chartpanel = new ChartPanel(chart);
		// chartpanel.setBounds(1, 1, buttonWidth, buttonHeight);
		chartpanel.addChartMouseListener(this);
		chartpanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				chartpanel.setPreferredSize(new Dimension(getWidth(),
						getHeight()));
				validate();
			}
		});

		// TODO: ToolTipが上手く表示できない
		chartpanel.setToolTipText(list.getErrors().size() + " : "
				+ list.getMessage());
		chartpanel.setDisplayToolTips(true);
		// デバッグ用
		// System.out.println(chartpanel.getToolTipText());

		add(chartpanel, BorderLayout.CENTER);
	}

	@Override
	public void chartMouseClicked(ChartMouseEvent arg0) {
		CCGraphFrame frame = new CCGraphFrame(list, libDir, base);
		frame.openGraph();
		frame.setVisible(true);
	}

	@Override
	public void chartMouseMoved(ChartMouseEvent arg0) {

	}
}