package src.coco.view;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;

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
	 * minigraph��\������ chartPanel��ActionListener�ɑΉ����Ă��Ȃ��̂ŁAMouseListener�Ŏ���
	 */

	private static final long serialVersionUID = 1L;

	private CCCompileErrorList list;

	private CDirectory libDir;
	private CDirectory base;

	private int buttonWidth = 100;
	private int buttonHeight = 100;

	public CCErrorElementButton2(int buttonWidth, int buttonHeight,
			CCCompileErrorList list, CDirectory libDir, CDirectory base) {
		this.list = list;
		this.libDir = libDir;
		this.base = base;
		this.buttonWidth = buttonWidth;
		this.buttonHeight = buttonHeight;

		super.setLayout(null);
		super.setSize(new Dimension(buttonWidth, buttonHeight));
		makeGraph();
	}

	private void makeGraph() {
		// ���{�ꂪ�����������Ȃ��e�[�}(�t�H���g�w��Ŕ������̂Ŏg��Ȃ�)
		// ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());

		// �O���t�f�[�^��ݒ肷��
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < list.getErrors().size(); i++) {
			dataset.addValue(list.getErrors().get(i).getCorrectTime(), "�C������",
					Integer.toString(i + 1));
		}

		// TODO: �O���t�̐��� message����������ꍇ�A�Z�����鏈�����ǂ����邩
		// 10�������炢�ŋ�؂��āAToolTip�ŕ⊮�����i���l����
		String message = list.getMessage();
		if (list.getMessage().length() > 10) {
			message = message.substring(0, 9) + "...";
		}

		JFreeChart chart = ChartFactory.createLineChart(message, "�C����",
				"�C������", dataset, PlotOrientation.VERTICAL, false, false, false);
		// �t�H���g�w�肵�Ȃ��ƕ�����������
		chart.getTitle().setFont(new Font("Font2DHandle", Font.PLAIN, 20));

		// �w�i�F�̃Z�b�g
		chart.setBackgroundPaint(new CCGraphBackgroundColor().graphColor(list
				.getRare()));

		// Plot�N���X������
		CategoryPlot plot = chart.getCategoryPlot();

		// �c���̐ݒ� �E ���͐����l�݂̂��w���悤�ɂ���
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		numberAxis.setVerticalTickLabels(false);
		numberAxis.setAutoRangeStickyZero(true);
		numberAxis.setRangeWithMargins(0, 60);
		numberAxis.setLabelFont(new Font("Font2DHandle", Font.PLAIN, 16));

		// x���̐ݒ�
		CategoryAxis domainAxis = (CategoryAxis) plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("Font2DHandle", Font.PLAIN, 16));

		// �v���b�g�̐ݒ�
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot
				.getRenderer();
		renderer.setSeriesPaint(0, ChartColor.RED);
		renderer.setSeriesStroke(0, new BasicStroke(1));
		renderer.setSeriesShapesVisible(0, true);

		ChartPanel chartpanel = new ChartPanel(chart);
		chartpanel.addChartMouseListener(this);
		chartpanel.setSize(buttonWidth, buttonHeight);

		// TODO: ToolTip����肭�\���ł��Ȃ�
		chartpanel.setToolTipText(list.getErrors().size() + " : "
				+ list.getMessage());
		chartpanel.setDisplayToolTips(true);
		// �f�o�b�O�p
		// System.out.println(chartpanel.getToolTipText());

		add(chartpanel);
	}

	@Override
	public void chartMouseClicked(ChartMouseEvent arg0) {
		// TODO Auto-generated method stub
		CCGraphFrame frame = new CCGraphFrame(list, libDir, base);
		frame.openGraph();
		frame.setVisible(true);
	}

	@Override
	public void chartMouseMoved(ChartMouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}