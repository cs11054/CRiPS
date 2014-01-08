package src.coco.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import src.coco.model.CCCompileErrorManager;

public class CCCompileErrorConverter extends CCCsvFileLoader {

	private CCCompileErrorManager manager;
	// private int addErrorID;
	private String CAMMA = ",";
	PrintWriter pw;

	public CCCompileErrorConverter(CCCompileErrorManager manager) {
		this.manager = manager;
		// addErrorID = manager.getAllLists().size() + 1;
	}

	public void convertData(String inFileName, String outFileName)
			throws IOException {
		File outfile = new File(outFileName);
		pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outfile), "sjis")));
		inputHeader(outfile);
		loadData(inFileName);
		pw.flush();
		pw.close();
	}

	private void inputHeader(File outFile) throws IOException {
		StringBuffer buf = new StringBuffer();

		buf.append("ErrorID");
		buf.append(CAMMA);
		buf.append("�t�@�C���p�X");
		buf.append(CAMMA);
		buf.append("��������");
		buf.append(CAMMA);
		buf.append("�C����������");
		buf.append(CAMMA);
		buf.append("�C������");
		// buf.append("ErrorID,�t�@�C����,��������,�C������");
		pw.println(buf.toString());
	}

	protected void separeteData(List<String> lines) throws IOException {
		// working time = false �Ȃ�f�[�^��ϊ����Ȃ�
		if (lines.get(15).equals("false")) {
			return;
		}

		StringBuffer buf = new StringBuffer();

		// // errorID��messageList��manager�ɍ����indexOf���\�b�h�ŉ���
		// // ���݂��Ă��Ȃ�errorID�̏ꍇ�A�V�����G���[���b�Z�[�W���L�^����
		// // ��ɃV���{���Ȃǂ̃`�F�b�N�����Ă���getMessageID������`�ɂ��A�i�V���{���j�ȂǂɑΉ�����
		// int errorID = 0;
		// String element = "";
		// if (lines.get(7) != null) {
		// element = "�i" + lines.get(7) + "�j";
		// }
		//
		// String message = lines.get(5) + element;
		//
		// try {
		// errorID = manager.getMessagesID(message);
		// } catch (Exception e) {
		// errorID = addErrorID;
		// manager.put(errorID, 6, message);
		// addErrorID++;
		// }
		//
		// String projectname = "";
		// String filename = lines.get(4);
		//
		// // �J�n�����̓t�@�C���̃t���p�X���玝���Ă���
		// // long beginTime = calculationBeginTime(lines.get(14));
		// long beginTime = 0;
		//
		// // �C�����Ԃ͎��o���Ď��Ԃ��v�Z���邱�Ƃɐ�������
		// int correctTime = calculationCorrectTime(lines.get(16));

		// errorID��messageList��manager�ɍ����indexOf���\�b�h�ŉ���
		// ���݂��Ă��Ȃ�errorID�̏ꍇ�A�V�����G���[���b�Z�[�W���L�^����
		// ��ɃV���{���Ȃǂ̃`�F�b�N�����Ă���getMessageID������`�ɂ��A�i�V���{���j�ȂǂɑΉ�����
		int errorID = 0;
		String element = "";
		if (lines.get(5) != null) {
			element = "�i" + lines.get(5) + "�j";
		}

		String message = lines.get(3) + element;

		try {
			errorID = manager.getMessagesID(message);
		} catch (Exception e) {
			return;
			// ErrorKinds.csv �ɂȂ��R���p�C���G���[�͕ʓr�ۑ�����
			// errorID = addErrorID;
			// manager.put(errorID, 6, message);
			// addErrorID++;
		}

		// �t�@�C���p�X�����Ă����āCCCCompileError�̂ق���filename�Ȃǂ�����
		String filePath = lines.get(2).replace("\\", "/");

		// spilt�͒���\\�ŋ�؂邱�Ƃ��ł��Ȃ��̂ŁC��������/�ɕϊ�����
		// ���R�ɂ��Ă͌���������邱��
		// String projectname = "";
		// String filename;
		//
		// String filepath = lines.get(2).replace("\\", "/");
		// String[] filepathSegments = filepath.split("/");
		// if (filepathSegments.length > 4) {
		// // �b��_�v���̂�
		// projectname = filepathSegments[filepathSegments.length - 4];
		// filename = filepathSegments[filepathSegments.length - 1];
		// } else {
		// filename = lines.get(2);
		// }

		// ��������
		long beginTime = 0;
		if (lines.get(12).indexOf(" ") == -1) {
			beginTime = Long.parseLong(lines.get(12));
		}

		// �C����������
		long endTime = 0;
		if (lines.get(13).indexOf(" ") == -1) {
			endTime = Long.parseLong(lines.get(13));
		} else {
			endTime = calculationCorrectTimeAsMills(lines.get(14));
		}

		// �C������
		int correctTime = 0;
		correctTime = Integer.parseInt(lines.get(18));

		// �f�[�^����������
		buf.append(String.valueOf(errorID));
		buf.append(CAMMA);
		buf.append(filePath);
		buf.append(CAMMA);
		buf.append(String.valueOf(beginTime));
		buf.append(CAMMA);
		buf.append(String.valueOf(endTime));
		buf.append(CAMMA);
		buf.append(String.valueOf(correctTime));
		pw.println(buf.toString());
		// out.write(errorID + "," + filename + "," + beginTime + ","
		// + correctTime + "\n");
	}

	// private long calculationBeginTime(String data) {
	// String[] tokenizer = data.split(" ");
	// String[] dates = tokenizer[0].split("/");
	// String[] times = tokenizer[1].split(":");
	//
	// int year = Integer.parseInt(dates[0]);
	// int month = Integer.parseInt(dates[1]);
	// int day = Integer.parseInt(dates[2]);
	// int hour = Integer.parseInt(times[0]);
	// int minute = Integer.parseInt(times[1]);
	// // int second = Integer.parseInt(times[2]);
	// int second = 0;
	//
	// Calendar calender = Calendar.getInstance();
	// calender.set(year, month, day, hour, minute, second);
	//
	// return calender.getTimeInMillis();
	// }

	private int calculationCorrectTimeAsMills(String time) {
		String[] tokanizer = time.split(":");
		int hour = Integer.parseInt(tokanizer[0]) * 3600;
		int minute = Integer.parseInt(tokanizer[1]) * 60;
		int second = Integer.parseInt(tokanizer[2]);
		return hour + minute + second * 1000;
	}
}