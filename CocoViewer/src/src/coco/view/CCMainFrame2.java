package src.coco.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import src.coco.model.CCCompileErrorList;
import src.coco.model.CCCompileErrorManager;

public class CCMainFrame2 extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String APP_NAME = "CoCo Viewer";
	public static final String VERSION = "0.0.7";

	// Button Size
	private int buttonWidth = 100;
	private int buttonHeight = 100;

	// Dialog size
	private int width = 1120;
	private int height = 740;

	// Compile Error Date
	private CCCompileErrorManager manager;

	// For GUI
	private JPanel rootPanel = new JPanel();
	private JPanel headerPanel = new JPanel();
	private JPanel buttonEreaPanel = new JPanel();

	public CCMainFrame2(CCCompileErrorManager manager) {
		this.manager = manager;
		// this.height = GraphicsEnvironment.getLocalGraphicsEnvironment()
		// .getMaximumWindowBounds().height - 25;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.width = d.width * 3 / 4;
		this.height = d.height * 3 / 4;
		this.buttonWidth = this.width / 8;
		this.buttonHeight = this.height / 8;
		initialize();
	}

	private void initialize() {
		// title�Ȃǂ̐ݒ�
		frameSetting();

		// rootPanel �̃��C�A�E�g�����Z�b�g����
		// rootPanel.setLayout(null);
		panelSetting();

		// �S�̂̃R���p�C�����\��
		setCompileErrorNumber();

		// �{�^����z�u����
		setMiniGraphButton();

		// ���C�A�E�g�����z�u�ŃR���e���c��ǉ�
		rootPanel.add(headerPanel, BorderLayout.NORTH);
		// rootPanel.add(Box.createHorizontalStrut(height / 32),
		// BorderLayout.NORTH);
		rootPanel.add(buttonEreaPanel, BorderLayout.SOUTH);
		add(rootPanel);
		// getContentPane().add(rootPanel, BorderLayout.CENTER);
		// TODO: Window�T�C�Y�ύX�ɑΉ��ł���悤�ɂ��邱��
		// this.addWindowListener(new WindowAdapter() {
		// public void windowStateChanged(WindowEvent e) {
		//
		// }
		// });
	}

	private void panelSetting() {
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		rootPanel.setSize(new Dimension(width, height));
		headerPanel.setLayout(new BorderLayout());
		headerPanel.setMaximumSize(new Dimension(width, height / 24));
		// buttonEreaPanel.setLayout(new BoxLayout(buttonEreaPanel,
		// BoxLayout.X_AXIS));
		// buttonEreaPanel.setMaximumSize(new Dimension(width, height * 15 /
		// 16));
	}

	private void frameSetting() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(width, height);
		setTitle(APP_NAME + " " + VERSION);
	}

	private void setCompileErrorNumber() {
		JLabel label = new JLabel();
		String string = "���Ȃ��̂���܂ł̑��R���p�C���G���[�� �F " + manager.getTotalErrorCount();
		label.setText(string);
		label.setMaximumSize(new Dimension(width, height / 24));
		label.setFont(new Font("Font2DHandle", Font.BOLD, 16));
		// CCAchivementButton achivementButton = new CCAchivementButton(manager,
		// label);
		// achivementButton.setBounds(10, 5, 350, 25);
		// label.setBounds(10, 5, 350, 25);

		// label �̔w�i��ݒ肷��ꍇ�͔w�i��s�����ɂ��鏈���������邱��
		// label.setBackground(Color.yellow);
		// label.setOpaque(true);

		LineBorder lineborder = new LineBorder(Color.YELLOW, 2, true);
		label.setBorder(lineborder);

		headerPanel.add(label, BorderLayout.WEST);
	}

	private void setMiniGraphButton() {
		ArrayList<CCErrorElementButton2> buttons = new ArrayList<CCErrorElementButton2>();
		GridLayout gridlayout = new GridLayout();

		gridlayout.setRows((height * 15 / 16) / buttonHeight);
		gridlayout.setColumns(width / buttonWidth);
		buttonEreaPanel.setLayout(gridlayout);

		// �G���[ID���Ƃ̐��l���������݁A�{�^������������
		for (CCCompileErrorList list : manager.getAllLists()) {
			CCErrorElementButton2 button = new CCErrorElementButton2(
					buttonWidth, buttonHeight, list, manager.getLibDir(),
					manager.getBase());
			buttons.add(button);
		}

		// �{�^����z�u����
		int i = 1;
		for (int x = 0; x < width; x += buttonWidth) {
			for (int y = height / 16; y < height - buttonHeight; y += buttonHeight) {
				if (manager.getAllLists().size() >= i) {
					if (manager.getList(i).getErrors().size() > 0) {
						buttonEreaPanel.add(buttons.get(i - 1));
					} else {
						buttonEreaPanel.add(setEmptyButton());
					}
					i++;
				} else {
					buttonEreaPanel.add(setEmptyButton());
				}
			}
		}
	}

	// �N���b�N�ł��Ȃ��{�^�����쐬
	private JButton setEmptyButton() {
		JButton emptyButton = new JButton("������");
		emptyButton.setEnabled(false);
		emptyButton.setToolTipText("�������ł�");
		emptyButton.setBackground(Color.GRAY);
		emptyButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		// emptyButton.setBounds(x, y, buttonWidth, buttonHeight);
		// rootPanel.add(emptyButton);
		return emptyButton;
	}
}