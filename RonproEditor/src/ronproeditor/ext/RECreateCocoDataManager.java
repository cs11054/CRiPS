package ronproeditor.ext;

import ppv.app.datamanager.PPDataManager;
import ronproeditor.REApplication;
import src.coco.controller.CCAddCompileErrorKinds;
import src.coco.controller.CCCompileErrorConverter;
import src.coco.controller.CCCompileErrorKindLoader;
import src.coco.model.CCCompileErrorManager;

// TODO CompileError.csv���o�͂������PPV���N���ł��Ȃ��Ȃ�s�������
public class RECreateCocoDataManager {
	REApplication application;

	private static String PPV_ROOT_DIR = ".ppv";// MyProjects/.ppv�t�H���_�ɓW�J����
	private static String ORIGINAL_KINDS_FILE = "ext/cocoviewer/ErrorKinds.csv"; // ext����ErrorKinds
	private static String ORIGINAL_DATA_FILE = "CompileError.csv"; // ppv����o�͂����csv�t�@�C��
	private static String KINDS_FILE = "MyErrorKinds.csv"; // ErrorKinds.csv�ɂȂ��R���p�C���G���[����ǉ������t�@�C��
	private static String DATA_FILE = "CompileErrorLog.csv"; // Coco�p�̃R���p�C���G���[�f�[�^

	public RECreateCocoDataManager(REApplication application) {
		this.application = application;
	}

	public void createCocoData() {
		// CompileError.csv�������I�ɃG�N�X�|�[�g����
		autoExportCompileErrorCSV();

		// �����I�ɃG�N�X�|�[�g�����t�@�C����Coco�p�f�[�^�ɕϊ�����
		convertCompileErrorData();
	}

	private void autoExportCompileErrorCSV() {
		REPresVisualizerManager ppvManager = new REPresVisualizerManager(
				application);
		ppvManager.exportAndImportAll();
		PPDataManager ppDataManager = ppvManager.getPPDataManager();
		ppDataManager.setLibDir(application.getLibraryManager().getDir());
		// TODO Hardcoding
		ppDataManager.openProjectSet("hoge", true, true, true);
	}

	private void convertCompileErrorData() {
		CCCompileErrorManager manager = new CCCompileErrorManager();
		String ppvRootPath = application.getSourceManager().getCRootDirectory()
				.findOrCreateDirectory(PPV_ROOT_DIR).getAbsolutePath()
				.toString()
				+ "/";

		checkAllFileExist();

		// �G���[�̎�ރf�[�^�����[�h
		CCCompileErrorKindLoader kindloader = new CCCompileErrorKindLoader(
				manager);
		kindloader.load(ORIGINAL_KINDS_FILE);

		// CompileError�f�[�^��Coco�p�ɃR���o�[�g
		try {
			CCCompileErrorConverter errorConverter = new CCCompileErrorConverter(
					manager);
			errorConverter.convertData(ppvRootPath + ORIGINAL_DATA_FILE,
					ppvRootPath + DATA_FILE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// �ǉ��̃G���[�f�[�^����������MyKinds���쐬
		try {
			CCAddCompileErrorKinds addCompileErrorKinds = new CCAddCompileErrorKinds(
					manager, kindloader.getLines());
			addCompileErrorKinds.addKinds(ORIGINAL_KINDS_FILE, ppvRootPath
					+ KINDS_FILE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkAllFileExist() {
		checkOneFileExist(DATA_FILE);
		checkOneFileExist(KINDS_FILE);
		checkOneFileExist(ORIGINAL_DATA_FILE);
		checkOneFileExist(ORIGINAL_KINDS_FILE);
	}

	private void checkOneFileExist(String filename) {
		application.getSourceManager().getCRootDirectory()
				.findOrCreateDirectory(PPV_ROOT_DIR).findOrCreateFile(filename);
	}
}
