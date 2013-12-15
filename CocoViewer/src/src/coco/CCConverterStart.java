package src.coco;

import java.io.IOException;
import java.util.Scanner;

import src.coco.controller.CCAddCompileErrorKinds;
import src.coco.controller.CCCompileErrorConverter;
import src.coco.controller.CCCompileErrorKindLoader;
import src.coco.model.CCCompileErrorManager;

public class CCConverterStart {

	/**
	 * �R���o�[�^�[���N������O�ɁCtestbase�ɓ����Ă���cocoviewer.zip��
	 * ppv��CCCompileError.csv���쐬���邱�ƁD
	 * 
	 * @throws IOException
	 */

	public static void main(String[] args) throws IOException {
		new CCConverterStart().run();
	}

	public void run() throws IOException {
		CCCompileErrorManager manager = new CCCompileErrorManager();
		CCCompileErrorKindLoader kindloader = new CCCompileErrorKindLoader(
				manager);
		kindloader.load("ErrorKinds.csv");

		// �ԊґO�̃t�@�C��������
		System.out.print("�R���o�[�g�������f�[�^����� : ");
		Scanner scanner = new Scanner(System.in);
		String resource = scanner.next();
		scanner.close();
		CCCompileErrorConverter errorconverter = new CCCompileErrorConverter(
				manager);
		errorconverter.convertData(resource, "CompileErrorLog.csv");

		CCAddCompileErrorKinds addcompileerrorkinds = new CCAddCompileErrorKinds(
				manager, kindloader.getLines());
		addcompileerrorkinds.addKinds("ErrorKinds.csv", "MyErrorKinds.csv");

		System.out.println("�ϊ��I��");
	}
}