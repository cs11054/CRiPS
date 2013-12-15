package src.coco.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import ppv.app.datamanager.PPDataManager;
import ppv.app.datamanager.PPProjectSet;
import ppv.view.frames.PPProjectViewerFrame;
import pres.loader.model.IPLUnit;
import pres.loader.model.PLFile;
import pres.loader.model.PLProject;
import src.coco.model.CCCompileError;
import src.coco.model.CCCompileErrorList;
import clib.common.filesystem.CDirectory;
import clib.common.time.CTime;

public class CCGraphFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int width = 680;
	private int height = 560;

	private JPanel rootPanel = new JPanel();
	private CCCompileErrorList list;

	private CDirectory libDir;
	private CDirectory base;

	ChartPanel chartpanel;
	JScrollPane scrollPanel;

	// default
	public CCGraphFrame(CCCompileErrorList list, CDirectory libDir,
			CDirectory base) {
		this.list = list;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) (d.width * 0.6);
		height = (int) (d.height * 0.6);
		this.libDir = libDir;
		this.base = base;
		initialize();
		makeGraphAndList();
	}

	private void initialize() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(width, height);
		setTitle(CCMainFrame2.APP_NAME + " " + CCMainFrame2.VERSION + " - "
				+ list.getMessage() + " �̏ڍ�");
	}

	private void makeGraphAndList() {
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.X_AXIS));
		makeGraph();
		makeSourceList();
		add(rootPanel);
		getContentPane().add(rootPanel, BorderLayout.CENTER);
		pack();
	}

	// TODO: �R���p�C���G���[�ꗗ�\�̃{�^���̃O���t�Ƃقړ���
	private void makeGraph() {
		// ���{�ꂪ�����������Ȃ��e�[�}
		// ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());

		// �O���t�f�[�^��ݒ肷��
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < list.getErrors().size(); i++) {
			dataset.addValue(list.getErrors().get(i).getCorrectTime(), "�C������",
					Integer.toString(i + 1));
		}

		// �O���t�̐���
		JFreeChart chart = ChartFactory.createLineChart(list.getMessage()
				+ "�̏C������   ���A�x: " + list.getRare(), "�C����", "�C������", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		// �t�H���g�w��i���������΍�j
		chart.getTitle().setFont(new Font("Font2DHandle", Font.PLAIN, 20));
		chart.getLegend().setItemFont(new Font("Font2DHandle", Font.PLAIN, 16));

		// �w�i�F�Z�b�g
		chart.setBackgroundPaint(new CCGraphBackgroundColor().graphColor(list
				.getRare()));

		// TODO: �v���b�g�N���b�N�@�\
		// ���̐ݒ�̊֌W����C���Plot�N���X������
		CategoryPlot plot = chart.getCategoryPlot();

		// y���̐ݒ� �E ���͐����l�̂�
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		numberAxis.setVerticalTickLabels(false);
		numberAxis.setAutoRangeStickyZero(true);
		numberAxis.setRange(0, 120);
		numberAxis.setLabelFont(new Font("Font2DHandle", Font.PLAIN, 16));

		// x���̐ݒ�
		CategoryAxis domainAxis = (CategoryAxis) plot.getDomainAxis();
		domainAxis.setLabelFont(new Font("Font2DHandle", Font.PLAIN, 16));

		// �v���b�g�̐ݒ�
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot
				.getRenderer();
		renderer.setSeriesPaint(0, ChartColor.RED);
		renderer.setSeriesStroke(0, new BasicStroke(2));
		renderer.setSeriesShapesVisible(0, true);

		// �O���t��JPanel��ɔz�u����
		chartpanel = new ChartPanel(chart);
		rootPanel.add(chartpanel, BorderLayout.WEST);
	}

	private void makeSourceList() {
		// java7����DefaultListModel�Ɋi�[����N���X���w�肵�Ȃ���΂Ȃ�Ȃ�
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (int i = 0; i < list.getErrors().size(); i++) {
			model.addElement((i + 1) + " ��ڂ̏C������ �F "
					+ list.getErrors().get(i).getCorrectTime() + "�b");
		}

		final JList<String> jlist = new JList<String>(model);
		jlist.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// ���N���b�N���ŃI�[�v������
				if (e.getButton() == MouseEvent.BUTTON1
						&& e.getClickCount() >= 2) {
					// �I�����ꂽ�v�f�����X�g�̉��Ԗڂł���̂����擾���C���̎��̃R���p�C���G���[�����擾
					int index = jlist.getSelectedIndex();
					final CCCompileError compileError = list.getErrors().get(
							index);
					// �t�@�C���p�X�ɕK�v�ȗv�f�̎��o��
					String projectname = compileError.getProjectName();
					String filename = compileError.getFilename();

					// �_�v������̋N����z��CCocoViewer�݂̂ł�baseDir��null
					if (base == null) {
						System.out.println("baseDir null");
					} else {
						PPDataManager ppDataManager = new PPDataManager(base);
						ppDataManager.setLibDir(libDir);

						CDirectory projectSetDir = ppDataManager
								.getDataDir()
								.findDirectory(compileError.getProjectSetName());
						PPProjectSet projectSet = new PPProjectSet(
								projectSetDir);

						// TODO ����R���p�C��������
						// ProjectViewerFrame�Ŏ��ۂɔ������Ă���R���p�C���G���[���o�͂������̂ŁC����R���p�C����true
						ppDataManager.loadProjectSet(projectSet, true, true);
						IPLUnit model = null;
						for (PLProject project : projectSet.getProjects()) {
							if (project.getName().equals(projectname)) {
								// �P�̂̂�
								List<PLFile> files = project.getFiles();
								for (PLFile file : files) {
									if (file.getName().equals(filename)) {
										model = file;
									}
								}

								// ���̃v���W�F�N�g�S��
								// model = project.getRootPackage();
							}
						}

						if (model == null) {
							throw new RuntimeException(
									"�R���p�C���G���[�������̃\�[�X�R�[�h�{���Ɏ��s���܂���");
						}

						final PPProjectViewerFrame frame = new PPProjectViewerFrame(
								model);
						frame.setBounds(50, 50, 1000, 700);
						frame.setVisible(true);
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								// �C���O�C�ԏC����
								frame.fitScale();
								frame.openToggleExtraView();

								long beginTime = compileError.getBeginTime();
								frame.getTimelinePane().getTimeModel2()
										.setTime(new CTime(beginTime));

								long endTime = compileError.getEndTime();
								frame.getTimelinePane().getTimeModel()
										.setTime(new CTime(endTime));
							}
						});

					}
				}
			}
		});

		scrollPanel = new JScrollPane();
		scrollPanel.getViewport().setView(jlist);

		rootPanel.add(scrollPanel, BorderLayout.EAST);
	}
}