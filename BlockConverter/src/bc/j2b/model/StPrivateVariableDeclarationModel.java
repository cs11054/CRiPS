package bc.j2b.model;

public class StPrivateVariableDeclarationModel extends
		StVariableDeclarationModel implements Cloneable {

	private final int privateVariableBlockHeight = 40;
	private String modifer = "";

	public StPrivateVariableDeclarationModel() {
		setBlockHeight(privateVariableBlockHeight);
	}

	public void setModifer(String modifer) {
		this.modifer = modifer;
	}

	@Override
	public String getGenusName() {
		String genusName;
		if (isProjectObject()) {
			genusName = "object-" + getType();
		} else {
			genusName = convertJavaTypeToBlockGenusName(getType());
		}
		if (genusName.equals("number")) {
			genusName = "int-number";
		}

		if (isArray()) {
			genusName += "-arrayobject";
		}

		return "private-" + modifer + "var-" + genusName;
	}

}
